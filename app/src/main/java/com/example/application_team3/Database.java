package com.example.application_team3;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class Database {

    FirebaseDatabase rootNode;
    DatabaseReference caregiverRef;
    DatabaseReference elderlyRef;
    ElderlyEntry _elderly;
    CaregiverEntry _caregiver;
    public Database() {
        rootNode = FirebaseDatabase.getInstance();
        caregiverRef = rootNode.getReference("Caregiver");
        elderlyRef = rootNode.getReference("Elderly");
    }

    public void registerCaregiver(String name, String pid, int password, int phoneNo, List<String> elderly, String dob, String country, String city){
        _caregiver = new CaregiverEntry(name, pid, password, phoneNo, elderly, dob, country, city);
        caregiverRef.child(pid).setValue(_caregiver);
    }

    public void registerElderly(String name, String pid, int pin, int phoneNo, String dob, String country, String city, List<String> caregivers){
        _elderly = new ElderlyEntry(name, pid, pin, phoneNo, dob, country, city, caregivers);
        elderlyRef.child(pid).setValue(_elderly);
    }
}
