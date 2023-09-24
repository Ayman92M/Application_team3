package com.example.application_team3;

import java.util.LinkedList;
import java.util.List;

public class CaregiverEntry {
    private String name;
    private String pid;
    private String password;
    private String phoneNo;
    private String birthday;
    private String address;

    private CaregiverEntry() {}
    public CaregiverEntry(String name, String pid, String password, String phoneNo) {
        this.name = name;
        this.pid = pid;
        this.password = password;
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

}
