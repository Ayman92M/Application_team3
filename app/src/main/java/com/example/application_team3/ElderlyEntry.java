package com.example.application_team3;

import java.io.Serializable;
import java.util.HashMap;

public class ElderlyEntry implements Serializable{
    private String name;
    private String username;
    private String phoneNo;
    private String pin;
    private String birthday;
    private String address;

    HashMap<String, String> caregivers = new HashMap<>();

    private ElderlyEntry() {}
    public ElderlyEntry(String name, String username, String pin, String phoneNo) {
        this.name = name;
        this.username = username;
        this.pin = pin;
        this.phoneNo = phoneNo;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public HashMap<String, String> getCaregivers() {
        return caregivers;
    }
    public void setCaregiver(HashMap<String, String> caregivers){
        this.caregivers = caregivers;
    }

    public void addCaregiver(String caregiver_pid, String caregiver_name) {
        this.caregivers.put(caregiver_pid, caregiver_name);
    }

    public void removeCaregiver(String caregiver){
        this.caregivers.remove(caregiver);
    }

}
