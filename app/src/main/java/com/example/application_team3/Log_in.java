package com.example.application_team3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

public class Log_in extends AppCompatActivity {
    UserAccountControl user = new UserAccountControl();
    TextView signup_bt;
    Database db = new Database();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        signup_bt = findViewById(R.id.textView_signup);
        signup_bt.setOnClickListener(view -> {
            Intent page = new Intent(Log_in.this, Sign_up2.class);
            startActivity(page);
        });

        Button button_login = findViewById(R.id.button3);
        button_login.setOnClickListener(view -> {
            String _user_name = ( (EditText) findViewById(R.id.editTextText)).getText().toString();
            String _pass =((EditText) findViewById(R.id.editTextNumberPassword)).getText().toString();

            Task<Boolean> loginCheck = db.checkLoginElderly(_user_name, _pass);

            if (!user.isValidUsername(_user_name) || !user.isValidPassword(_pass))
                Toast.makeText(Log_in.this, "invalid user name or password", Toast.LENGTH_SHORT).show();
            else{
                Tasks.whenAll(loginCheck).addOnCompleteListener(task -> {
                    if(loginCheck.getResult()){
                        Toast.makeText(Log_in.this, "True", Toast.LENGTH_SHORT).show();

                        Intent page1 = new Intent(Log_in.this, Caregiver_dash.class);
                        //db._caregiver.getName();
                        page1.putExtra("key", _user_name);
                        startActivity(page1);
                    }
                    else {
                        Toast.makeText(Log_in.this, "False", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });
    }

    /*
    void test(String _user_name, String _pass){
        db.caregiverRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(_user_name).exists()){
                    System.out.println(snapshot.child(_user_name).getValue());

                    if(Objects.equals(snapshot.child(_user_name).child("password").getValue(), _pass)){
                        Toast.makeText(Log_in.this, "True", Toast.LENGTH_SHORT).show();

                        Intent page1 = new Intent(Log_in.this, Caregiver_dash.class);
                        String _name1 =  snapshot.child(_user_name).child("name").getValue().toString();
                        page1.putExtra("key", _name1);
                        startActivity(page1);
                    }
                    else{
                        Toast.makeText(Log_in.this, "False password", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(Log_in.this, "False username", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

     */

}