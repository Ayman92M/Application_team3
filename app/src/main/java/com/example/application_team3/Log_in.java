package com.example.application_team3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;

import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;


public class Log_in extends AppCompatActivity {
    ViewNavigator navigator = new ViewNavigator(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        signUp_button();
        logIn_button();

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
                navigator.caregiverLogIn_process(_user_name, _pass);
                //logIn_process();
            }
        });
    }

    /*
    private void logIn_process(){
        String _user_name = navigator.getEditTextValue(R.id.editTextText);
        String _pass = navigator.getEditTextValue(R.id.editTextNumberPassword);

        if (!user.isValidUsername(_user_name) || !user.isValidPassword(_pass))
            navigator.notis("invalid user name or password");


        else{
            Task<Boolean> checkLogin = db.checkLoginCaregiver(_user_name, _pass);
            Task<DataSnapshot> caregiverTask = db.fetchCaregiver(_user_name);
            Tasks.whenAll(checkLogin, caregiverTask).addOnCompleteListener(task-> {
                if(checkLogin.getResult()){

                    CaregiverEntry caregiver = caregiverTask.getResult().getValue(CaregiverEntry.class);
                    if (caregiver != null)
                        navigator.goToNextActivity(Caregiver_dash.class, "success","name",
                                caregiver.getName() ,"pid", caregiver.getPid());
                }
                else{
                    navigator.notis("False");
                }

            });
        }
    }

     */

}