package com.example.application_team3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;

public class SignupCaregiver extends AppCompatActivity {

    Controller control;

    private final UserAccountControl userControl = new UserAccountControl();

    private ViewBuilder viewBuilder;
    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_caregiver);
        Intent get_info = getIntent();
        control = (Controller) get_info.getSerializableExtra("controller");
        if (control != null) {
            viewBuilder = control.getViewBuilder();
            db = control.getDatabase();
        }

        signUp_button();

    }

   private void signUp_button(){

        Button sign_up_btn = findViewById(R.id.button_sign_up);
        sign_up_btn.setOnClickListener(view -> {
            String name = findViewById(R.id.editTextText_name).toString();
            String user_name = findViewById(R.id.username).toString();
            String email = findViewById(R.id.email).toString();
            String pass = findViewById(R.id.password).toString();
            String pass2 = findViewById(R.id.rewritepassword).toString();

            signUpCaregiver(name, user_name, email,pass,pass2);

        });
    }

    public void signUpCaregiver(String _name, String _user_name, String _email, String _pass, String _pass2){

        if (!userControl.isValidName(_name))
            viewBuilder.notis("invalid name", SignupCaregiver.this);

        if(!userControl.isValidEmail(_email))
            viewBuilder.notis("invalid Email", SignupCaregiver.this);

        if(_pass.matches(_pass2)){
            if(!userControl.isValidPassword(_pass))
                viewBuilder.notis("invalid Password", SignupCaregiver.this);

        }
        else
            viewBuilder.notis("password doesn't match", SignupCaregiver.this);

        if (!userControl.isValidUsername(_user_name))
            viewBuilder.notis("invalid user name", SignupCaregiver.this);

        else{
            Task<DataSnapshot> caregiverDB = db.fetchCaregiverDB();

            Tasks.whenAll(caregiverDB).addOnCompleteListener(task -> {
                if(caregiverDB.getResult().child(_user_name).exists()){
                    viewBuilder.notis("User name is already exists, use a different user name", SignupCaregiver.this);
                }
                else {
                    if (userControl.isValidName(_name) &&
                            userControl.isValidEmail(_email) && _pass.matches(_pass2) && userControl.isValidPassword(_pass) ){
                        viewBuilder.notis("Success", SignupCaregiver.this);
                        db.registerCaregiver(_name, _user_name, _pass, null);

                        control.caregiverLogIn(_user_name, _pass, SignupCaregiver.this);

                    }
                }
            });
        }
    }


}