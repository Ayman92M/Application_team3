package com.example.application_team3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TaskCompletionSource<Boolean> testLoginElderlyTaskSource;
    private TaskCompletionSource<Boolean> testLoginTaskSource;
    private TaskCompletionSource<Boolean> testGetElderlyListTaskSource;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        testLoginElderlyTaskSource = new TaskCompletionSource<>();
        testLoginTaskSource = new TaskCompletionSource<>();
        testGetElderlyListTaskSource = new TaskCompletionSource<>();

        Task<Boolean> task1 = testLoginElderlyTaskSource.getTask();
        Task<Boolean> task2 = testLoginTaskSource.getTask();
        Task<Boolean> task3 = testGetElderlyListTaskSource.getTask();
        System.out.println("STARTSTARTSTART");
        testLoginElderly();

        Tasks.whenAll(task1).addOnCompleteListener(task -> testLogin());

        Tasks.whenAll(task1, task2).addOnCompleteListener(task -> testGetElderlyList());

        Tasks.whenAll(task1, task2, task3).addOnCompleteListener(task -> System.out.println("ENDENDEND"));

    }

    private void testLoginElderly(){
        Database db = new Database();
        Task<Boolean> task1 = db.checkLoginElderly("asd12", "1234");
        Task<Boolean> task2 = db.checkLoginElderly("asd12", "1235");
        Task<Boolean> task3 = db.checkLoginElderly("eee12", "1234");
        Task<Boolean> task4 = db.checkLoginElderly("e12ee", "1234");
        Task<Boolean> task5 = db.checkLoginElderly("eld", "1234");

        Tasks.whenAll(task1, task2, task3, task4, task5)
                .addOnCompleteListener(task ->
                {
                    System.out.println("1. asd12 - 1234: " + task1.getResult());
                    System.out.println("2. asd12 - 1235: " + task2.getResult());
                    System.out.println("3. eee12 - 1234: " + task3.getResult());
                    System.out.println("4. e12ee - 1234: " + task4.getResult());
                    System.out.println("5. eld   - 1234: " + task5.getResult());
                    testLoginElderlyTaskSource.setResult(true);
                });
    }

    private void testLogin(){
        String pid = "ggg12";
        String pass = "123456Aa";

        Database db = new Database();

        Task<DataSnapshot> caregiverDB = db.fetchCaregiverDB();
        Task<Boolean> loginCheck =
                db.checkLoginCaregiver(pid, pass);
        TaskCompletionSource<CaregiverEntry> caregiverSrc =
                new TaskCompletionSource<>();
        Task<CaregiverEntry> caregiverTask = caregiverSrc.getTask();

        Tasks.whenAll(loginCheck, caregiverDB).addOnCompleteListener(task -> {
            if(loginCheck.getResult()){
                System.out.println("LOGIN SUCCESS");
                caregiverSrc.setResult(caregiverDB.getResult().child(pid)
                        .getValue(CaregiverEntry.class));
            }
            else{
                System.out.println("LOGIN FAILURE");
                caregiverSrc.setResult(null);
            }
        });

        Tasks.whenAll(caregiverTask).addOnCompleteListener(task -> {
            CaregiverEntry caregiver = caregiverTask.getResult();
            if (caregiver != null){
                System.out.println(caregiver.getName() + ": " + caregiver.getPid());
                System.out.println(caregiver.getElderly().keySet());
            }
            testLoginTaskSource.setResult(true);
        });
    }

    public void testGetElderlyList(){
        Database db = new Database();
        String pid = "ggg12";
        Task<DataSnapshot> caregiverTask = db.fetchCaregiver(pid);
        Task<DataSnapshot> elderlyDBTask = db.fetchElderlyDB();
        List<ElderlyEntry> elderlyList = new ArrayList<>();

        Tasks.whenAll(caregiverTask, elderlyDBTask).addOnCompleteListener(task -> {
            CaregiverEntry caregiver = caregiverTask.getResult()
                    .getValue(CaregiverEntry.class);
            DataSnapshot elderlyData = elderlyDBTask.getResult();

            for(String elderlyPid : caregiver.getElderly().keySet()){
                if(elderlyData.child(elderlyPid).exists()){
                    elderlyList.add(elderlyData.child(elderlyPid)
                            .getValue(ElderlyEntry.class));
                }
            }

            System.out.println(elderlyList.get(0).getName());
            testGetElderlyListTaskSource.setResult(true);
        });
    }
}