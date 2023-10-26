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

public class CaregiverElderlyPageActivity extends AppCompatActivity {
    Controller control;
    Database db;
    ViewBuilder vb;
    CaregiverEntry user;
    ElderlyEntry elderly;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cargiver_elderly_page);

        Intent get_info = getIntent();
        control = (Controller) get_info.getSerializableExtra("controller");
        db = control.getDatabase();
        vb = control.getViewBuilder();
        user = control.getCaregiverUser();
        elderly = control.getElderlyUser();

        Button back_btn = findViewById(R.id.button_back);
        back_btn.setOnClickListener(view -> finish());

        ( (TextView) findViewById(R.id.elderly_name)).setText("       " + getString(R.string.care_client) + " " + elderly.getName());


        Button meal_reg = findViewById(R.id.mealPlanner);
        meal_reg.setOnClickListener(view -> control.goToActivity(CaregiverElderlyPageActivity.this, CalenderOverviewCaregiver.class));

        Button personal_info = findViewById(R.id.personalnformation);
        personal_info.setOnClickListener(view -> control.goToActivity(CaregiverElderlyPageActivity.this, PersonalInfoActivity.class));

        deleteButtonListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        elderly = control.getElderlyUser();
        ( (TextView) findViewById(R.id.elderly_name)).setText("       " + getString(R.string.care_client) + " " + elderly.getName());
    }

    private void deleteButtonListener(){
        Button delete_btn = findViewById(R.id.deleteElderly);

        delete_btn.setOnClickListener(view -> {
            Context context = view.getContext();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.popup_confirm_delete, null);

            final PopupWindow popupWindow = vb.buildPopup(popupView);
            TextView deleteText = popupView.findViewById(R.id.textView_delete);
            deleteText.setText(getString(R.string.remove) + " " + getString(R.string.care_client) + " " + elderly.getName() + "?");

            Button removeElderlyBtn = popupView.findViewById(R.id.Button_remove);

            removeElderlyBtn.setOnClickListener(v -> {
                db.removeElderly(user.getUsername(), elderly.getUsername());
                finish();
            });
            // Show the popup at the center of the screen
            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        });
    }
}