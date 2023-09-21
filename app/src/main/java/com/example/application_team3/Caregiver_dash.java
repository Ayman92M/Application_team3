package com.example.application_team3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Caregiver_dash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caregiverdash);

        Intent get_user_name = getIntent();
        String message = get_user_name.getStringExtra("key");

        ( (TextView) findViewById(R.id.textView_Welcome)).setText("Welcome " + message);


        Button list_bt = findViewById(R.id.button4);
        list_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent page = new Intent(Caregiver_dash.this, List_viewer.class);
                startActivity(page);



            }
        });

    }
}