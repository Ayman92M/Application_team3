package com.example.application_team3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;

import java.util.List;

public class Caregiver_Dash extends AppCompatActivity {
    private ListView listView;
    Controller control;
    CaregiverEntry user;
    List<String> elderlyStrings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caregiverdash);

        Intent get_info = getIntent();
        control = (Controller) get_info.getSerializableExtra("controller");
        user = control.getCaregiverUser();
        control.setElderlyUser(null);

        TextView welcomeText = findViewById(R.id.textView_Welcome);
        welcomeText.setText("Welcome " + user.getName());

        listView = findViewById(R.id.listView);
        elderlyStrings = control.getView().setupElderlyListView(user, listView, this);
        elderlyListActionListener();

        TextView logout = findViewById(R.id.TextView_logut);
        logout.setOnClickListener(view -> control.logout(Caregiver_Dash.this, MainActivity.class));

        TextView newElderly = findViewById(R.id.TextView_newElderly);
        newElderly.setOnClickListener(view -> control.goToActivity(Caregiver_Dash.this, Signup_elderly.class));

        TextView addElderly = findViewById(R.id.TextView_addElderly);
        addElderly.setOnClickListener(view -> {
            LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.popup_add_elderly, null);
            PopupWindow popupWindow = control.getView().buildPopup(popupView);

            addElderlyActionListener(popupView, popupWindow);
            // Show the popup at the center of the screen
            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        });

    }

    public void addElderlyActionListener(View popupView, PopupWindow popupWindow){
        Button add_btn = popupView.findViewById(R.id.Button_add);
        add_btn.setOnClickListener(v -> {
            Task<DataSnapshot> elderlyDBTask = control.getDb().fetchElderlyDB();

            Tasks.whenAll(elderlyDBTask).addOnCompleteListener(task -> {
                EditText usernameText = popupView.findViewById(R.id.textView_username);
                DataSnapshot elderlyDB = elderlyDBTask.getResult();
                String elderly_username = usernameText.getText().toString();

                if(elderlyDB.hasChild(elderly_username)){
                    ElderlyEntry elderly = elderlyDB.child(elderly_username).getValue(ElderlyEntry.class);
                    Task<DataSnapshot> assignTask = control.getDb().assignElderly(user.getPid(), user.getName(), elderly.getPid(), elderly.getName());
                    Tasks.whenAll(assignTask).addOnCompleteListener(task2 -> {
                        popupWindow.dismiss();
                        listView.refreshDrawableState();
                        elderlyStrings = control.getView().setupElderlyListView(user, listView, this);
                        elderlyListActionListener();
                    });

                }
                else {
                    control.getView().notis("Elderly " + elderly_username + " does not exist", Caregiver_Dash.this);
                }
            });
        });
    }
    public void elderlyListActionListener(){
        String[] elderlyArray = elderlyStrings.toArray(new String[0]);

        // Sätter en klickhändelse för ListView
        listView.setOnItemClickListener((parent, view, position, id) -> {
            // Hämtar det valda elementet från listan baserat på positionen
            String selectedItem = elderlyArray[position];
            // Delar upp det valda elementet i delar med hjälp av ", " som separator.
            String[] nameParts = selectedItem.split(", ");

            Task<DataSnapshot> elderlyTask = control.getDb().fetchElderly(nameParts[1]);

            Tasks.whenAll(elderlyTask).addOnCompleteListener(task -> {
                ElderlyEntry elderly = elderlyTask.getResult().getValue(ElderlyEntry.class);
                control.setElderlyUser(elderly);
                control.goToActivity(Caregiver_Dash.this, CaregiverElderlyPageActivity.class);
            });
        });
    }


}