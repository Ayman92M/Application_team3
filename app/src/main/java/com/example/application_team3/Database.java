package com.example.application_team3;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Database implements Serializable {

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

    public Task<DataSnapshot> fetchCaregiver(String username){
        return caregiverRef.child(username).get();
    }
    public Task<DataSnapshot> fetchElderly(String username){
        return elderlyRef.child(username).get();
    }

    public void registerCaregiver(String name, String username, String password, String phoneNo){
        CaregiverEntry caregiver = new CaregiverEntry(name, username, password, phoneNo);
        caregiverRef.child(username).setValue(caregiver);
    }
    public void updateCaregiver(CaregiverEntry caregiver){
        caregiverRef.child(caregiver.getUsername()).setValue(caregiver);
    }

    public void registerElderly(String name, String username, String pin, String phoneNo, String dateOfBirth, String address, String caregiver_name, String caregiver_username){
        ElderlyEntry elderly = new ElderlyEntry(name, username, pin, phoneNo);
        elderly.setBirthday(dateOfBirth);
        elderly.setAddress(address);
        elderlyRef.child(username).setValue(elderly);
        assignElderly(caregiver_username, caregiver_name, username, name);
    }
    public void updateElderly(ElderlyEntry elderly){
        elderlyRef.child(elderly.getUsername()).setValue(elderly);

    }
    public void registerMeal(String username, String date, String mealType, String time, String note){
        MealEntry mealEntry = new MealEntry(date, mealType, time, note, false);
        mealPlanRef.child(username).child(date).child(mealType).setValue(mealEntry);
    }
    public void updateMeal(String username, MealEntry meal)
    {
        mealPlanRef.child(username).child(meal.getDate()).child(meal.getMealType()).setValue(meal);
    }
    public void deleteMeal(String username, String date, String mealType){
        mealPlanRef.child(username).child(date).child(mealType).removeValue();
    }
    public void hasEatenMeal(String username, String date, String mealType){
        mealPlanRef.child(username).child(date).child(mealType).child("hasEaten").setValue(true);
    }

    public Task<DataSnapshot> fetchMealPlan(String username){
        TaskCompletionSource<DataSnapshot> mealPlanTaskSource = new TaskCompletionSource<>();
        Task<DataSnapshot> mealPlanTask = mealPlanTaskSource.getTask();
        Task<DataSnapshot> mealPlanDBTask = fetchMealPlanDB();

        Tasks.whenAll(mealPlanDBTask).addOnCompleteListener(task ->{
            DataSnapshot mealPlanDB = mealPlanDBTask.getResult();

            if(mealPlanDB.child(username).exists()){
                mealPlanTaskSource.setResult(mealPlanDB.child(username));
            }
            else { mealPlanTaskSource.setResult(null); }
        });
        return mealPlanTask;
    }
    public Task<DataSnapshot> fetchMealPlanDate(String username, String date){
        TaskCompletionSource<DataSnapshot> mealPlanTaskSource = new TaskCompletionSource<>();
        Task<DataSnapshot> mealPlanTask = mealPlanTaskSource.getTask();
        Task<DataSnapshot> mealPlanDBTask = fetchMealPlanDB();

        Tasks.whenAll(mealPlanDBTask).addOnCompleteListener(task ->{
            DataSnapshot mealPlanDB = mealPlanDBTask.getResult();

            if(mealPlanDB.child(username).exists()){
                if(mealPlanDB.child(username).child(date).exists()){
                    mealPlanTaskSource.setResult(mealPlanDB.child(username).child(date));
                }
                else{ mealPlanTaskSource.setResult(null); }
            }
            else { mealPlanTaskSource.setResult(null); }
        });
        return mealPlanTask;
    }

    public Task<Boolean> checkLoginElderly(String username, String pin){
        Task<DataSnapshot> elderlyTask = fetchElderlyDB();
        TaskCompletionSource<Boolean> boolSource = new TaskCompletionSource<>();
        Task<Boolean> bool = boolSource.getTask();

        Tasks.whenAll(elderlyTask).addOnCompleteListener(task ->{
            boolean exists = false;
            DataSnapshot snapshot = elderlyTask.getResult();
            if(snapshot.child(username).exists()) {
                if (Objects.equals(snapshot.child(username).child("pin").getValue(), pin)) {
                    exists = true;
                }
            }
            boolSource.setResult(exists);
        });
        return bool;
    }

    public Task<Boolean> checkLoginCaregiver(String username, String password){
        Task<DataSnapshot> caregiverTask = fetchCaregiverDB();
        TaskCompletionSource<Boolean> boolSource = new TaskCompletionSource<>();
        Task<Boolean> bool = boolSource.getTask();

        Tasks.whenAll(caregiverTask).addOnCompleteListener(task ->{
            boolean exists = false;
            DataSnapshot snapshot = caregiverTask.getResult();
            if(snapshot.child(username).exists()) {
                if (Objects.equals(snapshot.child(username).child("password").getValue(), password)) {
                    exists = true;
                }
            }
            boolSource.setResult(exists);
        });
        return bool;
    }

    public Task<List<ElderlyEntry>> ElderlyList(String caregiverusername){
        TaskCompletionSource<List<ElderlyEntry>> listTaskSource = new TaskCompletionSource<>();
        Task<List<ElderlyEntry>> listTask = listTaskSource.getTask();
        Task<DataSnapshot> elderlyDBTask = fetchElderlyDB();
        Task<DataSnapshot> caregiverTask = fetchCaregiver(caregiverusername);

        Tasks.whenAll(elderlyDBTask, caregiverTask).addOnCompleteListener(task -> {
            CaregiverEntry caregiver = caregiverTask.getResult().getValue(CaregiverEntry.class);
            DataSnapshot elderlyDB = elderlyDBTask.getResult();
            List<ElderlyEntry> elderlyList = new ArrayList<>();
            if(caregiver.getElderly() != null){
                for(String e_username : caregiver.getElderly().keySet()){
                    if(elderlyDB.child(e_username).exists()){
                        elderlyList.add(elderlyDB.child(e_username).getValue(ElderlyEntry.class));
                    }
                    else{
                        caregiver.removeElderly(e_username);
                        updateCaregiver(caregiver);
                    }
                }
                listTaskSource.setResult(elderlyList);
            }
        });
        return listTask;
    }

    public Task<List<CaregiverEntry>> CaregiverList(String elderlyusername){
        TaskCompletionSource<List<CaregiverEntry>> listTaskSource = new TaskCompletionSource<>();
        Task<List<CaregiverEntry>> listTask = listTaskSource.getTask();
        Task<DataSnapshot> caregiverDBTask = fetchCaregiverDB();
        Task<DataSnapshot> elderlyTask = fetchElderly(elderlyusername);

        Tasks.whenAll(caregiverDBTask, elderlyTask).addOnCompleteListener(task -> {
            ElderlyEntry elderly = elderlyTask.getResult().getValue(ElderlyEntry.class);
            DataSnapshot caregiverDB = caregiverDBTask.getResult();
            List<CaregiverEntry> caregiverList = new ArrayList<>();
            if(elderly.getCaregivers() != null){
                for(String c_username : elderly.getCaregivers().keySet()){
                    if(caregiverDB.child(c_username).exists()){
                        caregiverList.add(caregiverDB.child(c_username).getValue(CaregiverEntry.class));
                    }
                    else {
                        elderly.removeCaregiver(c_username);
                        updateElderly(elderly);
                    }
                }

            }
            listTaskSource.setResult(caregiverList);
        });
        return listTask;
    }
    public Task<List<MealEntry>> MealPlanList(String elderlyusername){
        TaskCompletionSource<List<MealEntry>> listTaskSource = new TaskCompletionSource<>();
        Task<List<MealEntry>> listTask = listTaskSource.getTask();
        Task<DataSnapshot> mealPlanTask = fetchMealPlan(elderlyusername);

        Tasks.whenAll(mealPlanTask).addOnCompleteListener(task -> {
            DataSnapshot mealPlanDB = mealPlanTask.getResult();
            MealEntry meal;
            List<MealEntry> mealList = new ArrayList<>();
            if (mealPlanDB != null && mealPlanDB.hasChildren()) {
                for(DataSnapshot mealPlans : mealPlanDB.getChildren()){
                    if(mealPlans.hasChildren()){
                        for(DataSnapshot mealPlan : mealPlans.getChildren()){
                            meal = mealPlan.getValue(MealEntry.class);
                            mealList.add(meal);
                        }
                    }

                }
            }


            listTaskSource.setResult(mealList);
        });
        return listTask;
    }
    public Task<DataSnapshot> assignElderly(String caregiver_username, String caregiver_name, String elderly_username, String elderly_name){
        Task<DataSnapshot> elderlyDBTask = fetchElderlyDB();

        TaskCompletionSource<DataSnapshot> caregiverListTaskSource = new TaskCompletionSource<>();
        Task<DataSnapshot> caregiverListTask = caregiverListTaskSource.getTask();
        Tasks.whenAll(elderlyDBTask).addOnCompleteListener(task -> {
            DataSnapshot elderlyDB = elderlyDBTask.getResult();
            if(elderlyDB.child(elderly_username).exists()){
                caregiverRef.child(caregiver_username).child("elderly").child(elderly_username).setValue(elderly_name);
                elderlyRef.child(elderly_username).child("caregivers").child(caregiver_username).setValue(caregiver_name);
            }
            Task<DataSnapshot> caregiverTask = fetchCaregiver(caregiver_username);
            Tasks.whenAll(caregiverTask).addOnCompleteListener(task2 -> {
                DataSnapshot caregiver = caregiverTask.getResult();
                caregiverListTaskSource.setResult(caregiver.child("elderly"));
            });
        });
        return caregiverListTask;
    }
    public void removeElderly(String caregiver, String elderly){
        caregiverRef.child(caregiver).child("elderly").child(elderly).removeValue();
        elderlyRef.child(elderly).child("caregivers").child(caregiver).removeValue();
    }

    public void deleteElderly(String elderly_username){

        Task<List<CaregiverEntry>> caregiverListTask = CaregiverList(elderly_username);

        Tasks.whenAll(caregiverListTask).addOnCompleteListener(task -> {
            List<CaregiverEntry> caregiverList = caregiverListTask.getResult();
            for(CaregiverEntry caregiver : caregiverList){
                removeElderly(caregiver.getUsername(), elderly_username);
            }
            elderlyRef.child(elderly_username).removeValue();
            mealPlanRef.child(elderly_username).removeValue();
        });
    }

}
