package com.example.application_team3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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

        bottomAppBar.setOnMenuItemClickListener(item -> {
            if (item.getItemId()==R.id.bottomNav_back){
                Intent intent = new Intent(Login_elderly.this, MainActivity.class);
                startActivity(intent);
            }
            return false;
        });

    }

    private void logIn_button(){
        Button button_login = findViewById(R.id.button3);
        button_login.setOnClickListener(view -> {

            _user_name = navigator.getEditTextValue(R.id.editTextText);
            _pass = navigator.getEditTextValue(R.id.editTextNumberPassword);
            navigator.saveInputToPreferences(_user_name, _pass, checkBoxRememberMe.isChecked());
            navigator.logInElderly(_user_name, _pass);
        });
    }
}