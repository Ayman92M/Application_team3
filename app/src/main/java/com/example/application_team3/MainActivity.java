package com.example.application_team3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseDatabase rootNode = FirebaseDatabase.getInstance("https://db-app-group3-default-rtdb.europe-west1.firebasedatabase.app/database");
        DatabaseReference reference = rootNode.getReference("message");

        reference.setValue("Hello World!");
    }
}