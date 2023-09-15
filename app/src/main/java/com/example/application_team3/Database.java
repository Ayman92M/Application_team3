package com.example.application_team3;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.LinkedList;
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

    public void registerCaregiver(String name, String pid, String password, String phoneNo, String dob, String country, String city){
        _caregiver = new CaregiverEntry(name, pid, password, phoneNo, dob, country, city);
        caregiverRef.child(pid).setValue(_caregiver);
    }

    public void registerElderly(String name, String pid, int pin, String phoneNo, String dob, String country, String city){
        _elderly = new ElderlyEntry(name, pid, pin, phoneNo, dob, country, city);
        elderlyRef.child(pid).setValue(_elderly);
    }

    public void checkLoginElderly(){

    }

    public void checkLoginCaregiver(){

    }
}
