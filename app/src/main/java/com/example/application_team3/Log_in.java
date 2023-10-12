package com.example.application_team3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;

import com.google.android.gms.tasks.Tasks;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.firebase.database.DataSnapshot;


public class Log_in extends AppCompatActivity {
    ViewNavigator navigator = new ViewNavigator(this);
    private CheckBox checkBoxRememberMe;
    private static final String EXTRA_LOGOUT = "logout";
    BottomAppBar bottomAppBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        bottomAppBar = findViewById(R.id.bottomAppBar);

        logUt();
        signUp_button();
        logIn_button();

        checkBoxRememberMe = findViewById(R.id.checkBox_rememberMe);
        navigator.setRememberMeValues(this, R.id.editTextText,
                        R.id.editTextNumberPassword, checkBoxRememberMe);

        bottomAppBar.setOnMenuItemClickListener(item -> {
            if (item.getItemId()==R.id.bottomNav_back){
                Intent intent = new Intent(Log_in.this, MainActivity.class);
                startActivity(intent);
            }
            return false;
        });

    }

    private void signUp_button(){
        TextView signup_bt = findViewById(R.id.textView_signup);
        navigator.goToNextActivity(signup_bt, Signup_caregiver.class, null, null, null, null);
    }

    private void logIn_button(){
        Button button_login = findViewById(R.id.button3);
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String _user_name = navigator.getEditTextValue(R.id.editTextText);
                String _pass = navigator.getEditTextValue(R.id.editTextNumberPassword);

                navigator.saveInputToPreferences(_user_name, _pass, checkBoxRememberMe.isChecked());
                navigator.caregiverLogIn_process(_user_name, _pass);
            }
        });
    }

    private void logUt(){
        Intent reset = getIntent();
        String bool = reset.getStringExtra("logut");
        System.out.println(" log ut activity " + bool);
        if (bool != null && bool.equals("true")){
            System.out.println(" log ut activity If" + bool);
            navigator.logout(this, MainActivity.class);
        }
    }

}