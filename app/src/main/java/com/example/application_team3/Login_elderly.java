package com.example.application_team3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.bottomappbar.BottomAppBar;

public class Login_elderly extends AppCompatActivity {
    String _user_name, _pass;
    ViewNavigator navigator = new ViewNavigator(this);
    private CheckBox checkBoxRememberMe;

    BottomAppBar bottomAppBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_elderly);
        logIn_button();
        bottomAppBar =findViewById(R.id.bottomAppBar);

        checkBoxRememberMe = findViewById(R.id.checkBox_rememberPassword);
        navigator.setRememberMeValues(this, R.id.editTextText,
              R.id.editTextNumberPassword, checkBoxRememberMe);
        Button bt = findViewById(R.id.log_in_cargiver_bt);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigator.goToNextActivity(Log_in.class);
            }
        });


    }

    private void logIn_button(){
        Button button_login = findViewById(R.id.button3);
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                _user_name = navigator.getEditTextValue(R.id.editTextText);
                _pass = navigator.getEditTextValue(R.id.editTextNumberPassword);
                navigator.saveInputToPreferences(_user_name, _pass, checkBoxRememberMe.isChecked());
                navigator.logInElderly(_user_name, _pass);

            }
        });
    }
}