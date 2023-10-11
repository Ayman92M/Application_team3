package com.example.application_team3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;

public class PersonalInfoActivity extends AppCompatActivity {

    private String elderlyUsername;
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

        nameText = this.findViewById(R.id.editTextText);
        usernameText = this.findViewById(R.id.editTextText9);
        pinText = this.findViewById(R.id.editTextText10);
        phoneNoText = this.findViewById(R.id.editTextText12);
        dateOfBirthText = this.findViewById(R.id.editTextText13);
        addressText = this.findViewById(R.id.editTextText14);

        Intent get_info = getIntent();
        elderlyUsername = get_info.getStringExtra("elderlyUserName");

        showData();

        Tasks.whenAll(elderlyTask).addOnCompleteListener(task -> edit_button());



    }

    private void showData(){
        Task<DataSnapshot> elderlyTask = db.fetchElderly(elderlyUsername);

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