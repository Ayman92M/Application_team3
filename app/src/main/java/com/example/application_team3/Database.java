package com.example.application_team3;


import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

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

        //test();

    }

    public void fetchTest(){
        fetchCaregiver("ggg12", new CaregiverCallback() {
            @Override
            public void onCallback(CaregiverEntry caregiver) {
                System.out.println(caregiver.getName());
                System.out.println(caregiver.getPid() + " " + caregiver.getPassword());
            }
        });
    }

    public void fetchCaregiver(String pid, CaregiverCallback callback){
        caregiverRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(pid).exists())
                {
                    callback.onCallback(snapshot.child(pid).getValue(CaregiverEntry.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void test(){
        checkLoginCaregiver("ggg12", "123456Aa", new MyCallback() {
            @Override
            public void onCallback(Object value) {
                if((boolean) value){
                    System.out.println("Continue");
                }
            }
        });
    }

    public void registerCaregiver(String name, String pid, String password, String phoneNo){
        _caregiver = new CaregiverEntry(name, pid, password, phoneNo);
        caregiverRef.child(pid).setValue(_caregiver);
    }

    public void registerElderly(String name, String pid, int pin, String phoneNo){
        _elderly = new ElderlyEntry(name, pid, pin, phoneNo);
        elderlyRef.child(pid).setValue(_elderly);
    }

    public void checkLoginElderly(String pid, int pin, MyCallback callback){
        elderlyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(pid).exists()){
                    if(Objects.equals(snapshot.child(pid).child("pin").getValue(), pin)){
                        callback.onCallback(true);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void checkLoginCaregiver(String pid, String password, MyCallback callback){
        caregiverRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(pid).exists()){
                    if(Objects.equals(snapshot.child(pid).child("password").getValue(), password)){
                        callback.onCallback(true);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}
