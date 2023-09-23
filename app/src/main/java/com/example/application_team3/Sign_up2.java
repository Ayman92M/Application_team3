package com.example.application_team3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
    int x = 100;
    UserAccountControl user = new UserAccountControl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up2);


        Button sign_up_bt = findViewById(R.id.button_sign_up);
        sign_up_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sign_up_func();

            }
        });
    }

    public void sign_up_func(){
        String _name = getEditTextValue(R.id.editTextText_name);

        String _email = getEditTextValue(R.id.email);
        String _pass = getEditTextValue(R.id.password);
        String _pass2 = getEditTextValue(R.id.rewritepassword);
        //String _user_name = ( (EditText) findViewById(R.id.username)).getText().toString();

        if (!user.isValidName(_name))
            Toast.makeText(Sign_up2.this, "invalid name", Toast.LENGTH_SHORT).show();

        //if (!user.isValidUsername(( (EditText) findViewById(R.id.username)).getText().toString()))
            //Toast.makeText(Sign_up2.this, "invalid user name", Toast.LENGTH_SHORT).show();

        if(!user.isValidEmail(_email))
            Toast.makeText(Sign_up2.this, "invalid Email", Toast.LENGTH_SHORT).show();

        if(_pass.matches(_pass2)){
            if(!user.isValidPassword(_pass))
                Toast.makeText(Sign_up2.this, "invalid Password", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(Sign_up2.this, "password doesn't match", Toast.LENGTH_SHORT).show();

        if (user.isValidName(_name) &&
                user.isValidEmail(_email) && _pass.matches(_pass2) && user.isValidPassword(_pass) ){

            Toast.makeText(Sign_up2.this, "200", Toast.LENGTH_SHORT).show();
            String _user_name = create_username(_name);
            db.registerCaregiver(_name, _user_name, _pass, null);
        }
    }

    private String create_username(String name){
        String user_name = name.substring(0, 3);
        return user_name+x;
    }
    private String getEditTextValue(int editTextId) {
        EditText editText = findViewById(editTextId);
        String value = editText.getText().toString();
        return value;
    }


}