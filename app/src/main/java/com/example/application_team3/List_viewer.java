package com.example.application_team3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class List_viewer extends AppCompatActivity {
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_viewer);

        listView = findViewById(R.id.listView);
        String[] data = {
                "User 1, Subitem 1",
                "User 2, Subitem 21",
                "User 3, Subitem 31",
        };

        /*
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.activity_list_item, R.id.textView1, data);
        listView.setAdapter(adapter);
        */
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.activity_list_item, R.id.textView1, data){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                View row = super.getView(position, convertView, parent);
                String[] itemParts = getItem(position).split(", ");
                TextView textView1 = row.findViewById(R.id.textView1);
                TextView textView2 = row.findViewById(R.id.textView2);

                textView1.setText(itemParts[0]); // Huvudtext (item)
                textView2.setText(itemParts[1]); // Undertext (subitem)

                return row;
            }
        };
        listView.setAdapter(adapter);

    }
}