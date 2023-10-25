package com.example.application_team3;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RemoteViews;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Controller control;
    SharedPreferences preferences;
    Database db;
    ViewBuilder vb;
    TextView loginText, signUpText;
    EditText usernameText, passwordText;
    CheckBox rememberCheckBox;
    Button signInButton;
    Switch userTypeSwitch;
    boolean isCaregiver;

    Spinner languageSpinner;
    ArrayAdapter<CharSequence> adapterItems;

    String item, currentLanguageCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        control = new Controller();
        preferences = this.getPreferences(Context.MODE_PRIVATE);

        db = control.getDatabase();
        vb = control.getViewBuilder();

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

        languageSpinner = findViewById(R.id.language_spinner);

        adapterItems = ArrayAdapter.createFromResource(this, R.array.language_array, android.R.layout.simple_spinner_dropdown_item);
        adapterItems.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(adapterItems);

        languageSpinner.setSelection(0);
        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedLang = adapterView.getItemAtPosition(i).toString();
                String langCode = null;

                if(selectedLang.equals("Swedish")) {
                    langCode = "sv";
                }
                else if(selectedLang.equals("English")){
                    langCode = "en";
                }
                System.out.println("langCode: " + langCode + " currentLanguageCode: " + currentLanguageCode);

                if(langCode != null) {
                    set_language(langCode);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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
                vb.notis("Invalid username or password", MainActivity.this);
            }
        });
    }

    private void setupUserType(){
        if(!userTypeSwitch.isChecked()){
            signUpText.setVisibility(View.GONE);
            userTypeSwitch.setText(R.string.care_client);
        }
        else {
            signUpText.setVisibility(View.VISIBLE);
            userTypeSwitch.setText(R.string.caregiver);
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

    public void set_language(String languageCode) {

        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Resources resources = getResources();
        Configuration config = new Configuration(resources.getConfiguration());
        config.setLocale(locale);

        resources.updateConfiguration(config, resources.getDisplayMetrics());

        currentLanguageCode = languageCode;

        System.out.println("locale: " + currentLanguageCode);
        //recreate();
    }

}