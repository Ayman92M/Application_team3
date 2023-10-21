package com.example.application_team3;

import java.io.Serializable;
import java.util.HashMap;


public class CaregiverEntry implements Serializable {
    private String name;
    private String username;
    private String password;
    private String phoneNo;
    private String birthday;
    private String address;
    private HashMap<String, String> elderly = new HashMap<>();

    private CaregiverEntry() {}
    public CaregiverEntry(String name, String username, String password, String phoneNo) {
        this.name = name;
        this.username = username;
        this.password = password;
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

    public HashMap<String, String> getElderly(){
        return elderly;
    }
    public void setElderly(HashMap<String, String> elderly){
        this.elderly = elderly;
    }

    public void addElderly(String pid, String name){
        elderly.put(pid, name);
    }

    public void removeElderly(String pid){
        elderly.remove(pid);
    }

}
