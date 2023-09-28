package com.example.application_team3;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import java.util.ArrayList;
import java.util.List;


public class List_viewer extends AppCompatActivity {
    private ListView listView;
    String elderlyString;
    List<String> elderlyStrings = new ArrayList<>();
    EditText textView_list_username;
    Database db = new Database();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caregiverdash);
        listView = findViewById(R.id.listView);

        TextView add_elderly = findViewById(R.id.TextView_addElderly);

        Task<List<ElderlyEntry>> elderlyListTask = db.ElderlyList("ggg12");
        Tasks.whenAll(elderlyListTask).addOnCompleteListener(task -> {
            List<ElderlyEntry> elderlyList = elderlyListTask.getResult();
            elderlyStrings.clear();
            for(ElderlyEntry elderly : elderlyList){
                elderlyString = elderly.getName() +", " + elderly.getPid();
                elderlyStrings.add(elderlyString);
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    getApplicationContext(), // Använd den aktuella kontexten
                    R.layout.activity_list_item,
                    R.id.textView_list_username,
                    elderlyStrings
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
        });

        add_elderly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }



}