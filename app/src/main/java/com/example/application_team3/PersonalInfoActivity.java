package com.example.application_team3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomappbar.BottomAppBar;

public class PersonalInfoActivity extends AppCompatActivity {

    Controller control;
    private TextView nameText;
    private TextView usernameText;
    private TextView pinText;
    private TextView phoneNoText;
    private TextView dateOfBirthText;
    private TextView addressText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        Intent get_info = getIntent();
        control = (Controller) get_info.getSerializableExtra("controller");

        Button back_btn = findViewById(R.id.button_back);
        back_btn.setOnClickListener(view -> finish());

        nameText = this.findViewById(R.id.editTextText);
        usernameText = this.findViewById(R.id.editTextText9);
        pinText = this.findViewById(R.id.editTextText10);
        phoneNoText = this.findViewById(R.id.editTextText12);
        dateOfBirthText = this.findViewById(R.id.editTextText13);
        addressText = this.findViewById(R.id.editTextText14);



        showData();

        edit_button();



    }

    private void showData(){

        ElderlyEntry elderly = control.getElderlyUser();
        nameText.setText(elderly.getName());
        usernameText.setText(elderly.getPid());
        pinText.setText(elderly.getPin());
        phoneNoText.setText(elderly.getPhoneNo());
        dateOfBirthText.setText(elderly.getBirthday());
        addressText.setText(elderly.getAddress());

    }

    private void edit_button()
    {
        Button button_edit = findViewById(R.id.edit_button);
        button_edit.setOnClickListener(view -> control.goToActivity(PersonalInfoActivity.this, SignupElderly.class));
    }
}