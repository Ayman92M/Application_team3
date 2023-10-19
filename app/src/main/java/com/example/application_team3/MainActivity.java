package com.example.application_team3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

public class MainActivity extends AppCompatActivity {

    Controller control;
    SharedPreferences preferences;
    Database db;
    ViewBuilder viewBuilder;
    TextView loginText, signUpText;
    EditText usernameText, passwordText;
    CheckBox rememberCheckBox;
    Button signInButton;
    Switch userTypeSwitch;
    boolean isCaregiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        control = new Controller();
        preferences = this.getPreferences(Context.MODE_PRIVATE);

        db = control.getDatabase();
        viewBuilder = control.getViewBuilder();

        loginText = findViewById(R.id.loginText);
        usernameText = findViewById(R.id.usernameText);
        passwordText = findViewById(R.id.passwordText);
        rememberCheckBox = findViewById(R.id.rememberCheckBox);
        signInButton = findViewById(R.id.logInButton);
        signUpText = findViewById(R.id.signUpTextButton);
        userTypeSwitch = findViewById(R.id.userTypeSwitch);

        setRememberMeValues();
        setupUserType();

        userTypeSwitch.setOnClickListener(view -> setupUserType());

        signInButton.setOnClickListener(view -> login());

        signUpText.setOnClickListener(view -> control.goToActivity(view.getContext(), SignupCaregiver.class));

    }

    private void login(){
        String username = usernameText.getText().toString();
        String password = passwordText.getText().toString();
        saveInputToPreferences(username, password, rememberCheckBox.isChecked(), userTypeSwitch.isChecked());
        Task<Boolean> loginTask;
        if(isCaregiver){
            loginTask = db.checkLoginCaregiver(username, password);
        }
        else {
            loginTask = db.checkLoginElderly(username, password);
        }

        Tasks.whenAll(loginTask).addOnCompleteListener(task -> {
            boolean loginResult = loginTask.getResult();
            if(loginResult){
                if(isCaregiver){
                    control.caregiverLogIn(username, password, MainActivity.this);
                }
                else {
                    control.logInElderly(username, password, MainActivity.this);
                }
            }
            else {
                viewBuilder.notis("Invalid username or password", MainActivity.this);
            }
        });
    }

    private void setupUserType(){
        if(!userTypeSwitch.isChecked()){
            signUpText.setVisibility(View.GONE);
            userTypeSwitch.setText("Elderly");
        }
        else {
            signUpText.setVisibility(View.VISIBLE);
            userTypeSwitch.setText("Caregiver");
        }
        isCaregiver = userTypeSwitch.isChecked();
    }

    private void setRememberMeValues() {
        preferences = this.getPreferences(Context.MODE_PRIVATE);

        String username = preferences.getString("username", "");
        usernameText.setText(username);
        isCaregiver = preferences.getBoolean("userType", false);
        userTypeSwitch.setChecked(isCaregiver);


        if (getRememberMeStatus()) {
            String password = preferences.getString("password", "");
            passwordText.setText(password);

            rememberCheckBox.setChecked(true);
        }
    }

    private boolean getRememberMeStatus() {
        // Retrieve the "Remember Me" status from SharedPreferences
        return preferences.getBoolean("rememberMe", false);
    }

    public void saveInputToPreferences(String username, String password, boolean rememberMe, boolean userType) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.putBoolean("rememberMe", rememberMe);
        editor.putBoolean("userType", userType);
        editor.apply();
    }

}