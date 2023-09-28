package com.example.application_team3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;

import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;


public class Log_in extends AppCompatActivity {
    UserAccountControl user = new UserAccountControl();
    TextView signup_bt;
    Database db = new Database();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        signUp_button();
        logIn_button();


    }

    private void signUp_button(){
        signup_bt = findViewById(R.id.textView_signup);
        signup_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent page = new Intent(Log_in.this, Signup_caregiver.class);
                startActivity(page);
            }
        });
    }

    private void logIn_button(){
        Button button_login = findViewById(R.id.button3);
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logIn_process();
            }
        });
    }

    private void logIn_process(){
        //String _user_name = ( (EditText) findViewById(R.id.editTextText)).getText().toString();
        String _user_name = getEditTextValue(R.id.editTextText);
        String _pass = getEditTextValue(R.id.editTextNumberPassword);


        if (!user.isValidUsername(_user_name) || !user.isValidPassword(_pass))
            Toast.makeText(Log_in.this, "invalid user name or password", Toast.LENGTH_SHORT).show();
        else{
            Task<Boolean> checkLogin = db.checkLoginCaregiver(_user_name, _pass);
            Task<DataSnapshot> caregiverTask = db.fetchCaregiver(_user_name);

            Tasks.whenAll(checkLogin, caregiverTask).addOnCompleteListener(task-> {
                if(checkLogin.getResult()){
                    notis("success");

                    Intent page1 = new Intent(Log_in.this, Caregiver_dash.class);

                    CaregiverEntry caregiver = caregiverTask.getResult().getValue(CaregiverEntry.class);

                    if (caregiver != null){
                        String name = caregiver.getName();
                        String _user = caregiver.getPid();
                        page1.putExtra("key", name);
                        page1.putExtra("pid", _user);
                        startActivity(page1);
                    }
                    else{
                        page1.putExtra("key", _user_name);
                        startActivity(page1);
                    }
                }
                else{
                    notis("False");
                }

            });
        }

    }

    private void notis(String msg){
        Toast.makeText(Log_in.this, msg, Toast.LENGTH_SHORT).show();
    }

    private String getEditTextValue(int editTextId) {
        EditText editText = findViewById(editTextId);
        String value = editText.getText().toString();
        return value;
    }



}