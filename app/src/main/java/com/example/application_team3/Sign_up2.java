package com.example.application_team3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

public class Sign_up2 extends AppCompatActivity {
    Database db = new Database();
    EditText name, username, password, password2, email;
    UserAccountControl user = new UserAccountControl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up2);


        Button sign_up_bt = findViewById(R.id.button_sign_up);
        name = findViewById(R.id.editTextText_name);
        sign_up_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String _name = name.getText().toString();
                String _user_name = ( (EditText) findViewById(R.id.username)).getText().toString();
                String _email = ( (EditText) findViewById(R.id.email)).getText().toString();
                String _pass =((EditText) findViewById(R.id.password)).getText().toString();
                String _pass2 =((EditText) findViewById(R.id.rewritepassword)).getText().toString();

                if (!user.isValidName(_name))
                    Toast.makeText(Sign_up2.this, "invalid name", Toast.LENGTH_SHORT).show();

                    if (!user.isValidUsername(( (EditText) findViewById(R.id.username)).getText().toString()))
                        Toast.makeText(Sign_up2.this, "invalid user name", Toast.LENGTH_SHORT).show();

                    if(!user.isValidEmail(_email))
                        Toast.makeText(Sign_up2.this, "invalid Email", Toast.LENGTH_SHORT).show();

                    if(_pass.matches(_pass2)){
                        if(!user.isValidPassword(_pass))
                            Toast.makeText(Sign_up2.this, "invalid Password", Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(Sign_up2.this, "password doesn't match", Toast.LENGTH_SHORT).show();

                if (user.isValidName(_name) &&
                        user.isValidUsername(( (EditText) findViewById(R.id.username)).getText().toString())
                        && user.isValidEmail(_email) && _pass.matches(_pass2) && user.isValidPassword(_pass) ){

                    Toast.makeText(Sign_up2.this, "200", Toast.LENGTH_SHORT).show();
                    db.registerCaregiver(_name, _user_name, _pass, null, "1999", "country");
                }

            }
        });
    }
}