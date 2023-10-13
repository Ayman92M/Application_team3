package com.example.application_team3;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ViewBuilder implements Serializable {

    List<String> elderlyStrings = new ArrayList<>();
    public ViewBuilder() {
    }

    public void notis(String msg, Context context){

        // Skapar en toast-notifiering med ett meddelande (msg)
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public PopupWindow buildPopup(View _popupView){
        System.out.println("popup start");
        // Create a PopupWindow
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // let taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(_popupView, width, height, focusable);
        System.out.println("after block 1");
        // Set a click listener for the close button in the popup
        Button closePopupBtn = _popupView.findViewById(R.id.Button_cancel);
        closePopupBtn.setOnClickListener(v -> {
            // Dismiss the popup
            popupWindow.dismiss();
        });
        System.out.println("after listener");
        return popupWindow;
    }

    //ELDERLY LIST MANAGER
    public List<String> setupElderlyListView(CaregiverEntry user, ListView listView, Context context){
        elderlyStrings.clear();
        HashMap<String, String> elderlyList = user.getElderly();
        elderlyList.forEach((username, name) ->{
            String elderlyString = name + ", " + username;
            elderlyStrings.add(elderlyString);
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                context.getApplicationContext(), // Använd den aktuella kontexten
                R.layout.activity_list_item,
                R.id.textView_username,
                elderlyStrings
        ) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View row = super.getView(position, convertView, parent);
                String[] itemParts = Objects.requireNonNull(getItem(position)).split(", ");
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
        return elderlyStrings;
    }

    public void setupMealListView(Context context, List<MealEntry> mealList, ListView listView, int layoutResourceId){

        // Skapa en adapter för att koppla data till ListView
        ArrayAdapter<MealEntry> adapter = new ArrayAdapter<MealEntry>(
                context.getApplicationContext(), // Använd den aktuella kontexten -- context.getApplicationContext(),
                layoutResourceId,
                R.id.meal,
                mealList
        ) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {

                View row = super.getView(position, convertView, parent);
                MealEntry meal = getItem(position);
                TextView textView1 = row.findViewById(R.id.meal);
                TextView textView2 = row.findViewById(R.id.time);
                notis(meal.getMealType(), context);

                // Sätt texten för användarnamn och caregiverUserName
                textView1.setText(meal.getMealType()); // Huvudtext (item)
                textView2.setText(meal.getTime()); // Undertext (subitem)
                textView1.setTextColor(Color.rgb(0, 0, 0));
                textView2.setTextColor(Color.rgb(0, 0, 0));

                return row;
            }
        };
        listView.setAdapter(adapter);
    }
    private void mealListChangeColor(Context context, TextView txt_bakground, int position){

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

}
