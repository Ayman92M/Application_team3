package com.example.application_team3;

import java.util.List;

public class CaregiverEntry {
    String name, pid;
    int password, phoneNo;
    List<String> elderly;

    String dob, country, city;

    public CaregiverEntry(String name, String pid, int password, int phoneNo, List<String> elderly, String dob, String country, String city) {
        this.name = name;
        this.pid = pid;
        this.password = password;
        this.phoneNo = phoneNo;
        this.elderly = elderly;
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

    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
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

    public List<String> getElderly() {
        return elderly;
    }

    public void setElderly(List<String> elderly) {
        this.elderly = elderly;
    }

}
