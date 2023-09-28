package com.example.application_team3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import java.util.ArrayList;
import java.util.List;

public class Caregiver_dash extends AppCompatActivity {
    private ListView listView;
    String elderlyString;
    List<String> elderlyStrings = new ArrayList<>();
    UserAccountControl user = new UserAccountControl();
    Database db = new Database();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caregiverdash);

        Intent get_user_name = getIntent();
        String message = get_user_name.getStringExtra("key");
        String pid = get_user_name.getStringExtra("pid");
        ( (TextView) findViewById(R.id.textView_Welcome)).setText("Welcome " + message);

        listView = findViewById(R.id.listView);

        TextView add_elderly = findViewById(R.id.TextView_addElderly);

        Task<List<ElderlyEntry>> elderlyListTask = db.ElderlyList(pid);
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
                    TextView textView2 = row.findViewById(R.id.textView_list_pid);

                    textView1.setText(itemParts[0]); // Huvudtext (item)
                    textView2.setText(itemParts[1]); // Undertext (subitem)

                    return row;
                }
            };

            listView.setAdapter(adapter);

            String[] elderlyArray = elderlyStrings.toArray(new String[elderlyStrings.size()]);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // Här kan du hantera klickhändelsen för det valda elementet
                    String selectedItem = elderlyArray[position]; // Använd den omvandlade arrayen här
                    Intent page1 = new Intent(Caregiver_dash.this, Elderly_home_test.class);
                    String elderly_name = elderlyArray[position];
                    String[] nameParts = elderly_name.split(", ");
                    page1.putExtra("key", nameParts[0]);
                    startActivity(page1);
                    // Gör något med det valda objektet, t.ex. visa det eller starta en ny aktivitet.
                }
            });
        });

        TextView addElderly = findViewById(R.id.TextView_addElderly);
        addElderly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent page = new Intent(Caregiver_dash.this, Signup_elderly.class);
                startActivity(page);
            }
        });


    }


}