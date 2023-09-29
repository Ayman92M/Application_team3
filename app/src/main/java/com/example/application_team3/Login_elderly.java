package com.example.application_team3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

public class Login_elderly extends AppCompatActivity {
    String _user_name, _pass;
    ViewNavigator navigator = new ViewNavigator(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_elderly);
        logIn_button();

    }

    private void logIn_button(){
        Button button_login = findViewById(R.id.button3);
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                _user_name = navigator.getEditTextValue(R.id.editTextText);
                _pass = navigator.getEditTextValue(R.id.editTextNumberPassword);
                navigator.logInElderly(_user_name, _pass);
            }
        });
    }

    /*
    private void logIn_process(){

        String _user_name = navigator.getEditTextValue(R.id.editTextText);
        String _pass = navigator.getEditTextValue(R.id.editTextNumberPassword);

        if (!user.isValidUsername(_user_name) || !user.isValidPin(_pass))
            navigator.notis("invalid user name or Pin");

        Task<Boolean> loginCheck = db.checkLoginElderly(_user_name, _pass);
        Tasks.whenAll(loginCheck).addOnCompleteListener(task -> {
            if(loginCheck.getResult())
                navigator.goToNextActivity(Elderly_home_test.class, "True","name", _user_name ,null, null);

            else
                navigator.notis("False");

        });

    }

     */



}