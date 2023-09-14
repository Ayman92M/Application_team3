package com.example.application_team3;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Database {

    FirebaseDatabase rootNode;
    DatabaseReference caregiverReference;
    DatabaseReference elderlyReference;
    public Database() {
        rootNode = FirebaseDatabase.getInstance();
        caregiverReference = rootNode.getReference("Caregiver");
        elderlyReference = rootNode.getReference("Elderly");

        ElderlyEntry _elderly = new ElderlyEntry("Britt-Marie");
        elderlyReference.child("1").setValue(_elderly);
    }
}
