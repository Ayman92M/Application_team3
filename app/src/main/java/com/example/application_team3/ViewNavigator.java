package com.example.application_team3;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
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

import androidx.annotation.RequiresApi;
import androidx.core.util.Pair;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

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
    private Notification notification = new Notification();
    String elderlyString, mealString;
    List<String> elderlyStrings = new ArrayList<>();
    List<String> mealStrings =  new ArrayList<>();
    Database db = new Database();
    UserAccountControl user = new UserAccountControl();
    final int ELDERLY_REMIND_AFTER = 1; //*45
    final int ELDERLY_DEADLINE = 3;
    final int CAREGIVER_DEADLINE = 135;

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



    //SIGN MANAGER
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
                //createNotification(_user_name);
                updateNotification(_user_name);
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
                        notis("Success");
                        db.registerCaregiver(_name, _user_name, _pass, null);
                        caregiverLogIn_process(_user_name, _pass);

                    }
                }
            });
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

    public void remove(Button bt, String _caregiver_username, String _elderly_username){

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("caregiver_username: "+ _caregiver_username + " remove "
                        + "elderly_username: "+ _elderly_username);

                db.removeElderly(_caregiver_username,_elderly_username);
                notis("Done");
                goToNextActivity(Caregiver_dash.class);
            }
        });


    }


    //ELDERLY LIST MANAGER
    public void showElderlyList(ListView listView, String caregiverUserName, String caregiverName){
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
            setupElderlyListView(listView, caregiverUserName, caregiverName);
        });
    }

    private void setupElderlyListView(ListView listView, String caregiverUserName, String caregiverName){
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
                updateNotificationCaregiver(itemParts[1], itemParts[0], textView1);
                notification.runFunctionCaregiver(context, itemParts[1], itemParts[0]);
                notification.runCopyMeal(context, itemParts[1]);

                return row;
            }
        };
        // Koppla adaptern till ListView
        listView.setAdapter(adapter);


        // Actionlistner metod för Listan
        elderlyListActionListener(listView, caregiverUserName, caregiverName);
    }

    public void elderlyListActionListener(ListView listView, String caregiverUserName, String caregiverName){
        String[] elderlyArray = elderlyStrings.toArray(new String[elderlyStrings.size()]);

        // Sätter en klickhändelse för ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Hämtar det valda elementet från listan baserat på positionen
                String selectedItem = elderlyArray[position];
                // Delar upp det valda elementet i delar med hjälp av ", " som separator.
                String[] nameParts = selectedItem.split(", ");


                goToNextActivity(CaregiverElderlyPageActivity.class, "Elderly name: "
                                + nameParts[0]+ " & Elderly username: " + nameParts[1],
                        "elderlyName", nameParts[0], "elderlyUserName", nameParts[1],
                        "caregiverName",caregiverName, "caregiverUserName", caregiverUserName);

            }
        });

    }

    public void updateNotification(String elderly_username){

        Task<DataSnapshot> mealPlanTask = db.fetchMealPlanDate(elderly_username, getToday());
        Tasks.whenAll(mealPlanTask).addOnCompleteListener(task -> {
            DataSnapshot mealsData = mealPlanTask.getResult();

            if(mealsData != null && mealsData.hasChildren()){

                for(DataSnapshot mealData : mealsData.getChildren()) {

                    MealEntry meal = mealData.getValue(MealEntry.class);
                    long current_time_ToMillis = convertStringToMillis(getCurrentTime());
                    long dateToMillis = convertStringToMillis(meal.getDate()+ " " + meal.getTime());
                    long timeUpToMillis = getTimeUp(meal.getDate()+ " " + meal.getTime(), ELDERLY_REMIND_AFTER);

                    if (meal.getElderlyNotificationID() == null && dateToMillis >= current_time_ToMillis){

                        notification.createNotificationChannel(context, meal.getMealType());
                        System.out.println("updateNotification ---> setAlarm for: " + meal.getMealType() + " -> at: " + meal.getDate()+ " " + meal.getTime());
                        notification.setAlarm(context, meal.getMealType(), elderly_username, null, meal.getDate(), dateToMillis, timeUpToMillis);
                        meal.setElderlyNotificationID(meal.getDate()+meal.getTime());
                        db.updateMeal(elderly_username, meal);
                    }
                }
            }

        });

    }
    public void copyMealElderly(String elderly_username){

        Task<DataSnapshot> mealPlanTask = db.fetchMealPlanDate(elderly_username, getToday());
        Tasks.whenAll(mealPlanTask).addOnCompleteListener(task -> {
            DataSnapshot mealsData = mealPlanTask.getResult();

            if(mealsData != null && mealsData.hasChildren()){

                for(DataSnapshot mealData : mealsData.getChildren()) {

                    MealEntry meal = mealData.getValue(MealEntry.class);
                    String mealNextDate = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        mealNextDate = getNextWeekDate(meal.getDate());
                    }
                    if (mealNextDate != null)
                        db.registerMeal(elderly_username, mealNextDate, meal.getMealType(),meal.getTime(), meal.getNote());

                }
            }

        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getNextWeekDate(String inputDate) {
        // Skapa en DateTimeFormatter för att konvertera strängen till LocalDate
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(inputDate, formatter);

        LocalDate nextWeekDate = date.plusWeeks(1);


        return nextWeekDate.format(formatter);
    }
    private long getTimeUp(String date, int minutesToAdd){
        String timeUp = addMinutesToDateString(date, minutesToAdd);

        long dateToMillisMiss = 0;
        dateToMillisMiss = convertStringToMillis(timeUp);
        return dateToMillisMiss;
    }
    public void updateNotificationCaregiver(String elderly_username, String elderly_name, TextView textView){

        String current_time = getCurrentTime();
        long current_time_ToMillis = convertStringToMillis(current_time);

        Task<List<MealEntry>> mealListTask = db.MealPlanList(elderly_username);
        Tasks.whenAll(mealListTask).addOnCompleteListener(task ->
        { List<MealEntry> mealList = mealListTask.getResult();

            if(mealList != null ){
                for (MealEntry meal : mealList){
                    long dateToMillisMiss = getTimeUp(meal.getDate()+ " "+ meal.getTime(), CAREGIVER_DEADLINE);

                    if(!meal.isHasEaten() && dateToMillisMiss <= current_time_ToMillis && meal.getCaregiverNotificationID() == null){
                        if (textView != null )  textView.setError("miss");
                        notification.createNotificationChannel(context, meal.getMealType());
                        notification.setAlarm(context, meal.getMealType(), elderly_username, elderly_name, meal.getDate(), current_time_ToMillis, current_time_ToMillis);
                        System.out.println("SET Notification for Caregiver: " + meal.getMealType() + " -> at: " + meal.getDate()+ " " + meal.getTime());
                        meal.setCaregiverNotificationID(meal.getDate()+meal.getTime());
                        db.updateMeal(elderly_username, meal);

                    }
                }
            }


        });

    }


    //MEAL LIST MANAGER
    public void showMealList(ListView listView, int layoutResourceId, boolean elderlyView, String elderlyUserName, String elderlyName, String date){
        if(elderlyView)
            notification.runFunctionElderly(context, elderlyUserName, null);

        Task<DataSnapshot> mealPlanTask = db.fetchMealPlanDate(elderlyUserName, date);
        // Hämta resultatet
        Tasks.whenAll(mealPlanTask).addOnCompleteListener(task -> {
            DataSnapshot mealsData = mealPlanTask.getResult();
            mealStrings.clear();
            if(mealsData != null && mealsData.hasChildren()){

                for(DataSnapshot mealData : mealsData.getChildren()) {
                    MealEntry meal = mealData.getValue(MealEntry.class);
                    mealString = meal.getMealType() +", " + meal.getTime()+", "
                            + meal.getNote() +", "+ meal.isHasEaten() + ", "+  date;
                    mealStrings.add(mealString);

                }
            }
            sortMealByTime(mealStrings);
            setupMealListView(listView, layoutResourceId,  elderlyView, elderlyUserName, elderlyName);

        });
    }

    private void sortMealByTime(List<String> mealStrings) {
        Collections.sort(mealStrings, new Comparator<String>() {
            @Override
            public int compare(String meal1, String meal2) {
                // Antag att tiden är i formatet "HH:mm"
                String[] parts1 = meal1.split(", ")[1].split(":");
                String[] parts2 = meal2.split(", ")[1].split(":");

                int hour1 = Integer.parseInt(parts1[0]);
                int minute1 = Integer.parseInt(parts1[1]);

                int hour2 = Integer.parseInt(parts2[0]);
                int minute2 = Integer.parseInt(parts2[1]);

                if (hour1 == hour2) {
                    return Integer.compare(minute1, minute2);
                } else {
                    return Integer.compare(hour1, hour2);
                }
            }
        });
    }

    private void setupMealListView(ListView listView, int layoutResourceId, boolean elderlyView, String elderlyUserName, String elderlyName){

        // Skapa en adapter för att koppla data till ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                context.getApplicationContext(), // Använd den aktuella kontexten -- context.getApplicationContext(),
                layoutResourceId,
                R.id.meal,
                mealStrings
        ) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View row = super.getView(position, convertView, parent);
                String[] itemParts = getItem(position).split(", ");
                TextView textView1 = row.findViewById(R.id.meal);
                TextView textView2 = row.findViewById(R.id.time);
                TextView txt_bakground = row.findViewById(R.id.TextView_background);

                // Sätt texten för användarnamn och caregiverUserName
                textView1.setText(itemParts[0]); // Huvudtext (item)
                textView2.setText(itemParts[1]); // Undertext (subitem)
                mealListChangeColor(txt_bakground, position);

                if("false".equals(itemParts[3]) && isTimeUp(itemParts, 135)){
                    textView1.setError("miss");
                }

                return row;
            }
        };
        // Koppla adaptern till ListView
        listView.setAdapter(adapter);

        elderlyMealListActionListener(listView,layoutResourceId, elderlyView,  elderlyUserName, elderlyName);
    }

    private void mealListChangeColor(TextView txt_bakground, int position){

        int[] rowColors = {
                Color.rgb(121, 165, 123),   // Grön
                Color.rgb(255, 193, 7), // Gul
                Color.rgb(164, 120, 241) , // Lila
                Color.rgb(33, 150, 243)   // Blå
        };

        //txt_bakground.setBackgroundColor(rowColors[position % rowColors.length]);
        GradientDrawable gradientDrawable = (GradientDrawable) context.getResources().getDrawable(R.drawable.rounded_corner);
        //GradientDrawable gradientDrawable = (GradientDrawable) getResources().getDrawable(R.drawable.rounded_corner);
        gradientDrawable.setColor(rowColors[position % rowColors.length]);
        txt_bakground.setBackground(gradientDrawable);
    }

    public void elderlyMealListActionListener(ListView listView, int layoutResourceId, boolean elderlyView,  String elderlyUserName, String elderlyName){
        String[] mealArray = mealStrings.toArray(new String[mealStrings.size()]);

        // Sätter en klickhändelse för ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Hämtar det valda elementet från listan baserat på positionen
                String selectedItem = mealArray[position];
                // Delar upp det valda elementet i delar med hjälp av ", " som separator.
                String[] nameParts = selectedItem.split(", ");


                if(elderlyView)
                    mealInfo_elderly(listView, layoutResourceId, elderlyView, view, nameParts, elderlyUserName, elderlyName);
                else
                    mealInfo_caregiver(view, nameParts, elderlyUserName, elderlyName);


            }
        });

    }

    private void mealInfo_elderly(ListView listView, int layoutResourceId, boolean elderlyView, View view, String[] nameParts, String elderlyUserName, String elderlyName){
        Context context = view.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_meal_info_elderly, null);

        // Create a PopupWindow
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // let taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // Set a click listener for the close button in the popup
        Button closePopupBtn = popupView.findViewById(R.id.closePopupBtn);
        closePopupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {   popupWindow.dismiss();}
        });

        TextView meal_type = popupView.findViewById(R.id.meal_info);
        meal_type.setText("     " + nameParts[0]);

        TextView note = popupView.findViewById(R.id.textView7);
        note.setText(" " + nameParts[2]);

        // Show the popup at the center of the screen
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);


        Button bt_sant = popupView.findViewById(R.id.bt_sant);
        Button bt_falsk = popupView.findViewById(R.id.bt_falsk);

        if(!isItToday(nameParts[4])){
            bt_sant.setEnabled(false);
            bt_falsk.setEnabled(false);
        }


        bt_sant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ////////////////
                db.hasEatenMeal(elderlyUserName, nameParts[4], nameParts[0]);
                showMealList(listView, layoutResourceId, elderlyView, elderlyUserName, elderlyName, nameParts[4]);
                notification.cancelAlarm(context, nameParts[0],nameParts[4]);
            }
        });

        bt_falsk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {    popupWindow.dismiss();}
        });

    }


    private void mealInfo_caregiver(View view, String[] mealParts, String elderlyUserName, String elderlyName){
        Context context = view.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_meal_info_caregiver, null);

        // Create a PopupWindow
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // let taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // Set a click listener for the close button in the popup
        Button closePopupBtn = popupView.findViewById(R.id.closePopupBtn);
        closePopupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the popup
                popupWindow.dismiss();
            }
        });

        TextView meal_type = popupView.findViewById(R.id.meal_info);
        meal_type.setText("     " + mealParts[0]);

        TextView note = popupView.findViewById(R.id.textView7);
        note.setText(" " + mealParts[2]);


        // Show the popup at the center of the screen
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);


        Button bt_delete_meal = popupView.findViewById(R.id.deleteMeal);
        bt_delete_meal.setEnabled(isTimeUp(mealParts, -720) ? false : true);

        bt_delete_meal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ////////////////
                notis("TODO");

            }
        });

    }

    public int calculateDateDifferenceInMinutes(long currentTime, long timeUp) {
        long timeDifference = timeUp - currentTime;
        int differenceInMinutes = (int) TimeUnit.MILLISECONDS.toMinutes(timeDifference);
        return differenceInMinutes;
    }
    private boolean isTimeUp(String[] itemParts, int minutesToAdd){
        String current_time = getCurrentTime();
        long current_time_ToMillis = convertStringToMillis(current_time);

        String date_timeMeal = itemParts[4] + " " + itemParts[1];
        String date_timeUp = addMinutesToDateString(date_timeMeal, minutesToAdd);
        long date_timeUpToMillis = convertStringToMillis(date_timeUp);

        return date_timeUpToMillis <= current_time_ToMillis;
    }

    public long convertStringToMillis(String dateString) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            Date date = dateFormat.parse(dateString);
            if (date != null) {
                return date.getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
            // Handle the ParseException if needed
        }

        // Return a default value or handle the case where the conversion fails
        return -1;
    }


    public String addMinutesToDateString(String inputDate, int minutesToAdd) {
        // Define the date format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        try {
            // Parse the input date string
            Date date = dateFormat.parse(inputDate);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MINUTE, minutesToAdd);
            //System.out.println(inputDate +" + " + minutesToAdd + " --> " + dateFormat.format(calendar.getTime()) );
            return dateFormat.format(calendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace(); // Handle the exception according to your needs
            return null;
        }
    }
    public String getCurrentTime(){
        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // Months are 0-based, so we add 1
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Format the date and time into "yyyy-MM-dd HH:mm" format
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        return sdf.format(calendar.getTime());
    }

    public boolean isItToday(String date){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        return date.equals(sdf.format(calendar.getTime()));
    }

    public String getToday() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    }

    //SharedPreferences
    public void saveInputToPreferences(String username, String password, boolean rememberMe) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.putBoolean("rememberMe", rememberMe);
        editor.apply();

        //if (checkBoxRememberMe.isChecked())
    }

    public void saveInputToPreferencesElderlySchedular(String username) {
        SharedPreferences preferences = context.getSharedPreferences("elderly_scheduler", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("_username", username);
        notis(username);
        editor.apply();

    }

    public void saveInputToPreferencesCaregiverDashboard(String username ,String name) {
        SharedPreferences preferences = context.getSharedPreferences("CaregiverDashboard", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("_username", username);
        editor.putString("_name", name);

        editor.apply();

    }

    public Pair<String, String> getPreferencesCaregiverDashboard() {
        SharedPreferences preferences = context.getSharedPreferences("CaregiverDashboard", Context.MODE_PRIVATE);
        String username = preferences.getString("_username", "");
        String name = preferences.getString("_name", "");

        return new Pair<>(username, name);
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

    public String getUsernameFromPreferences() {
        SharedPreferences preferences = context.getSharedPreferences("elderly_scheduler", Context.MODE_PRIVATE);
        return preferences.getString("_username", "");
    }

    public void saveInputToPreferencesUserName(String username) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("username", username);

        editor.apply();

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


        /*
    public void updateNotification(String elderly_username){

        String current_time = getCurrentTime();
        long current_time_ToMillis = convertStringToMillis(current_time);

        Task<List<MealEntry>> mealListTask = db.MealPlanList(elderly_username);
        Tasks.whenAll(mealListTask).addOnCompleteListener(task ->
        { List<MealEntry> mealList = mealListTask.getResult();
            System.out.println(" -- updateNotification --");
            if(mealList != null )
                for (MealEntry meal : mealList){
                    if (meal.getElderlyNotificationID() == null){

                        long dateToMillis = convertStringToMillis(meal.getDate()+ " " + meal.getTime());

                        if(dateToMillis >= current_time_ToMillis){
                            notification.createNotificationChannel(context, meal.getMealType());

                            long timeUpToMillis = getTimeUp(meal.getDate()+ " " + meal.getTime(), ELDERLY_REMIND_AFTER);

                            notification.setAlarm(context, meal.getMealType(), elderly_username, null, meal.getDate(), dateToMillis, timeUpToMillis);
                            System.out.println("updateNotification ---> setAlarm for: " + meal.getMealType() + " -> at: " + meal.getDate()+ " " + meal.getTime());
                            meal.setElderlyNotificationID(meal.getDate()+meal.getTime());
                            db.updateMeal(elderly_username, meal);

                        }
                    }
                }
        });

    }

        public void createNotification(String elderly_username){

        String current_time = getCurrentTime();
        long current_time_ToMillis = convertStringToMillis(current_time);

        Task<List<MealEntry>> mealListTask = db.MealPlanList(elderly_username);
        Tasks.whenAll(mealListTask).addOnCompleteListener(task ->
        { List<MealEntry> mealList = mealListTask.getResult();

            if(mealList != null )
                for (MealEntry meal : mealList){

                    long dateToMillis = convertStringToMillis(meal.getDate()+ " " + meal.getTime());
                    long timeUpToMillis = getTimeUp(meal.getDate()+ " " + meal.getTime(), ELDERLY_DEADLINE);

                    if(dateToMillis >= current_time_ToMillis){
                        notification.createNotificationChannel(context, meal.getMealType());
                        notification.setAlarm(context, meal.getMealType(), elderly_username, null, meal.getDate(), dateToMillis, timeUpToMillis);
                        System.out.println("createNotification ---> setAlarm for: " + meal.getMealType() + " -> at: " + meal.getDate()+ " " + meal.getTime());
                        meal.setElderlyNotificationID(meal.getDate()+meal.getTime());
                        db.updateMeal(elderly_username, meal);

                    }

                }

        });

    }

    * */

    /*
     public void createReminder(String elderly_username, String MealType, String MealDate){


        for (int i = 1; i < 4; i++) {

            String reminderTime = addMinutesToDateString(getCurrentTime(), 1*i);
            long reminderTimeToMillis = convertStringToMillis(reminderTime);

            notification.createNotificationChannel(context, MealType);
            notification.setAlarm(context, MealType, elderly_username, null, MealDate, reminderTimeToMillis, 0L);
            System.out.println("updateNotification ---> SET Reminder "+ i + " for : " + MealType);

        }
    }

    public void crateReminder(String elderly_username, String MealType, String mealDate, long triggerTimeInMillis, long timeUpToMillis){
        notification.createNotificationChannel(context, MealType);
        String currentTime = getCurrentTime();

        String reminderTime = addMinutesToDateString(currentTime, ELDERLY_REMIND_AFTER);
        long reminderTimeToMillis = convertStringToMillis(reminderTime);

        int reminderNum =calculateDateDifferenceInMinutes(reminderTimeToMillis, timeUpToMillis);
        reminderNum = Math.abs(reminderNum -2) +1;

        if (reminderNum <= 3){
           System.out.println("reminder num: " + reminderNum + " at: " + reminderTime);
           notification.setAlarm(context, MealType , elderly_username, null, mealDate, reminderTimeToMillis, timeUpToMillis);
        }

    }
    */


    /*
     public void createNotificationCaregiver(String elderly_username, String elderly_name){

        String current_time = getCurrentTime();
        long current_time_ToMillis = convertStringToMillis(current_time);
§
        Task<List<MealEntry>> mealListTask = db.MealPlanList(elderly_username);
        Tasks.whenAll(mealListTask).addOnCompleteListener(task ->
        { List<MealEntry> mealList = mealListTask.getResult();

            if(mealList != null ){
                for (MealEntry meal : mealList){
                    if(!meal.isHasEaten()){
                        String missTime = addMinutesToTime(meal.getTime(), 135);
                        long dateToMillisMiss = convertStringToMillis(meal.getDate() + " " + missTime);
                        if(dateToMillisMiss <= current_time_ToMillis){
                            notification.createNotificationChannel(context, meal.getMealType());
                            notification.setAlarm(context, meal.getMealType(), elderly_username, elderly_name, current_time_ToMillis);
                            notis(meal.getMealType() + " alarm at: "+ meal.getDate() + " " + meal.getTime());
                        }
                    }
                }
            }


        });

    }
     */
}

