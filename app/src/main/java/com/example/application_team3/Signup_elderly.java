package com.example.application_team3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Signup_elderly extends AppCompatActivity {
    String _name, _user_name, _pin, _pin2;
    Intent get_caregiver;
    String _caregiverUserName, _caregiverName;
    ViewNavigator navigator = new ViewNavigator(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_elderly);

        Intent get_info = getIntent();

        if(get_info.getStringExtra("elderlyUserName") != null)
        {
            EditText nameText = this.findViewById(R.id.editTextText2);
            EditText usernameText = this.findViewById(R.id.editTextText9);
            EditText pinText = this.findViewById(R.id.editTextText10);
            EditText phoneNoText = this.findViewById(R.id.editTextText12);
            EditText dateOfBirthText = this.findViewById(R.id.editTextText13);
            EditText addressText = this.findViewById(R.id.editTextText14);

            nameText.setText(get_info.getStringExtra("elderlyName"));
            usernameText.setText(get_info.getStringExtra("elderlyUserName"));
            pinText.setText((get_info.getStringExtra("elderlyPin")));
            phoneNoText.setText(get_info.getStringExtra("elderlyPhoneNo"));
            dateOfBirthText.setText(get_info.getStringExtra("elderlyDateOfBirth"));
            addressText.setText(get_info.getStringExtra("elderlyAddress"));
        }

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

                get_caregiver = getIntent();
                _caregiverUserName = get_caregiver.getStringExtra("caregiverUserName");
                _caregiverName = get_caregiver.getStringExtra("caregiverName");
                System.out.println("caregiverName Signup_elderly: " + _caregiverName);
                System.out.println("caregiverUserName Signup_elderly: " + _caregiverUserName);
                navigator.signUpAnElderly(_name, _user_name, _pin, _pin2, _caregiverName, _caregiverUserName);
            }
        });
    }

}
