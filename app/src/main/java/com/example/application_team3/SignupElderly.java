package com.example.application_team3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class SignupElderly extends AppCompatActivity {

    Controller control;
    EditText nameText, usernameText, pinText, phoneNoText, dateOfBirthText, addressText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_elderly);

        Intent get_info = getIntent();
        control = (Controller) get_info.getSerializableExtra("controller");

        Button back_btn = findViewById(R.id.button_back);
        back_btn.setOnClickListener(view -> finish());

        nameText = this.findViewById(R.id.editTextText2);
        usernameText = this.findViewById(R.id.editTextText9);
        pinText = this.findViewById(R.id.editTextText10);
        phoneNoText = this.findViewById(R.id.editTextText12);
        dateOfBirthText = this.findViewById(R.id.editTextText13);
        addressText = this.findViewById(R.id.editTextText14);

        if(control.getElderlyUser() != null)
        {
            ElderlyEntry elderly = control.getElderlyUser();

            nameText.setText(elderly.getName());
            usernameText.setText(elderly.getPid());
            pinText.setText(elderly.getPin());
            phoneNoText.setText(elderly.getPhoneNo());
            dateOfBirthText.setText(elderly.getBirthday());
            addressText.setText(elderly.getAddress());
        }

        Button button_signup_elderly = findViewById(R.id.button11);
        button_signup_elderly.setOnClickListener(view -> control.getDatabase().registerElderly(nameText.getText().toString(), usernameText.getText().toString(),
                pinText.getText().toString(), phoneNoText.getText().toString(),
                control.getCaregiverUser().getName(), control.getCaregiverUser().getPid()));
    }
}
