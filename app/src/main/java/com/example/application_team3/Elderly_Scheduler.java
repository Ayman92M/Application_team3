package com.example.application_team3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Elderly_Scheduler extends AppCompatActivity {
    ListView listView;
    String mealString;
    List<String> mealStrings =  new ArrayList<>();
    Database db = new Database();
    ViewNavigator navigator = new ViewNavigator(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elderly_scheduler);

        Intent get_info = getIntent();
        String elderly_username = get_info.getStringExtra("elderlyUserName");
        String elderly_name = get_info.getStringExtra("elderlyName");
        listView = findViewById(R.id.listView_elderly_scheduler);
        showMealList(listView, elderly_username, elderly_name, "2023-10-5");

    }

    public void showMealList(ListView listView, String elderlyUserName, String elderlyName, String date){
        // Hämta en lista av ElderlyEntry-objekt som tillhör en caregiver (som har caregiverUserName som username)
        Task<DataSnapshot> mealPlanTask = db.fetchMealPlanDate(elderlyUserName, date);
        // Hämta resultatet
        Tasks.whenAll(mealPlanTask).addOnCompleteListener(task -> {
            DataSnapshot mealsData = mealPlanTask.getResult();
            mealStrings.clear();

            for(DataSnapshot mealData : mealsData.getChildren()) {
                MealEntry meal = mealData.getValue(MealEntry.class);
                mealString = meal.getMealType() +", " + meal.getTime()+", "
                        + meal.getNote() +", "+ meal.isHasEaten();
                mealStrings.add(mealString);
            }

            for (String mealString : mealStrings)
                System.out.println(mealString);


            sortMealByTime(mealStrings);

            for (String mealString : mealStrings)
                System.out.println(mealString);

            setupMealListView(listView, elderlyUserName, elderlyName);

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

    private void setupMealListView(ListView listView, String elderlyUserName, String elderlyName){
        // Skapa en adapter för att koppla data till ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getApplicationContext(), // Använd den aktuella kontexten -- context.getApplicationContext(),
                R.layout.activity_list_item_elderlyscheduler,
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

                return row;
            }
        };
        // Koppla adaptern till ListView
        listView.setAdapter(adapter);

        elderlyMealListActionListener(listView, elderlyUserName, elderlyName);
    }

    private void mealListChangeColor(TextView txt_bakground, int position){

        int[] rowColors = {
                Color.rgb(121, 165, 123),   // Grön
                Color.rgb(255, 193, 7), // Gul
                Color.rgb(164, 120, 241) , // Lila
                Color.rgb(33, 150, 243)   // Blå
        };

        //txt_bakground.setBackgroundColor(rowColors[position % rowColors.length]);
        //GradientDrawable gradientDrawable = (GradientDrawable) context.getResources().getDrawable(R.drawable.rounded_corner);
        GradientDrawable gradientDrawable = (GradientDrawable) getResources().getDrawable(R.drawable.rounded_corner);
        gradientDrawable.setColor(rowColors[position % rowColors.length]);
        txt_bakground.setBackground(gradientDrawable);
    }

    public void elderlyMealListActionListener(ListView listView, String elderlyUserName, String elderlyName){
        String[] mealArray = mealStrings.toArray(new String[mealStrings.size()]);

        // Sätter en klickhändelse för ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Hämtar det valda elementet från listan baserat på positionen
                String selectedItem = mealArray[position];
                // Delar upp det valda elementet i delar med hjälp av ", " som separator.
                String[] nameParts = selectedItem.split(", ");


                //goToNextActivity(Meal_info.class, "----"
                //              + nameParts[0]+ " -- " + nameParts[1] +" -- " + nameParts[2] +" -- " + nameParts[3] ,
                //    "elderlyName",elderlyName, "elderlyUserName", elderlyUserName,
                //  "mealType", nameParts[0], "mealTime", nameParts[1], "mealNote", nameParts[2], "hasEaten", nameParts[3]);

                Context context = view.getContext();
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.popup_layout, null);

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
                // Show the popup at the center of the screen
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

            }
        });

    }




}