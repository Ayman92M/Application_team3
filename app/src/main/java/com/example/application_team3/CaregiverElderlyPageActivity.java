package com.example.application_team3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.android.material.bottomappbar.BottomAppBar;

public class CaregiverElderlyPageActivity extends AppCompatActivity {
    Controller control;
    CaregiverEntry user;
    ElderlyEntry elderly;
    BottomAppBar bottomAppBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cargiver_elderly_page);
        bottomAppBar = findViewById(R.id.bottomAppBar);

        Intent get_info = getIntent();
        control = (Controller) get_info.getSerializableExtra("controller");
        user = control.getCaregiverUser();
        elderly = control.getElderlyUser();

        bottomAppBar.setOnMenuItemClickListener(item -> {
            if (item.getItemId()==R.id.bottomNav_back){
                control.goToActivity(CaregiverElderlyPageActivity.this, Caregiver_Dash.class);
            }
            return false;
        });

        ( (TextView) findViewById(R.id.elderly_name)).setText("          Elderly " + elderly.getName());


        Button meal_reg = findViewById(R.id.mealPlanner);
        meal_reg.setOnClickListener(view -> control.goToActivity(CaregiverElderlyPageActivity.this, CalenderOverviewCaregiver.class));

        Button personal_info = findViewById(R.id.personalnformation);

        personal_info.setOnClickListener(view -> control.goToActivity(CaregiverElderlyPageActivity.this, PersonalInfoActivity.class));

        deleteButtonListener();

    }

    private void deleteButtonListener(){
        Button delete_btn = findViewById(R.id.deleteElderly);

        delete_btn.setOnClickListener(view -> {
            Context context = view.getContext();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.popup_confirm_delete, null);

            final PopupWindow popupWindow = control.getView().buildPopup(popupView);
            TextView deleteText = popupView.findViewById(R.id.textView_delete);
            deleteText.setText("Remove " + elderly.getName() + "?");

            Button removeElderlyBtn = popupView.findViewById(R.id.Button_remove);

            removeElderlyBtn.setOnClickListener(v -> {
                control.getDb().removeElderly(user.getPid(), elderly.getPid());
                control.goToActivity(CaregiverElderlyPageActivity.this, Caregiver_Dash.class);
            });
            // Show the popup at the center of the screen
            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        });
    }
}