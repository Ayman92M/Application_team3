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
    EditText name, username, password, password2, email;
    boolean valid_username = false;
    int x = 100;
    UserAccountControl user = new UserAccountControl();

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
                signUp_process();

            }
        });
    }
    private void signUp_process(){
        String _name = getEditTextValue(R.id.editTextText_name);

        String _email = getEditTextValue(R.id.email);
        String _pass = getEditTextValue(R.id.password);
        String _pass2 = getEditTextValue(R.id.rewritepassword);
        String _user_name = getEditTextValue(R.id.username);

        if (!user.isValidName(_name))
            notis("invalid name");

        if(!user.isValidEmail(_email))
            notis("invalid Email");

        if(_pass.matches(_pass2)){
            if(!user.isValidPassword(_pass))
                notis("invalid Password");

        }
        else
            notis("password doesn't match");

        if (!user.isValidUsername(_user_name))
            notis("invalid user name");

        else{
            Task<DataSnapshot> caregiverDB = db.fetchCaregiverDB();

            Tasks.whenAll(caregiverDB).addOnCompleteListener(task -> {
                if(caregiverDB.getResult().child(_user_name).exists()){
                    notis("User name is already exists, use a different user name");
                }
                else {
                    if (user.isValidName(_name) &&
                            user.isValidEmail(_email) && _pass.matches(_pass2) && user.isValidPassword(_pass) ){
                        notis("200");
                        db.registerCaregiver(_name, _user_name, _pass, null);

                    }
                }
            });
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

    private void notis(String msg){
        Toast.makeText(Signup_caregiver.this, msg, Toast.LENGTH_SHORT).show();
    }

}