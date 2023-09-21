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
    TextView signup_bt; boolean loginSuccess;
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
                String _user_name = ( (EditText) findViewById(R.id.editTextText)).getText().toString();
                String _pass =((EditText) findViewById(R.id.editTextNumberPassword)).getText().toString();
              /*  db.caregiverRef.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.child(_user_name).exists()){
                            System.out.println(snapshot.child(_user_name).getValue());
                            System.out.println("1" + loginSuccess);
                            if(Objects.equals(snapshot.child(_user_name).child("password").getValue(), _pass)){
                                loginSuccess = true;
                                System.out.println("2" +loginSuccess);
                                Toast.makeText(Log_in.this, "True", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(Log_in.this, "False", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

               */
                db.checkLoginCaregiver(_user_name, _pass);
                if(db.getLoginSuccess())
                    Toast.makeText(Log_in.this, "True", Toast.LENGTH_SHORT).show();

               else
                   Toast.makeText(Log_in.this, "False", Toast.LENGTH_SHORT).show();
            }
        });
    }

}