package com.example.application_team3;


import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
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
                    else
                        callback.onCallback(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void checkLoginCaregiver(String pid, MyCallback callback){
        caregiverRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(pid).exists())
                        callback.onCallback(true);
                else
                    callback.onCallback(false);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void checkUsername(String user_name, MyCallback callback){
        checkLoginCaregiver(user_name, new MyCallback() {
            @Override
            public void onCallback(Object value) {
                if((boolean) value)
                    callback.onCallback(true);

                else
                    callback.onCallback(false);

            }
        });
    }

    public String getCaregiverName(String pid, NameCallback callback){
        caregiverRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(pid).exists()){
                    String name = snapshot.child(pid).child("name").getValue(String.class);
                    callback.onNameFetched(name);
                }
                else {
                    callback.onNameFetched(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return null;
    }

    public CaregiverEntry getCaregiverEntry(String pid, CaregiverEntryCallback callback) {
        caregiverRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(pid).exists()){
                    String name = snapshot.child(pid).child("name").getValue(String.class);
                    String password = snapshot.child(pid).child("password").getValue(String.class);
                    String phoneNo = snapshot.child(pid).child("phoneNo").getValue(String.class);

                    CaregiverEntry caregiverEntry = new CaregiverEntry(name, pid, password, phoneNo);
                    callback.onEntryFetched(caregiverEntry);
                }
                else{
                    callback.onEntryFetched(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return null;
    }

    public interface NameCallback {
        void onNameFetched(String name);
    }

    public interface ListCallback {
        void onPidValuesFetched(List<String> pidList);
    }

}
