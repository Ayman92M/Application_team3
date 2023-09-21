package com.example.application_team3;

import java.util.LinkedList;
import java.util.List;

public class ElderlyEntry {
    String name, pid, phoneNo;
    int pin;
    String birthday, address;

    List<String> caregivers = new LinkedList<>();

    public ElderlyEntry(String name, String pid, int pin, String phoneNo) {
        this.name = name;
        this.pid = pid;
        this.pin = pin;
        this.phoneNo = phoneNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
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

    public List<String> getCaregivers() {
        return caregivers;
    }

    public void addCaregiver(String caregiver) {
        this.caregivers.add(caregiver);
    }

    public void removeCaregiver(String caregiver){
        this.caregivers.remove(caregiver);
    }

}
