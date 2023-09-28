package com.example.application_team3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

public class Login_elderly extends AppCompatActivity {
    Database db = new Database();
    UserAccountControl user = new UserAccountControl();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_elderly);
        logIn_button();

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

        String _user_name = getEditTextValue(R.id.editTextText);
        String _pass = getEditTextValue(R.id.editTextNumberPassword);

        if (!user.isValidUsername(_user_name) || !user.isValidPin(_pass))
            Toast.makeText(Login_elderly.this, "invalid user name or Pin", Toast.LENGTH_SHORT).show();

        Task<Boolean> loginCheck = db.checkLoginElderly(_user_name, _pass);

        Tasks.whenAll(loginCheck).addOnCompleteListener(task -> {
            if(loginCheck.getResult())
            {
                Toast.makeText(Login_elderly.this, "Trueeeeeeeee", Toast.LENGTH_SHORT).show();
                Intent page1 = new Intent(Login_elderly.this, Elderly_home_test.class);
                page1.putExtra("key", _user_name);

                startActivity(page1);
            }
            else {
                Toast.makeText(Login_elderly.this, "Falseeeeee", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void notis(String msg){
        Toast.makeText(Login_elderly.this, msg, Toast.LENGTH_SHORT).show();
    }

    private String getEditTextValue(int editTextId) {
        EditText editText = findViewById(editTextId);
        String value = editText.getText().toString();
        return value;
    }
}