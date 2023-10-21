package com.example.application_team3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;

public class SignupElderly extends AppCompatActivity {

    Controller control;
    Database db;
    ViewBuilder vb;
    private final UserAccountControl userControl = new UserAccountControl();
    EditText nameText, usernameText, pinText, pin2Text, phoneNoText, dateOfBirthText, addressText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_elderly);

        Intent get_info = getIntent();
        control = (Controller) get_info.getSerializableExtra("controller");
        db = control.getDatabase();
        vb = control.getViewBuilder();

        Button back_btn = findViewById(R.id.button_back);
        back_btn.setOnClickListener(view -> finish());

        nameText = findViewById(R.id.editTextText2);
        usernameText = findViewById(R.id.editTextText9);
        pinText = findViewById(R.id.editTextText10);
        pin2Text = findViewById(R.id.editTextText11);
        phoneNoText = findViewById(R.id.editTextText12);
        dateOfBirthText = findViewById(R.id.editTextText13);
        addressText = findViewById(R.id.editTextText14);

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
        button_signup_elderly.setOnClickListener(view -> {
            String _name = nameText.getText().toString();
            String _username = usernameText.getText().toString();
            String _pin = pinText.getText().toString();
            String _pin2 = pin2Text.getText().toString();
            String _phoneNo = phoneNoText.getText().toString();
            String _dateOfBirth = dateOfBirthText.getText().toString();
            String _address = addressText.getText().toString();

            signUpElderly(_name, _username, _pin, _pin2, _phoneNo, _dateOfBirth, _address);
        });
    }

    public void signUpElderly(String _name, String _username, String _pin, String _pin2, String _phoneNo, String _dateOfBirth, String _address){

        if (!userControl.isValidName(_name))
            vb.notis("invalid name: " + _name, SignupElderly.this);


        if(_pin.matches(_pin2)){
            if(!userControl.isValidPin(_pin))
                vb.notis("invalid Password", SignupElderly.this);

        }
        else
            vb.notis("password doesn't match", SignupElderly.this);

        if (!userControl.isValidUsername(_username))
            vb.notis("invalid username: " + _username, SignupElderly.this);

        else{
            Task<DataSnapshot> caregiverDB = db.fetchCaregiverDB();

            Tasks.whenAll(caregiverDB).addOnCompleteListener(task -> {
                if(caregiverDB.getResult().child(_username).exists()){
                    vb.notis("User name is already exists, use a different user name", SignupElderly.this);
                }
                else {
                    if (userControl.isValidName(_name) &&
                             _pin.matches(_pin2) && userControl.isValidPin(_pin) ){
                        vb.notis("Success", SignupElderly.this);
                        Task<Long> registerTask = db.registerElderly(_name, _username, _pin, _phoneNo,control.getCaregiverUser().getName(), control.getCaregiverUser().getPid());

                        Tasks.whenAll(registerTask).addOnCompleteListener(task2 ->{
                            Long size = registerTask.getResult();
                            System.out.println("elderlycount = " + size);
                            finish();
                            System.out.println("finish -> goToActivity");
                            control.goToActivity(SignupElderly.this, CaregiverDash.class);
                        });

                    }
                }
            });
        }
    }
}
