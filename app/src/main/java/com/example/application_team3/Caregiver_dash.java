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
    ViewNavigator navigator = new ViewNavigator(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caregiverdash);

        //getIntent() används för att hämta data som har skickats till den här aktiviteten via intent.
        Intent get_info = getIntent();

        //hämta en sträng med nyckeln "name" från den tidigare nämnda intenten.
        String _caregiverName = get_info.getStringExtra("name");
        ((TextView) findViewById(R.id.textView_Welcome)).setText("Welcome " + _caregiverName);

        listView = findViewById(R.id.listView);
        String pid = get_info.getStringExtra("pid");
        navigator.showList(listView, pid);

        TextView addElderly = findViewById(R.id.TextView_addElderly);
        String _caregiverUserName = get_info.getStringExtra("pid");
        navigator.goToNextActivity(addElderly, Signup_elderly.class, "caregiverUserName",_caregiverName , "caregiverName", _caregiverUserName);

    }
    /*
    private void showList(String pid){
        listView = findViewById(R.id.listView);

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

            elderlyOverview();

        });
    }

    private void elderlyOverview(){
        String[] elderlyArray = elderlyStrings.toArray(new String[elderlyStrings.size()]);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String selectedItem = elderlyArray[position];
                String[] nameParts = selectedItem.split(", ");

                navigator.goToNextActivity(Elderly_home_test.class, "Elderly username skickas till databasen för att få Elderly overview" + nameParts[0]+ " " + nameParts[1],
                        "name", nameParts[0], "username", nameParts[1]);

            }
        });
    }

     */
}