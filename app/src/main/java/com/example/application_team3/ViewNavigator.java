package com.example.application_team3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

/*
    För att kunna använda de här funktioner,
    måste man först skapa en instans av klassen ViewNavigator. så ->
    ViewNavigator navigator = new ViewNavigator(this);  //Högst up (global)

    sen kan man anropa funktioner genom att ->
    navigator.notis("msg")
    ||
    //först deklarera. Vilken typ är det? TextView eller knapp?
    TextView addElderly = findViewById(R.id.TextView_addElderly);
    navigator.goToNextActivity(addElderly, Signup_elderly.class);

    Button _caregiver = findViewById(R.id.button2);
    navigator.goToNextActivity(_caregiver, Log_in.class);
    ||
    Scanf.
    String _user_name = navigator.getEditTextValue(R.id.editTextText);
    String _pass = navigator.getEditTextValue(R.id.editTextNumberPassword);
 */
public class ViewNavigator {
    private Context context;

    String elderlyString, mealString;
    List<String> elderlyStrings = new ArrayList<>();
    List<String> mealStrings =  new ArrayList<>();
    Database db = new Database();
    UserAccountControl user = new UserAccountControl();
    private SharedPreferences preferences;

    public void notis(String msg){

        // Skapar en toast-notifiering med ett meddelande (msg)
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public String getEditTextValue(int editTextId) {
        // Hitta EditText-komponenten med hjälp av dess ID
        EditText editText = ((Activity) context).findViewById(editTextId);
        // Hämta textvärdet från EditText och konvertera det till en sträng
        String value = editText.getText().toString();
        return value;
    }

    public void setEditTextValue(int editTextId, String value) {
        EditText editText = ((Activity) context).findViewById(editTextId);
        editText.setText(value);
    }

    public void goToNextActivity(Button bt, Class<?> targetActivity) {

        // När knappen klickas på, skapar vi en ny Intent (ActionListener).
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, targetActivity);
                // Startar en ny aktivitet (targetActivity).
                context.startActivity(intent);
            }
        });
    }
    public ViewNavigator(Context context) {
        this.context = context;
    }

    public void goToNextActivity(Class<?> targetActivity) {
        Intent intent = new Intent(context, targetActivity);
        context.startActivity(intent);
    }
    public void goToNextActivity(TextView txt, Class<?> targetActivity) {
        // När textvyn klickas på, skapar vi en ny Intent-objekt (ActionListener).
        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, targetActivity);
                context.startActivity(intent);
            }
        });
    }
    public void goToNextActivity(Class<?> targetActivity, String notis, String key1, String value1, String key2, String value2) {
        // Skapa en ny Intent för att flytta till nästa aktivitet (targetActivity)
        Intent intent = new Intent(context, targetActivity);

        // Om det finns ett meddelande (notis), visa det
        if(notis != null)
            notis(notis);

        // Om det finns en key1, skicka den vidare till targetActivity.
        if(key1 != null)
            intent.putExtra(key1, value1);

        if(key2 != null)
            intent.putExtra(key2, value2);

        // Starta nästa aktivitet (targetActivity)
        context.startActivity(intent);
    }

    public void goToNextActivity(TextView txt, Class<?> targetActivity, String key1, String value1, String key2, String value2) {
        // När textvyn klickas på, skapar vi en ny Intent-objekt (ActionListener).
        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, targetActivity);

                if(key1 != null)
                    intent.putExtra(key1, value1);

                if(key2 != null)
                    intent.putExtra(key2, value2);

                // Startar en ny aktivitet (targetActivity).
                context.startActivity(intent);
            }
        });
    }

    public void goToNextActivity(Class<?> targetActivity, String notis, String key1, String value1, String key2, String value2,
                                 String key3, String value3, String key4, String value4) {
        // Skapa en ny Intent för att flytta till nästa aktivitet (targetActivity)
        Intent intent = new Intent(context, targetActivity);

        // Om det finns ett meddelande (notis), visa det
        if(notis != null)
            notis(notis);

        // Om det finns en key1, skicka den vidare till targetActivity.
        if(key1 != null)
            intent.putExtra(key1, value1);

        if(key2 != null)
            intent.putExtra(key2, value2);

        if(key3 != null)
            intent.putExtra(key3, value3);

        if(key4 != null)
            intent.putExtra(key4, value4);

        // Starta nästa aktivitet (targetActivity)
        context.startActivity(intent);
    }

    public void goToNextActivity(Class<?> targetActivity, String notis, String key1, String value1, String key2, String value2,
                                 String key3, String value3, String key4, String value4, String key5, String value5
                                    ,String key6, String value6) {
        // Skapa en ny Intent för att flytta till nästa aktivitet (targetActivity)
        Intent intent = new Intent(context, targetActivity);

        // Om det finns ett meddelande (notis), visa det
        if(notis != null)
            notis(notis);

        // Om det finns en key1, skicka den vidare till targetActivity.
        if(key1 != null)
            intent.putExtra(key1, value1);

        if(key2 != null)
            intent.putExtra(key2, value2);

        if(key3 != null)
            intent.putExtra(key3, value3);

        if(key4 != null)
            intent.putExtra(key4, value4);

        if(key5 != null)
            intent.putExtra(key5, value5);

        if(key6 != null)
            intent.putExtra(key6, value6);

        // Starta nästa aktivitet (targetActivity)
        context.startActivity(intent);
    }


    public void caregiverLogIn_process(String _user_name, String _pass){

        if (!user.isValidUsername(_user_name) || !user.isValidPassword(_pass))
            notis("invalid user name or password");

        else{
            Task<Boolean> checkLogin = db.checkLoginCaregiver(_user_name, _pass);
            Task<DataSnapshot> caregiverTask = db.fetchCaregiver(_user_name);
            Tasks.whenAll(checkLogin, caregiverTask).addOnCompleteListener(task-> {
                if(checkLogin.getResult()){

                    CaregiverEntry caregiver = caregiverTask.getResult().getValue(CaregiverEntry.class);
                    if (caregiver != null)
                        goToNextActivity(Caregiver_dash.class, "success","caregiverName",
                                caregiver.getName() ,"caregiverUserName", caregiver.getPid());

                    System.out.println("caregiverName LogIn_process: " + caregiver.getName());
                    System.out.println("caregiverUserName LogIn_process: " + caregiver.getPid());
                }
                else{
                    notis("False");
                }

            });
        }
    }

    public void signUpAnElderly(String _name, String _user_name, String _pin, String _pin2, String caregiver_name, String caregiver_pid ) {
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
                        db.registerElderly(_name, _user_name, _pin, null, caregiver_name, caregiver_pid);

                    }
                }

            });

        }
    }

    public void logInElderly(String _user_name, String _pass){
        if (!user.isValidUsername(_user_name) || !user.isValidPin(_pass))
            notis("invalid user name or Pin");

        Task<Boolean> loginCheck = db.checkLoginElderly(_user_name, _pass);
        Task<DataSnapshot> elderlyTask = db.fetchElderly(_user_name);
        Tasks.whenAll(loginCheck, elderlyTask).addOnCompleteListener(task -> {
            if(loginCheck.getResult()) {
                DataSnapshot elderly = elderlyTask.getResult();
                String elderly_name = elderly.child("name").getValue().toString();
                goToNextActivity(Elderly_Scheduler.class, "True", "elderlyUserName", _user_name, "elderlyName", elderly_name);
            }
            else
                notis("False");

        });

    }

    public void signUpCaregiver(String _name, String _user_name, String _email, String _pass, String _pass2){

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



    public void showList(ListView listView, String caregiverUserName, String caregiverName){
        // Hämta en lista av ElderlyEntry-objekt som tillhör en caregiver (som har caregiverUserName som username)
        Task<List<ElderlyEntry>> elderlyListTask = db.ElderlyList(caregiverUserName);
        // Lyssna på när uppgiften (Task) är klar
        Tasks.whenAll(elderlyListTask).addOnCompleteListener(task -> {
            // Hämta resultatet
            List<ElderlyEntry> elderlyList = elderlyListTask.getResult();

            // Rensa den befintliga listan (elderlyStrings). Börja om
            elderlyStrings.clear();

            // Loopa genom ElderlyEntry-objekten och skapa strängar som innehåller namn och caregiverUserName
            for(ElderlyEntry elderly : elderlyList){
                elderlyString = elderly.getName() +", " + elderly.getPid();
                elderlyStrings.add(elderlyString);
            }
            // Skapa en adapter för att koppla data till ListView
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    context.getApplicationContext(), // Använd den aktuella kontexten
                    R.layout.activity_list_item,
                    R.id.textView_username,
                    elderlyStrings
            ) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View row = super.getView(position, convertView, parent);
                    String[] itemParts = getItem(position).split(", ");
                    TextView textView1 = row.findViewById(R.id.textView_username);
                    TextView textView2 = row.findViewById(R.id.textView_list_pid);

                    // Sätt texten för användarnamn och caregiverUserName
                    textView1.setText(itemParts[0]); // Huvudtext (item)
                    textView2.setText(itemParts[1]); // Undertext (subitem)

                    return row;
                }
            };
            // Koppla adaptern till ListView
            listView.setAdapter(adapter);


            // Actionlistner metod för Listan
            elderlyOverview(listView, caregiverUserName, caregiverName);
        });
    }

    public void elderlyOverview(ListView listView, String caregiverUserName, String caregiverName){
        String[] elderlyArray = elderlyStrings.toArray(new String[elderlyStrings.size()]);

        // Sätter en klickhändelse för ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Hämtar det valda elementet från listan baserat på positionen
                String selectedItem = elderlyArray[position];
                // Delar upp det valda elementet i delar med hjälp av ", " som separator.
                String[] nameParts = selectedItem.split(", ");


                goToNextActivity(CargiverElderlyPageActivity.class, "Elderly username skickas till databasen för att få Elderly overview"
                                + nameParts[0]+ " " + nameParts[1],
                        "elderlyName", nameParts[0], "elderlyUserName", nameParts[1],
                        "caregiverName",caregiverName, "caregiverUserName", caregiverUserName);

            }
        });

    }

    public void saveInputToPreferences(String username, String password, boolean rememberMe) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.putBoolean("rememberMe", rememberMe);
        editor.apply();

        //if (checkBoxRememberMe.isChecked())
    }

    public void setRememberMeValues(Activity activity, int editTextUsernameId, int editTextPasswordId, CheckBox checkBoxRemember) {
        preferences = activity.getPreferences(Context.MODE_PRIVATE);

        String username = preferences.getString("username", "");
        if (username != null)
            setEditTextValue(editTextUsernameId, username);

        if (getRememberMeStatus()) {
            String password = preferences.getString("password", "");
            setEditTextValue(editTextPasswordId, password);

            checkBoxRemember.setChecked(true);
        }
    }

    private void saveRememberMeStatus(boolean status) {
        // Save the "Remember Me" status in SharedPreferences
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("rememberMe", status);
        editor.apply();
    }

    public boolean getRememberMeStatus() {
        // Retrieve the "Remember Me" status from SharedPreferences
        return preferences.getBoolean("rememberMe", false);
    }

    public void getUsernameFromPreferences(SharedPreferences preferences, int editTextId) {
        if (preferences.contains("username")) {
            String username = preferences.getString("username", "");
            setEditTextValue(editTextId, username);
        }
    }

    public void autoLogIn(Activity activity, CheckBox checkBoxRemember) {
        preferences = activity.getPreferences(Context.MODE_PRIVATE);
        if (preferences.getBoolean("rememberMe", false)) {
            String username = preferences.getString("username", "");
            String password = preferences.getString("password", "");
            System.out.println("- Username: " + username + ", Password: " + password);
            if (!username.isEmpty() && !password.isEmpty())
                caregiverLogIn_process(username, password);

            checkBoxRemember.setChecked(true);
        }
    }

    public void setRememberMeValuesWithOutCheckBox(Activity activity, int editTextUsernameId,
                                                   int editTextPasswordId, Boolean shouldRememberMe) {
        preferences = activity.getPreferences(Context.MODE_PRIVATE);
        if (shouldRememberMe) {
            String username = preferences.getString("username", "");
            String password = preferences.getString("password", "");

            setEditTextValue(editTextUsernameId, username);
            setEditTextValue(editTextPasswordId, password);
            /*
            if (!savedUserName.isEmpty() && !savedPassword.isEmpty()) {
                navigator.caregiverLogIn_process(savedUserName, savedPassword);
            }
             */
        }
    }


    public void logout(Activity activity, Class<?> targetActivity) {
        // Clear saved credentials
        preferences = activity.getPreferences(Context.MODE_PRIVATE);

        // Log the current values before removal
        String usernameBefore = preferences.getString("username", "");
        String passwordBefore = preferences.getString("password", "");
        System.out.println("Before Logout - Username: " + usernameBefore + ", Password: " + passwordBefore);


        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("rememberMe");
        editor.remove("username");
        editor.remove("password");
        editor.apply();

        // Log the values after removal
        String usernameAfter = preferences.getString("username", "");
        String passwordAfter = preferences.getString("password", "");
        System.out.println("after Logout - Username: " + usernameBefore + ", Password: " + passwordBefore);

        saveRememberMeStatus(false);
        goToNextActivity(targetActivity, "logout successful", "logout", "true", null, null);
    }
}

