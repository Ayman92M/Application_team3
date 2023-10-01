package com.example.application_team3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
    Database db = new Database();
    UserAccountControl user = new UserAccountControl();

    public ViewNavigator(Context context) {
        this.context = context;
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
    public void goToNextActivity(Class<?> targetActivity) {
        Intent intent = new Intent(context, targetActivity);
        context.startActivity(intent);
    }

    String elderlyString;
    List<String> elderlyStrings = new ArrayList<>();
    public void showList(ListView listView, String pid){
        // Hämta en lista av ElderlyEntry-objekt som tillhör en caregiver (som har pid som username)
        Task<List<ElderlyEntry>> elderlyListTask = db.ElderlyList(pid);
        // Lyssna på när uppgiften (Task) är klar
        Tasks.whenAll(elderlyListTask).addOnCompleteListener(task -> {
            // Hämta resultatet
            List<ElderlyEntry> elderlyList = elderlyListTask.getResult();

            // Rensa den befintliga listan (elderlyStrings). Börja om
            elderlyStrings.clear();

            // Loopa genom ElderlyEntry-objekten och skapa strängar som innehåller namn och pid
            for(ElderlyEntry elderly : elderlyList){
                elderlyString = elderly.getName() +", " + elderly.getPid();
                elderlyStrings.add(elderlyString);
            }
            // Skapa en adapter för att koppla data till ListView
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    context.getApplicationContext(), // Använd den aktuella kontexten
                    R.layout.activity_list_item,
                    R.id.textView_list_username,
                    elderlyStrings
            ) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View row = super.getView(position, convertView, parent);
                    String[] itemParts = getItem(position).split(", ");
                    TextView textView1 = row.findViewById(R.id.textView_list_username);
                    TextView textView2 = row.findViewById(R.id.textView_list_pid);

                    // Sätt texten för användarnamn och pid
                    textView1.setText(itemParts[0]); // Huvudtext (item)
                    textView2.setText(itemParts[1]); // Undertext (subitem)

                    return row;
                }
            };
            // Koppla adaptern till ListView
            listView.setAdapter(adapter);

            // Actionlistner metod för Listan
            elderlyOverview(listView);
        });
    }

    public void elderlyOverview(ListView listView){
        String[] elderlyArray = elderlyStrings.toArray(new String[elderlyStrings.size()]);

        // Sätter en klickhändelse för ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Hämtar det valda elementet från listan baserat på positionen
                String selectedItem = elderlyArray[position];
                // Delar upp det valda elementet i delar med hjälp av ", " som separator.
                String[] nameParts = selectedItem.split(", ");

                // Anropar metoden goToNextActivity för att gå till Elderly_home_test-aktiviteten
                // och skickar med vissa data som extras via en Intent. name och username så vi kan skicka de vidare till databasen.
                goToNextActivity(Elderly_home_test.class, "Elderly username skickas till databasen för att få Elderly overview" + nameParts[0]+ " " + nameParts[1],
                        "name", nameParts[0], "username", nameParts[1]);

            }
        });
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
                        goToNextActivity(Caregiver_dash.class, "success","name",
                                caregiver.getName() ,"pid", caregiver.getPid());
                }
                else{
                    notis("False");
                }

            });
        }
    }

    public void signUpAnElderly(String _name, String _user_name, String _pin, String _pin2, String caregiver_name, String caregiver_pid ){
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
        Tasks.whenAll(loginCheck).addOnCompleteListener(task -> {
            if(loginCheck.getResult())
                goToNextActivity(Elderly_home_test.class, "True","name", _user_name ,null, null);

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

    public void notis(String msg){

        // Skapar en toast-notifiering med ett meddelande (msg)
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    //// Metod för att hämta textvärdet från en EditText-komponent
    public String getEditTextValue(int editTextId) {
        // Hitta EditText-komponenten med hjälp av dess ID
        EditText editText = ((Activity) context).findViewById(editTextId);
        // Hämta textvärdet från EditText och konvertera det till en sträng
        String value = editText.getText().toString();
        return value;
    }


}

