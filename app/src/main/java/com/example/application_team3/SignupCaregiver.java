package com.example.application_team3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

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

        Button back_btn = findViewById(R.id.button_back);
        back_btn.setOnClickListener(view -> finish());
        signUp_button();

    }

   private void signUp_button(){

        Button sign_up_btn = findViewById(R.id.button_sign_up);
        sign_up_btn.setOnClickListener(view -> {
            EditText nameEditText = findViewById(R.id.editTextText_name);
            EditText usernameEditText = findViewById(R.id.username);
            EditText emailEditText = findViewById(R.id.email);
            EditText passEditText = findViewById(R.id.password);
            EditText pass2EditText = findViewById(R.id.rewritepassword);

            String name = nameEditText.getText().toString();
            String username = usernameEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String pass = passEditText.getText().toString();
            String pass2 = pass2EditText.getText().toString();

            signUpCaregiver(name, username, email,pass,pass2);

        });
    }

    public void signUpCaregiver(String _name, String _user_name, String _email, String _pass, String _pass2){

        if (!userControl.isValidName(_name))
            viewBuilder.notis("invalid name: " + _name, SignupCaregiver.this);

        if(!userControl.isValidEmail(_email))
            viewBuilder.notis("invalid Email: " + _email, SignupCaregiver.this);

        if(_pass.matches(_pass2)){
            if(!userControl.isValidPassword(_pass))
                viewBuilder.notis("invalid Password", SignupCaregiver.this);

        }
        else
            viewBuilder.notis("password doesn't match", SignupCaregiver.this);

        if (!userControl.isValidUsername(_user_name))
            viewBuilder.notis("invalid username: " + _user_name, SignupCaregiver.this);

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