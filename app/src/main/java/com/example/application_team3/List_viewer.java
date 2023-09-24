package com.example.application_team3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class List_viewer extends AppCompatActivity {
    private ListView listView;
    Database db = new Database();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_viewer2);
        

        Intent get_user_name = getIntent();
        String message = get_user_name.getStringExtra("key");
        ( (TextView) findViewById(R.id.textView5)).setText("Welcome " + message);

        
        listView = findViewById(R.id.listView);
        showList("ggg12");
        
        
    }

   
    private void showList(String pid) {
        List<CaregiverEntry> caregiverList = new ArrayList<>();
        List<String> caregiverStrings = new ArrayList<>();

        db.getCaregiverList(pid, new Database.ListCallback() {
            @Override
            public void onListValuesFetched(List<CaregiverEntry> CaregiverList) {
                if (CaregiverList != null) {
                    for (CaregiverEntry entry : CaregiverList) {
                        String caregiverString =  entry.getName() + ", " + entry.getPid();
                        caregiverStrings.add(caregiverString);
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            getApplicationContext(), // Använd den aktuella kontexten
                            R.layout.activity_list_item,
                            R.id.textView_list_username,
                            caregiverStrings
                    ) {
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            View row = super.getView(position, convertView, parent);
                            String[] itemParts = getItem(position).split(", ");
                            TextView textView1 = row.findViewById(R.id.textView_list_username);
                            TextView textView2 = row.findViewById(R.id.textView_list_subitem);

                            textView1.setText(itemParts[0]); // Huvudtext (item)
                            textView2.setText(itemParts[1]); // Undertext (subitem)

                            return row;
                        }
                    };

                    listView.setAdapter(adapter);
                } else {
                    System.out.println("Ingen matchande vårdgivare hittades.");
                }
            }
        });
    }
    
    
    /*      
        String[] data = {
                "User 1, Subitem 1",
                "User 2, Subitem 21",
                "User 3, Subitem 31",
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.activity_list_item, R.id.textView1, data);
        listView.setAdapter(adapter);
        */
        /*
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.activity_list_item,
                R.id.textView_list_username, data){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                View row = super.getView(position, convertView, parent);
                String[] itemParts = getItem(position).split(", ");
                TextView textView1 = row.findViewById(R.id.textView_list_username);
                TextView textView2 = row.findViewById(R.id.textView_list_subitem);

                textView1.setText(itemParts[0]); // Huvudtext (item)
                textView2.setText(itemParts[1]); // Undertext (subitem)

                return row;
            }
        };
        listView.setAdapter(adapter);
        */
}