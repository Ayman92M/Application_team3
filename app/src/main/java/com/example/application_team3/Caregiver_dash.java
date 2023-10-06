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
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class Caregiver_dash extends AppCompatActivity {
    private ListView listView;
    ViewNavigator navigator = new ViewNavigator(this);
    String _caregiverName, _caregiverUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caregiverdash);

        //getIntent() används för att hämta data som har skickats till den här aktiviteten via intent.
        Intent get_info = getIntent();
        _caregiverName = get_info.getStringExtra("caregiverName");
        _caregiverUserName = get_info.getStringExtra("caregiverUserName");

        ((TextView) findViewById(R.id.textView_Welcome)).setText("Welcome " + _caregiverName);

        listView = findViewById(R.id.listView);
        navigator.showElderlyList(listView, _caregiverUserName, _caregiverName);

        logut();

        TextView newElderly = findViewById(R.id.TextView_newElderly);
        //navigator.goToNextActivity(newElderly, Signup_elderly.class);

        navigator.goToNextActivity(newElderly, Signup_elderly.class,
                "caregiverUserName",_caregiverUserName ,
                "caregiverName", _caregiverName);

        TextView addElderly = findViewById(R.id.TextView_addElderly);
        addElderly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Context context = view.getContext();
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.popup_add_elderly, null);

                // Create a PopupWindow
                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                boolean focusable = true; // let taps outside the popup also dismiss it
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                //Button Database
                // Set a click listener for the close button in the popup
                Button closePopupBtn = popupView.findViewById(R.id.Button_cancel);
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

        //navigator.addElderlyToCaregiver(addElderly, _caregiverUserName, _caregiverName);



    }

    private void logut() {
        TextView logut = findViewById(R.id.TextView_logut);
        logut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //navigator.logout(Caregiver_dash.this, Log_in.class);
                navigator.goToNextActivity(Log_in.class, "null", "logut", "true", null, null);
            }
        });

    }


}