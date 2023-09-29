package com.example.application_team3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;

public class Signup_caregiver extends AppCompatActivity {
    Database db = new Database();
    String _name, _user_name, _pass, _pass2, _email;
    UserAccountControl user = new UserAccountControl();
    ViewNavigator navigator = new ViewNavigator(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_caregiver);

        signUp_button();
    }

    private void signUp_button(){

        Button sign_up_bt = findViewById(R.id.button_sign_up);
        sign_up_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _name = navigator.getEditTextValue(R.id.editTextText_name);
                _user_name = navigator.getEditTextValue(R.id.username);
                _email = navigator.getEditTextValue(R.id.email);
                _pass = navigator.getEditTextValue(R.id.password);
                _pass2 = navigator.getEditTextValue(R.id.rewritepassword);

                navigator.signUpCaregiver(_name, _user_name, _email,_pass,_pass2);

            }
        });
    }


}