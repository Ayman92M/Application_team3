package com.example.application_team3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.firebase.database.DataSnapshot;

public class PersonalInfoActivity extends AppCompatActivity {
    BottomAppBar bottomAppBar;
    private String elderly_username, elderly_name, caregiver_username, caregiver_name;
    private final Database db = new Database();
    TaskCompletionSource<ElderlyEntry> elderlyTaskSource = new TaskCompletionSource<>();
    Task<ElderlyEntry> elderlyTask = elderlyTaskSource.getTask();
    private ElderlyEntry elderly;
    private TextView nameText;
    private TextView usernameText;
    private TextView pinText;
    private TextView phoneNoText;
    private TextView dateOfBirthText;
    private TextView addressText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        Intent get_info = getIntent();
        elderly_username = get_info.getStringExtra("elderlyUserName");
        elderly_name = get_info.getStringExtra("elderlyName");
        caregiver_username = get_info.getStringExtra("caregiverUserName");
        caregiver_name = get_info.getStringExtra("caregiverName");

        bottomAppBar = findViewById(R.id.bottomAppBar);
        bottomAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId()==R.id.bottomNav_back){
                    Intent intent = new Intent(PersonalInfoActivity.this, CaregiverElderlyPageActivity.class);
                    intent.putExtra("elderlyName", elderly_name);
                    intent.putExtra("elderlyUserName", elderly_username);
                    intent.putExtra("caregiverName", caregiver_name);
                    intent.putExtra("caregiverUserName", caregiver_username);
                    startActivity(intent);
                }
                return false;
            }
        });

        nameText = this.findViewById(R.id.editTextText);
        usernameText = this.findViewById(R.id.editTextText9);
        pinText = this.findViewById(R.id.editTextText10);
        phoneNoText = this.findViewById(R.id.editTextText12);
        dateOfBirthText = this.findViewById(R.id.editTextText13);
        addressText = this.findViewById(R.id.editTextText14);



        showData();

        Tasks.whenAll(elderlyTask).addOnCompleteListener(task -> edit_button());



    }

    private void showData(){
        Task<DataSnapshot> elderlyTask = db.fetchElderly(elderly_username);

        Tasks.whenAll(elderlyTask).addOnCompleteListener(task -> {
            elderly = elderlyTask.getResult().getValue(ElderlyEntry.class);
            nameText.setText(elderly.getName());
            usernameText.setText(elderly.getPid());
            pinText.setText(elderly.getPin());
            phoneNoText.setText(elderly.getPhoneNo());
            dateOfBirthText.setText(elderly.getBirthday());
            addressText.setText(elderly.getAddress());
            elderlyTaskSource.setResult(elderly);
        });
    }

    private void edit_button()
    {
        Button button_edit = findViewById(R.id.edit_button);
        button_edit.setOnClickListener(view -> {
            Intent intent = new Intent(PersonalInfoActivity.this, Signup_elderly.class);
            intent.putExtra("elderlyName", elderly.getName());
            intent.putExtra("elderlyUserName", elderly.getPid());
            intent.putExtra("elderlyPin", elderly.getPin());
            intent.putExtra("elderlyPhoneNo", elderly.getPhoneNo());
            intent.putExtra("elderlyDateOfBirth", elderly.getBirthday());
            intent.putExtra("elderlyAddress", elderly.getAddress());

            startActivity(intent);
        });
    }
}