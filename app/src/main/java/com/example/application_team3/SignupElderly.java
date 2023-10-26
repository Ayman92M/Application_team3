package com.example.application_team3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;

import java.util.HashMap;
import java.util.Objects;

public class SignupElderly extends AppCompatActivity {

    Controller control;
    Database db;
    ViewBuilder vb;
    private final UserAccountControl userControl = new UserAccountControl();
    EditText nameText, usernameText, pinText, pin2Text, phoneNoText, dateOfBirthText, addressText;

    String usernameHint;

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

        usernameHint = "";
        usernameText.setText(usernameHint);

        if(control.getElderlyUser() != null)
        {
            ElderlyEntry elderly = control.getElderlyUser();

            nameText.setText(elderly.getName());
            usernameText.setText(elderly.getUsername());
            pinText.setText(elderly.getPin());
            phoneNoText.setText(elderly.getPhoneNo());
            dateOfBirthText.setText(elderly.getBirthday());
            addressText.setText(elderly.getAddress());
        }

        nameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() >= 1 && usernameText.getText().toString().equals(usernameHint)) {
                    usernameText.setText("");
                    usernameHint = "";
                    if (charSequence.length() < 3) {
                        for (int j = 0; j < charSequence.length(); j++) {
                            usernameHint += charSequence.charAt(j);
                        }
                    }
                    else{
                        for(int j = 0; j < 3; j++){
                            usernameHint += charSequence.charAt(j);
                        }
                    }

                    Task<DataSnapshot> elderlyDBTask = db.fetchElderlyDB();

                    Tasks.whenAll(elderlyDBTask).addOnCompleteListener(task -> {
                        DataSnapshot elderlyDB = elderlyDBTask.getResult();
                        int number = 100;
                        String usernameCheck = usernameHint + number;
                        while (elderlyDB.hasChild(usernameCheck)) {
                            number += 1;
                            usernameCheck = usernameHint + number;
                        }
                        usernameHint += number;
                        usernameHint = usernameHint.toLowerCase();
                        usernameText.setText(usernameHint);
                    });
                }
                else if(charSequence.length() == 0 && usernameText.getText().toString().length() < 1){
                    usernameHint = "";
                    usernameText.setText(usernameHint);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        Button button_signup_elderly = findViewById(R.id.button11);
        button_signup_elderly.setOnClickListener(view -> {
            String _name = nameText.getText().toString();
            String _username = usernameText.getText().toString();
            String _pin = pinText.getText().toString();
            String _pin2 = pin2Text.getText().toString();
            String _phoneNo = phoneNoText.getText().toString();
            String _dateOfBirth = dateOfBirthText.getText().toString();
            String _address = addressText.getText().toString();

            checkUserData(_name, _username, _pin, _pin2, _phoneNo, _dateOfBirth, _address);
        });
    }

    public void checkUserData(String _name, String _username, String _pin, String _pin2, String _phoneNo, String _dateOfBirth, String _address){

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
                if(caregiverDB.getResult().child(_username).exists() && !Objects.equals(control.getElderlyUser().getUsername(), _username)){
                    vb.notis("User name is already exists, use a different user name", SignupElderly.this);
                }
                else {
                    if (userControl.isValidName(_name) &&
                            _pin.matches(_pin2) && userControl.isValidPin(_pin) ){
                        vb.notis("Success", SignupElderly.this);
                        registerElderly(_name, _username,  _pin, _phoneNo, _dateOfBirth, _address);
                        finish();
                    }
                }
            });
        }
    }

    private void registerElderly(String _name, String _username, String _pin, String _phoneNo, String _dateOfBirth, String _address){
        db.registerElderly(_name, _username, _pin, _phoneNo, _dateOfBirth, _address, control.getCaregiverUser().getName(), control.getCaregiverUser().getUsername());
        ElderlyEntry elderly = control.getElderlyUser();
        if(elderly != null){
            if(!Objects.equals(elderly.getUsername(), _username) || !Objects.equals(elderly.getName(), _name))
            {
                db.deleteElderly(elderly.getUsername());
                HashMap<String, String> caregiversList = elderly.getCaregivers();
                caregiversList.forEach((username, name) -> db.assignElderly(username, name, _username, _name));
            }
            elderly.setName(_name);
            elderly.setUsername(_username);
            elderly.setPin(_pin);
            elderly.setPhoneNo(_phoneNo);
            elderly.setBirthday(_dateOfBirth);
            elderly.setAddress(_address);
            control.setElderlyUser(elderly);
        }
    }

}
