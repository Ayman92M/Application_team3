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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CaregiverDash extends AppCompatActivity {
    private ListView listView;
    Controller control;
    Database db;
    ViewBuilder vb;
    CaregiverEntry user;
    List<String> elderlyStrings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caregiverdash);

        Intent get_info = getIntent();
        control = (Controller) get_info.getSerializableExtra("controller");
        db = control.getDatabase();
        vb = control.getViewBuilder();
        user = control.getCaregiverUser();

        control.setElderlyUser(null);

        TextView welcomeText = findViewById(R.id.textView_Welcome);
        welcomeText.setText("Welcome " + user.getName());
        listView = findViewById(R.id.listView);
        elderlyStrings = new ArrayList<>();
        setupElderlyList();

        TextView logout = findViewById(R.id.TextView_logut);
        logout.setOnClickListener(view -> control.logout(CaregiverDash.this));

        TextView newElderly = findViewById(R.id.TextView_newElderly);
        newElderly.setOnClickListener(view -> control.goToActivity(CaregiverDash.this, SignupElderly.class));

        TextView addElderly = findViewById(R.id.TextView_addElderly);
        // Show the popup at the center of the screen
        addElderly.setOnClickListener(this::addElderlyActionListener);


    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("Resume");
        control.setElderlyUser(null);
        setupElderlyList();
    }

    private void addElderlyActionListener(View view){
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_add_elderly, null);
        PopupWindow popupWindow = vb.buildPopup(popupView);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        Button add_btn = popupView.findViewById(R.id.Button_add);
        add_btn.setOnClickListener(v -> {
            Task<DataSnapshot> elderlyDBTask = db.fetchElderlyDB();

            Tasks.whenAll(elderlyDBTask).addOnCompleteListener(task -> {
                EditText usernameText = popupView.findViewById(R.id.textView_username);
                DataSnapshot elderlyDB = elderlyDBTask.getResult();
                String elderly_username = usernameText.getText().toString();

                if(elderlyDB.hasChild(elderly_username)){
                    ElderlyEntry elderly = elderlyDB.child(elderly_username).getValue(ElderlyEntry.class);
                    Task<DataSnapshot> assignTask = control.getDatabase().assignElderly(user.getUsername(), user.getName(), elderly.getUsername(), elderly.getName());
                    Tasks.whenAll(assignTask).addOnCompleteListener(task2 -> {
                        popupWindow.dismiss();
                        setupElderlyList();
                        recreate();
                    });

                }
                else {
                    vb.notis("Elderly " + elderly_username + " does not exist", CaregiverDash.this);
                }
            });
        });
    }
    private void elderlyListActionListener(){
        String[] elderlyArray = elderlyStrings.toArray(new String[0]);

        listView.setOnItemClickListener((parent, view, position, id) -> {

            String selectedItem = elderlyArray[position];

            String[] nameParts = selectedItem.split(", ");

            Task<DataSnapshot> elderlyTask = db.fetchElderly(nameParts[1]);

            Tasks.whenAll(elderlyTask).addOnCompleteListener(task -> {
                ElderlyEntry elderly = elderlyTask.getResult().getValue(ElderlyEntry.class);
                control.setElderlyUser(elderly);
                control.goToActivity(CaregiverDash.this, CaregiverElderlyPageActivity.class);
            });
        });
    }

    private void setupElderlyList(){
        Task<DataSnapshot> caregiverTask = db.fetchCaregiver(control.getCaregiverUser().getUsername());
        Tasks.whenAll(caregiverTask).addOnCompleteListener(task -> {
            user = caregiverTask.getResult().getValue(CaregiverEntry.class);
            control.setCaregiverUser(user);
            elderlyStrings.clear();
            HashMap<String, String> elderlyList = user.getElderly();
            elderlyList.forEach((username, name) ->{
                Notification notification = control.getNotification();
                control.updateNotificationCaregiver(username, name, findViewById(R.id.textView_username), this);
                notification.runFunctionCaregiver(CaregiverDash.this, username, name);
                notification.runCopyMeal(CaregiverDash.this, username);
                String elderlyString = name + ", " + username;
                elderlyStrings.add(elderlyString);
            });
            vb.buildListView(false,
                    elderlyStrings, this, listView,
                    R.layout.activity_list_item, R.id.textView_username, R.id.textView_list_pid
            );
            elderlyListActionListener();
        });

    }


}