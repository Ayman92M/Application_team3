package com.example.application_team3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;

public class Caregiver_dash extends AppCompatActivity {
    private ListView listView;
    ViewNavigator navigator = new ViewNavigator(this);
    String _caregiverName, _caregiverUserName;
    Database db = new Database();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caregiverdash);

        //getIntent() används för att hämta data som har skickats till den här aktiviteten via intent.
        Intent get_info = getIntent();
        _caregiverName = get_info.getStringExtra("caregiverName");
        _caregiverUserName = get_info.getStringExtra("caregiverUserName");


        if (_caregiverUserName == null){
            Pair<String, String> caregiver = navigator.getPreferencesCaregiverDashboard();
            _caregiverUserName = caregiver.first;
            _caregiverName = caregiver.second;
        }
        else{
            navigator.saveInputToPreferencesCaregiverDashboard(_caregiverUserName, _caregiverName);
        }


        ((TextView) findViewById(R.id.textView_Welcome)).setText("Welcome" +" "+ _caregiverName);

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


                // Set a click listener for the close button in the popup
                Button closePopupBtn = popupView.findViewById(R.id.Button_cancel);
                closePopupBtn.setOnClickListener(v -> {
                    // Dismiss the popup
                    popupWindow.dismiss();
                });

                Button add_btn = popupView.findViewById(R.id.Button_add);

                add_btn.setOnClickListener(v -> {
                    Task<DataSnapshot> elderlyDBTask = db.fetchElderlyDB();

                    Tasks.whenAll(elderlyDBTask).addOnCompleteListener(task -> {
                        EditText usernameText = popupView.findViewById(R.id.textView_username);
                        DataSnapshot elderlyDB = elderlyDBTask.getResult();
                        String elderly_username = usernameText.getText().toString();

                        if(elderlyDB.hasChild(elderly_username)){
                            ElderlyEntry elderly = elderlyDB.child(elderly_username).getValue(ElderlyEntry.class);
                            Task<DataSnapshot> assignTask = db.assignElderly(_caregiverUserName, _caregiverName, elderly.getPid(), elderly.getName());
                            Tasks.whenAll(assignTask).addOnCompleteListener(task2 -> {
                                popupWindow.dismiss();
                                navigator.showElderlyList(listView, _caregiverUserName, _caregiverName);
                            });

                        }
                        else {
                            navigator.notis("Elderly " + elderly_username + " does not exist");
                        }
                    });
                });
                // Show the popup at the center of the screen
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
            }
        });

        //navigator.addElderlyToCaregiver(addElderly, _caregiverUserName, _caregiverName);



    }

    private void logut() {
        TextView logut = findViewById(R.id.TextView_logut);
        logut.setOnClickListener(view -> {
            //navigator.logout(Caregiver_dash.this, Log_in.class);
            navigator.goToNextActivity(Log_in.class, "null", "logut", "true", null, null);
        });

    }


}