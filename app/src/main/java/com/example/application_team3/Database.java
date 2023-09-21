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

    boolean loginSuccess;
    public Database() {
        rootNode = FirebaseDatabase.getInstance();
        caregiverRef = rootNode.getReference("Caregiver");
        elderlyRef = rootNode.getReference("Elderly");

    }

    public void registerCaregiver(String name, String pid, String password, String phoneNo, String birthday, String address){
        _caregiver = new CaregiverEntry(name, pid, password, phoneNo, birthday, address, null);
        caregiverRef.child(pid).setValue(_caregiver);
    }

    public void registerElderly(String name, String pid, int pin, String phoneNo, String birthday, String address, String caregiver){
        _elderly = new ElderlyEntry(name, pid, pin, phoneNo, birthday, address, caregiver);
        elderlyRef.child(pid).setValue(_elderly);
    }

    public boolean checkLoginElderly(String pid, int pin){
        loginSuccess=false;
        elderlyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(pid).exists()){
                    if(Objects.equals(snapshot.child(pid).child("pin").getValue(), pin)){
                        loginSuccess = true;
                        _elderly = snapshot.child(pid).getValue(ElderlyEntry.class);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return loginSuccess;
    }

    public boolean getLoginSuccess(){
        return loginSuccess;
    }
    public void setLoginSuccess(){
        loginSuccess=true;
    }

    public void checkLoginCaregiver(String pid, String password){
        loginSuccess=false;
        caregiverRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(pid).exists()){
                    System.out.println(snapshot.child(pid).getValue());
                    System.out.println("1" + loginSuccess);
                    if(Objects.equals(snapshot.child(pid).child("password").getValue(), password)){
                        setLoginSuccess();
                        System.out.println("2" +loginSuccess);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        System.out.println("3" + loginSuccess);

    }
}
