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



public class LoginCaregiver extends AppCompatActivity {
    private CheckBox checkBoxRememberMe;

    SharedPreferences preferences;

    Controller control;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_caregiver);
        Intent get_info = getIntent();
        control = (Controller) get_info.getSerializableExtra("controller");
        checkBoxRememberMe = findViewById(R.id.checkBox_rememberMe);

        logUt();
        logIn_button();

        TextView signup_btn = findViewById(R.id.textView_signup);
        signup_btn.setOnClickListener(view -> control.goToActivity(LoginCaregiver.this, SignupCaregiver.class));

        setRememberMeValues();
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

            checkBoxRememberMe.setChecked(true);
        }
    }

    public boolean getRememberMeStatus() {
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
            control.caregiverLogIn(_user_name, _pass, LoginCaregiver.this);
        });
    }

    private void logUt(){
        Intent reset = getIntent();
        String bool = reset.getStringExtra("logut");
        System.out.println(" log ut activity " + bool);
        if (bool != null && bool.equals("true")){
            System.out.println(" log ut activity If" + bool);
            control.logout(this);
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