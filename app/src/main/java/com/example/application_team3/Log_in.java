package com.example.application_team3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class Log_in extends AppCompatActivity {
    UserAccountControl user = new UserAccountControl();
    TextView signup_bt;
    Database db = new Database();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        signup_bt = findViewById(R.id.textView_signup);
        signup_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent page = new Intent(Log_in.this, Sign_up2.class);
                startActivity(page);
            }
        });

        Button button_login = findViewById(R.id.button3);
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                                Toast.makeText(Log_in.this, "True", Toast.LENGTH_SHORT).show();

                                Intent page1 = new Intent(Log_in.this, Caregiver_dash.class);
                                //db._caregiver.getName();
                                page1.putExtra("key", _user_name);
                                startActivity(page1);
                            }
                            else {
                                Toast.makeText(Log_in.this, "False", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });
    }

    private String getEditTextValue(int editTextId) {
        EditText editText = findViewById(editTextId);
        String value = editText.getText().toString();
        return value;
    }

}