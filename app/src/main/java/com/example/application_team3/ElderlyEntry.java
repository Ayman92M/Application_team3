package com.example.application_team3;

import java.util.List;

public class ElderlyEntry {
    String name, pid;
    int pin, phoneNo;
    String dob, country, city;
    List<String> caregivers;

    public ElderlyEntry(String name, String pid, int pin, int phoneNo, String dob, String country, String city, List<String> caregivers) {
        this.name = name;
        this.pid = pid;
        this.pin = pin;
        this.phoneNo = phoneNo;
        this.dob = dob;
        this.country = country;
        this.city = city;
        this.caregivers = caregivers;
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

    public int getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(int phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<String> getCaregivers() {
        return caregivers;
    }

    public void setCaregivers(List<String> caregivers) {
        this.caregivers = caregivers;
    }



}
