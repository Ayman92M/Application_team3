package com.example.application_team3;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;


public class List_viewer extends AppCompatActivity {
    private ListView listView;
    String caregiverString;
    EditText textView_list_username;
    Database db = new Database();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_viewer2);



    }



}