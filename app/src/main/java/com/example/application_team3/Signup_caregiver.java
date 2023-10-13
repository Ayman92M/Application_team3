package com.example.application_team3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;

public class Signup_caregiver extends AppCompatActivity {

    String _name, _user_name, _pass, _pass2, _email;
    Controller control;

    private final UserAccountControl userControl = new UserAccountControl();

    private ViewBuilder view;
    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_caregiver);
        Intent get_info = getIntent();
        control = (Controller) get_info.getSerializableExtra("controller");
        view = control.getView();
        db = control.getDb();
        //signUp_button();

    }

   /* private void signUp_button(){

        Button sign_up_bt = findViewById(R.id.button_sign_up);
        sign_up_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _name = navigator.getEditTextValue(R.id.editTextText_name);
                _user_name = navigator.getEditTextValue(R.id.username);
                _email = navigator.getEditTextValue(R.id.email);
                _pass = navigator.getEditTextValue(R.id.password);
                _pass2 = navigator.getEditTextValue(R.id.rewritepassword);

                signUpCaregiver(_name, _user_name, _email,_pass,_pass2);

            }
        });
    }*/

    public void signUpCaregiver(String _name, String _user_name, String _email, String _pass, String _pass2){

        if (!userControl.isValidName(_name))
            view.notis("invalid name", Signup_caregiver.this);

        if(!userControl.isValidEmail(_email))
            view.notis("invalid Email", Signup_caregiver.this);

        if(_pass.matches(_pass2)){
            if(!userControl.isValidPassword(_pass))
                view.notis("invalid Password", Signup_caregiver.this);

        }
        else
            view.notis("password doesn't match", Signup_caregiver.this);

        if (!userControl.isValidUsername(_user_name))
            view.notis("invalid user name", Signup_caregiver.this);

        else{
            Task<DataSnapshot> caregiverDB = db.fetchCaregiverDB();

            Tasks.whenAll(caregiverDB).addOnCompleteListener(task -> {
                if(caregiverDB.getResult().child(_user_name).exists()){
                    view.notis("User name is already exists, use a different user name", Signup_caregiver.this);
                }
                else {
                    if (userControl.isValidName(_name) &&
                            userControl.isValidEmail(_email) && _pass.matches(_pass2) && userControl.isValidPassword(_pass) ){
                        view.notis("Success", Signup_caregiver.this);
                        db.registerCaregiver(_name, _user_name, _pass, null);

                        control.caregiverLogIn(_user_name, _pass, Signup_caregiver.this);

                    }
                }
            });
        }
    }


}