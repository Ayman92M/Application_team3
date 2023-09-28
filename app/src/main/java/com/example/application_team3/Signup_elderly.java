package com.example.application_team3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

public class Signup_elderly extends AppCompatActivity {
    Database db = new Database();
    UserAccountControl user = new UserAccountControl();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_elderly);
        Button button_sigup_elderly = findViewById(R.id.button11);
        button_sigup_elderly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp_process();
            }
        });
    }


    private void signUp_process(){
        String _name = getEditTextValue(R.id.editTextText2);


        String _pin = getEditTextValue(R.id.editTextText10);
        String _pin2 = getEditTextValue(R.id.editTextText11);
        String _user_name = getEditTextValue(R.id.editTextText9);

        if (!user.isValidName(_name))
            notis("invalid name");



        if(_pin.matches(_pin2)){
            if(!user.isValidPin(_pin))
                notis("invalid Pin");

        }
        else
            notis("Pin doesn't match");

        if (!user.isValidUsername(_user_name))
            notis("invalid user name");

        else{
            Task<DataSnapshot> elderlyDB = db.fetchElderlyDB();

            Tasks.whenAll(elderlyDB).addOnCompleteListener(task -> {
                DataSnapshot snapshot = elderlyDB.getResult();
                if(snapshot.child(_user_name).exists()){
                    notis("User name is already exists, use a different user name");
                }
                else {
                    if (user.isValidName(_name) &&
                            _pin.matches(_pin2) && user.isValidPin(_pin) ){
                        notis("200");
                        db.registerElderly(_name, _user_name, _pin, null, null);

                    }
                }

            });

        }


    }


    private String getEditTextValue(int editTextId) {
        EditText editText = findViewById(editTextId);
        String value = editText.getText().toString();
        return value;
    }

    private void notis(String msg){
        Toast.makeText(Signup_elderly.this, msg, Toast.LENGTH_SHORT).show();
    }

}
