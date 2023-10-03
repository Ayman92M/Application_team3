package com.example.application_team3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Signup_elderly extends AppCompatActivity {
    String _name, _user_name, _pin, _pin2;
    ViewNavigator navigator = new ViewNavigator(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_elderly);

        signup_bt();
    }

    private void signup_bt(){
        Button button_signup_elderly = findViewById(R.id.button11);
        button_signup_elderly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _name = navigator.getEditTextValue(R.id.editTextText2);
                _user_name = navigator.getEditTextValue(R.id.editTextText9);
                _pin = navigator.getEditTextValue(R.id.editTextText10);
                _pin2 = navigator.getEditTextValue(R.id.editTextText11);

                Intent get_caregiver = getIntent();
                String _caregiverUserName = get_caregiver.getStringExtra("caregiverUserName");
                String _caregiverName = get_caregiver.getStringExtra("caregiverName");
                System.out.println("caregiverName S: " + _caregiverName);
                System.out.println("caregiverUserName S: " + _caregiverUserName);
                navigator.signUpAnElderly(_name, _user_name, _pin, _pin2, _caregiverName, _caregiverUserName);
            }
        });
    }

    /*
    private void signUp_process(){

        String _name = getEditTextValue(R.id.editTextText2);
        String _pin = getEditTextValue(R.id.editTextText10);
        String _pin2 = getEditTextValue(R.id.editTextText11);
        String _user_name = getEditTextValue(R.id.editTextText9);

        if (!user.isValidName(_name))
            notis("invalid name");



        if(_pin.matches(_pin2)){
            if(!user.isValidPin(_pin))
                notis("invalid Pin");

        }
        else
            notis("Pin doesn't match");

        if (!user.isValidUsername(_user_name))
            notis("invalid user name");

        else{
            Task<DataSnapshot> elderlyDB = db.fetchElderlyDB();

            Tasks.whenAll(elderlyDB).addOnCompleteListener(task -> {
                DataSnapshot snapshot = elderlyDB.getResult();
                if(snapshot.child(_user_name).exists()){
                    notis("User name is already exists, use a different user name");
                }
                else {
                    if (user.isValidName(_name) &&
                            _pin.matches(_pin2) && user.isValidPin(_pin) ){
                        notis("200");
                        db.registerElderly(_name, _user_name, _pin, null, null);

                    }
                }

            });

        }


    }
    */

}
