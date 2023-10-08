package com.example.application_team3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ViewNavigator navigator = new ViewNavigator(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_language);

        Button _elderly = findViewById(R.id.button);
        navigator.goToNextActivity(_elderly, Login_elderly.class);

        Button _caregiver = findViewById(R.id.button2);
        navigator.goToNextActivity(_caregiver, Log_in.class);



        Button _test = findViewById(R.id.testButton);
        //Delete Elderly_home_test.class and write the name of your class
        //className.class
        //navigator.goToNextActivity(_caregiver, className.class);
        // --navigator.goToNextActivity(_test, CargiverElderlyPageActivity.class);
        //you can Merge into Master only if it works.
        _test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    public long convertStringToMillis(String dateString) {
        List<String> possibleFormats = new ArrayList<>();
        possibleFormats.add("yyyy-MM-dd HH:mm");
        possibleFormats.add("yyyy-MM-d HH:mm");
        possibleFormats.add("yyyy-M-dd HH:mm");
        possibleFormats.add("yyyy-MM-d H:mm");

        for (String format : possibleFormats) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat(format);
                Date date = dateFormat.parse(dateString);
                System.out.println("convert: "+ date);
                System.out.println("convert: "+ date.getTime());
                return date.getTime();
            } catch (ParseException ignored) {
                // Ignorera ParseException för detta format, fortsätt med nästa
            }
        }

        // Ingen matchande format hittades
        return -1;
    }

}