package com.example.application_team3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

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
                Intent page = new Intent(Log_in.this, Sign_up2.class);
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
            db.checkLoginCaregiver(_user_name, _pass, new MyCallback() {
                @Override
                public void onCallback(Object value) {
                    if((boolean) value){
                        notis("Success");

                        Intent page1 = new Intent(Log_in.this, Caregiver_dash.class);
                        testList(_user_name);
                        //db._caregiver.getName();
                        db.getCaregiverName(_user_name, new Database.NameCallback() {
                            @Override
                            public void onNameFetched(String name) {
                                if (name != null){
                                    page1.putExtra("key", name);
                                    test_struct(_user_name);
                                    startActivity(page1);
                                }
                                else{
                                    page1.putExtra("key", _user_name);
                                    startActivity(page1);
                                }
                            }
                        });
                        //page1.putExtra("key", _user_name);
                        //startActivity(page1);
                    }
                    else
                        notis("Username or password is incorrect");

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

    private void test_struct(String pid){
        db.getCaregiverEntry(pid, new CaregiverEntryCallback() {
            @Override
            public void onEntryFetched(CaregiverEntry entry) {
                if (entry != null) {

                    System.out.println("Namn: " + entry.getName());
                    System.out.println("PID: " + entry.getPid());
                    System.out.println("Lösenord: " + entry.getPassword());
                    System.out.println("Telefonnummer: " + entry.getPhoneNo());
                } else {
                    System.out.println("Ingen vårdgivare hittades för den angivna pid.");
                }
            }
        });
    }

    private void testList(String pid){
        db.getCaregiverList(pid, new Database.ListCallback() {
            @Override
            public void onListValuesFetched(List<CaregiverEntry> CaregiverList) {
                if (CaregiverList != null){
                    for (CaregiverEntry entry : CaregiverList) {
                        System.out.println("Namn: " + entry.getName());
                        System.out.println("PID: " + entry.getPid());
                        System.out.println("Lösenord: " + entry.getPassword());
                        System.out.println("Telefonnummer: " + entry.getPhoneNo());
                    }
                }
                else {
                    System.out.println("Ingen matchande vårdgivare hittades.");
                }
            }
        });
    }


}