package com.example.application_team3;



import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    }

    public Task<DataSnapshot> fetchCaregiverDB(){
        return caregiverRef.get();
    }
    public Task<DataSnapshot> fetchElderlyDB(){
        return elderlyRef.get();
    }

    public Task<DataSnapshot> fetchCaregiver(String pid){
        return caregiverRef.child(pid).get();
    }
    public Task<DataSnapshot> fetchElderly(String pid){
        return elderlyRef.child(pid).get();
    }

    public void registerCaregiver(String name, String pid, String password, String phoneNo){
        _caregiver = new CaregiverEntry(name, pid, password, phoneNo);
        caregiverRef.child(pid).setValue(_caregiver);
    }

    public void registerElderly(String name, String pid, String pin, String phoneNo){
        _elderly = new ElderlyEntry(name, pid, pin, phoneNo);
        elderlyRef.child(pid).setValue(_elderly);
    }

    public Task<Boolean> checkLoginElderly(String pid, String pin){
        Task<DataSnapshot> elderlyTask = fetchElderlyDB();
        TaskCompletionSource<Boolean> boolSource = new TaskCompletionSource<>();
        Task<Boolean> bool = boolSource.getTask();

        Tasks.whenAll(elderlyTask).addOnCompleteListener(task ->{
            boolean exists = false;
            DataSnapshot snapshot = elderlyTask.getResult();
            if(snapshot.child(pid).exists()) {
                if (Objects.equals(snapshot.child(pid).child("pin").getValue(), pin)) {
                    exists = true;
                }
            }
            boolSource.setResult(exists);
        });

        return bool;
    }

    public Task<Boolean> checkLoginCaregiver(String pid, String password){
        Task<DataSnapshot> caregiverTask = fetchCaregiverDB();
        TaskCompletionSource<Boolean> boolSource = new TaskCompletionSource<>();
        Task<Boolean> bool = boolSource.getTask();

        Tasks.whenAll(caregiverTask).addOnCompleteListener(task ->{
            boolean exists = false;
            DataSnapshot snapshot = caregiverTask.getResult();
            if(snapshot.child(pid).exists()) {
                if (Objects.equals(snapshot.child(pid).child("password").getValue(), password)) {
                    exists = true;
                }
            }
            boolSource.setResult(exists);
        });

        return bool;

    }

}
