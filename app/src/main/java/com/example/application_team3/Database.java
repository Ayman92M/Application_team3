package com.example.application_team3;



import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Database {

    FirebaseDatabase rootNode;
    DatabaseReference caregiverRef;
    DatabaseReference elderlyRef;
    DatabaseReference mealPlanRef;

    public Database() {
        rootNode = FirebaseDatabase.getInstance();
        caregiverRef = rootNode.getReference("Caregiver");
        elderlyRef = rootNode.getReference("Elderly");
        mealPlanRef = rootNode.getReference("Meal-Plan");
    }

    public Task<DataSnapshot> fetchCaregiverDB(){
        return caregiverRef.get();
    }
    public Task<DataSnapshot> fetchElderlyDB(){
        return elderlyRef.get();
    }
    public Task<DataSnapshot> fetchMealPlanDB(){
        return mealPlanRef.get();
    }

    public Task<DataSnapshot> fetchCaregiver(String pid){
        return caregiverRef.child(pid).get();
    }
    public Task<DataSnapshot> fetchElderly(String pid){
        return elderlyRef.child(pid).get();
    }

    public void registerCaregiver(String name, String pid, String password, String phoneNo){
        CaregiverEntry caregiver = new CaregiverEntry(name, pid, password, phoneNo);
        caregiverRef.child(pid).setValue(caregiver);
    }
    public void updateCaregiver(CaregiverEntry caregiver){
        caregiverRef.child(caregiver.getPid()).setValue(caregiver);
    }

    public void registerElderly(String name, String pid, String pin, String phoneNo, String caregiver){
        ElderlyEntry elderly = new ElderlyEntry(name, pid, pin, phoneNo, caregiver);
        elderlyRef.child(pid).setValue(elderly);
    }
    public void updateElderly(ElderlyEntry elderly){
        elderlyRef.child(elderly.getPid()).setValue(elderly);
    }
    public void registerMealPlan(String pid, String date, String breakfastNote, String lunchNote, String smallMealNote, String dinnerNote){
        MealPlanEntry mealPlan = new MealPlanEntry(date, breakfastNote, lunchNote, smallMealNote, dinnerNote);
        mealPlanRef.child(pid).child(date).setValue(mealPlan);
    }

    public Task<DataSnapshot> fetchMealPlan(String pid){
        TaskCompletionSource<DataSnapshot> mealPlanTaskSource = new TaskCompletionSource<>();
        Task<DataSnapshot> mealPlanTask = mealPlanTaskSource.getTask();
        Task<DataSnapshot> mealPlanDBTask = fetchMealPlanDB();

        Tasks.whenAll(mealPlanDBTask).addOnCompleteListener(task ->{
            DataSnapshot mealPlanDB = mealPlanDBTask.getResult();

            if(mealPlanDB.child(pid).exists()){
                mealPlanTaskSource.setResult(mealPlanDB.child(pid));
            }
            else { mealPlanTaskSource.setResult(null); }
        });
        return mealPlanTask;
    }
    public Task<DataSnapshot> fetchMealPlanDate(String pid, String date){
        TaskCompletionSource<DataSnapshot> mealPlanTaskSource = new TaskCompletionSource<>();
        Task<DataSnapshot> mealPlanTask = mealPlanTaskSource.getTask();
        Task<DataSnapshot> mealPlanDBTask = fetchMealPlanDB();

        Tasks.whenAll(mealPlanDBTask).addOnCompleteListener(task ->{
            DataSnapshot mealPlanDB = mealPlanDBTask.getResult();

            if(mealPlanDB.child(pid).exists()){
                if(mealPlanDB.child(pid).child(date).exists()){
                    mealPlanTaskSource.setResult(mealPlanDB.child(pid).child(date));
                }
                else{ mealPlanTaskSource.setResult(null); }
            }
            else { mealPlanTaskSource.setResult(null); }
        });
        return mealPlanTask;
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

    public Task<List<ElderlyEntry>> ElderlyList(String caregiverPID){
        TaskCompletionSource<List<ElderlyEntry>> listTaskSource = new TaskCompletionSource<>();
        Task<List<ElderlyEntry>> listTask = listTaskSource.getTask();
        Task<DataSnapshot> elderlyDBTask = fetchElderlyDB();
        Task<DataSnapshot> caregiverTask = fetchCaregiver(caregiverPID);

        Tasks.whenAll(elderlyDBTask, caregiverTask).addOnCompleteListener(task -> {
            CaregiverEntry caregiver = caregiverTask.getResult().getValue(CaregiverEntry.class);
            DataSnapshot elderlyDB = elderlyDBTask.getResult();
            List<ElderlyEntry> elderlyList = new ArrayList<>();
            if(caregiver.getElderly() != null){
                for(String e_pid : caregiver.getElderly().keySet()){
                    if(elderlyDB.child(e_pid).exists()){
                        elderlyList.add(elderlyDB.child(e_pid).getValue(ElderlyEntry.class));
                    }
                }
                listTaskSource.setResult(elderlyList);
            }
        });
        return listTask;
    }

    public Task<List<CaregiverEntry>> CaregiverList(String elderlyPID){
        TaskCompletionSource<List<CaregiverEntry>> listTaskSource = new TaskCompletionSource<>();
        Task<List<CaregiverEntry>> listTask = listTaskSource.getTask();
        Task<DataSnapshot> caregiverDBTask = fetchCaregiverDB();
        Task<DataSnapshot> elderlyTask = fetchElderly(elderlyPID);

        Tasks.whenAll(caregiverDBTask, elderlyTask).addOnCompleteListener(task -> {
            ElderlyEntry elderly = elderlyTask.getResult().getValue(ElderlyEntry.class);
            DataSnapshot caregiverDB = caregiverDBTask.getResult();
            List<CaregiverEntry> caregiverList = new ArrayList<>();
            if(elderly.getCaregivers() != null){
                for(String c_pid : elderly.getCaregivers().keySet()){
                    if(caregiverDB.child(c_pid).exists()){
                        caregiverList.add(caregiverDB.child(c_pid).getValue(CaregiverEntry.class));
                    }
                }
            }
            listTaskSource.setResult(caregiverList);
        });
        return listTask;
    }


}
