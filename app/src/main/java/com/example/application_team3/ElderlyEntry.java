package com.example.application_team3;

import java.util.List;

public class ElderlyEntry {
    String name, pid, phoneNo;
    int pin;
    String dob, country, city;

    public ElderlyEntry(String name, String pid, int pin, String phoneNo, String dob, String country, String city) {
        this.name = name;
        this.pid = pid;
        this.pin = pin;
        this.phoneNo = phoneNo;
        this.dob = dob;
        this.country = country;
        this.city = city;
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


}
