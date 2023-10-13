package com.example.application_team3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.android.material.bottomappbar.BottomAppBar;

public class CaregiverElderlyPageActivity extends AppCompatActivity {
    Intent get_info;
    ViewNavigator navigator = new ViewNavigator(this);
    Database db = new Database();

    BottomAppBar bottomAppBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cargiver_elderly_page);
        bottomAppBar = findViewById(R.id.bottomAppBar);

        get_info = getIntent();
        String elderly_name = get_info.getStringExtra("elderlyName");
        String elderly_username = get_info.getStringExtra("elderlyUserName");
        String caregiver_username = get_info.getStringExtra("caregiverUserName");
        String caregiver_name = get_info.getStringExtra("caregiverName");

        navigator.createNotificationCaregiver(elderly_username, elderly_name);

        bottomAppBar.setOnMenuItemClickListener(item -> {
            if (item.getItemId()==R.id.bottomNav_back){
                Intent intent = new Intent(CaregiverElderlyPageActivity.this, Caregiver_dash.class);
                intent.putExtra("elderlyUserName", elderly_username);
                intent.putExtra("elderlyName", elderly_name);
                intent.putExtra("caregiverUserName", caregiver_username);
                intent.putExtra("caregiverName", caregiver_name);
                startActivity(intent);
            }
            return false;
        });

        ( (TextView) findViewById(R.id.elderly_name)).setText("          Elderly " + elderly_name);


        Button meal_reg = findViewById(R.id.mealPlanner);
        meal_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CaregiverElderlyPageActivity.this, CalenderOverviewCaregiver.class);
                get_info = getIntent();
                String elderly_username = get_info.getStringExtra("elderlyUserName");
                String elderly_name = get_info.getStringExtra("elderlyName");
                intent.putExtra("elderlyUserName", elderly_username);
                intent.putExtra("elderlyName", elderly_name);
                intent.putExtra("caregiverUserName", caregiver_username);
                intent.putExtra("caregiverName", caregiver_name);
                //navigator.notis(elderly_username);
                startActivity(intent);
            }
        });

        Button meal_history = findViewById(R.id.mealHistory);

        meal_history.setOnClickListener(view -> {
            Intent intent = new Intent(CaregiverElderlyPageActivity.this, MealHistoryActivity.class);
            startActivity(intent);
        });

        Button personal_info = findViewById(R.id.personalnformation);

        personal_info.setOnClickListener(view -> {
                Intent intent = new Intent(CaregiverElderlyPageActivity.this, PersonalInfoActivity.class);
                intent.putExtra("elderlyUserName", elderly_username);
                intent.putExtra("elderlyName", elderly_name);
                intent.putExtra("caregiverUserName", caregiver_username);
                intent.putExtra("caregiverName", caregiver_name);
                startActivity(intent);
        });

        Button delete_btn = findViewById(R.id.deleteElderly);

        delete_btn.setOnClickListener(view -> {
            Context context = view.getContext();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.popup_confirm_delete, null);

            // Create a PopupWindow
            int width = LinearLayout.LayoutParams.WRAP_CONTENT;
            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
            boolean focusable = true; // let taps outside the popup also dismiss it
            final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
            TextView deleteText = popupView.findViewById(R.id.textView_delete);
            deleteText.setText("Remove " + elderly_name + "?");

            // Set a click listener for the close button in the popup
            Button closePopupBtn = popupView.findViewById(R.id.Button_cancel);
            closePopupBtn.setOnClickListener(v -> {
                // Dismiss the popup
                popupWindow.dismiss();
            });

            Button removeElderlyBtn = popupView.findViewById(R.id.Button_remove);

            removeElderlyBtn.setOnClickListener(v -> {
                db.removeElderly(caregiver_username, elderly_username);
                Intent intent = new Intent(CaregiverElderlyPageActivity.this, Caregiver_dash.class);
                intent.putExtra("elderlyUserName", elderly_username);
                intent.putExtra("elderlyName", elderly_name);
                intent.putExtra("caregiverUserName", caregiver_username);
                intent.putExtra("caregiverName", caregiver_name);
                startActivity(intent);
            });
            // Show the popup at the center of the screen
            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        });


    }
}