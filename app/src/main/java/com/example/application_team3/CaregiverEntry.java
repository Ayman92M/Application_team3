package com.example.application_team3;

import java.util.List;

public class CaregiverEntry {
    String name, pid, password, phoneNo;
    String dob, country, city;

    public CaregiverEntry(String name, String pid, String password, String phoneNo, String dob, String country, String city) {
        this.name = name;
        this.pid = pid;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
