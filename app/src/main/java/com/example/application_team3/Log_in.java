package com.example.application_team3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.bottomappbar.BottomAppBar;


public class Log_in extends AppCompatActivity {
    private CheckBox checkBoxRememberMe;

    BottomAppBar bottomAppBar;
    SharedPreferences preferences;

    Controller control;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        Intent get_info = getIntent();
        control = (Controller) get_info.getSerializableExtra("controller");
        bottomAppBar = findViewById(R.id.bottomAppBar);

        logUt();
        logIn_button();

        TextView signup_btn = findViewById(R.id.textView_signup);
        signup_btn.setOnClickListener(view -> control.goToActivity(Log_in.this, Signup_caregiver.class));

        setRememberMeValues();

        bottomAppBar.setOnMenuItemClickListener(item -> {
            if (item.getItemId()==R.id.bottomNav_back){
                Intent intent = new Intent(Log_in.this, MainActivity.class);
                startActivity(intent);
            }
            return false;
        });

    }

    public void setRememberMeValues() {
        preferences = this.getPreferences(Context.MODE_PRIVATE);

        String username = preferences.getString("username", "");
        EditText usernameText = findViewById(R.id.editTextText);
        usernameText.setText(username);


        if (getRememberMeStatus()) {
            String password = preferences.getString("password", "");
            EditText passwordText = findViewById(R.id.editTextNumberPassword);
            passwordText.setText(password);

            checkBoxRememberMe = findViewById(R.id.checkBox_rememberMe);
            checkBoxRememberMe.setChecked(true);
        }
    }

    public boolean getRememberMeStatus() {
        // Retrieve the "Remember Me" status from SharedPreferences
        return preferences.getBoolean("rememberMe", false);
    }

    private void logIn_button(){
        Button button_login = findViewById(R.id.button3);
        button_login.setOnClickListener(view -> {
            EditText usernameText = findViewById(R.id.editTextText);
            String _user_name = usernameText.getText().toString();
            EditText passwordText = findViewById(R.id.editTextNumberPassword);
            String _pass = passwordText.getText().toString();

            saveInputToPreferences(_user_name, _pass, checkBoxRememberMe.isChecked());
            control.caregiverLogIn(_user_name, _pass, Log_in.this);
        });
    }

    private void logUt(){
        Intent reset = getIntent();
        String bool = reset.getStringExtra("logut");
        System.out.println(" log ut activity " + bool);
        if (bool != null && bool.equals("true")){
            System.out.println(" log ut activity If" + bool);
            control.logout(this, MainActivity.class);
        }
    }

    public void saveInputToPreferences(String username, String password, boolean rememberMe) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.putBoolean("rememberMe", rememberMe);
        editor.apply();
    }
}