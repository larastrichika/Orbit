package com.shreebrahmanitravels.app.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class UserEntity {

    @PrimaryKey
    @NonNull
    private String username;

    private String name;
    private String password;
    private String phoneNo;
    private String bus_no;
    private String branch;
    private String currentyear;
    private String stand;
    private String pendingfees;
    private String lastbooked_date;

    public UserEntity(@NonNull String username, String name, String password, String phoneNo,
                      String bus_no, String branch, String currentyear, String stand, String pendingfees) {
        this.username = username;
        this.name = name;
        this.password = password;
        this.phoneNo = phoneNo;
        this.bus_no = bus_no;
        this.branch = branch;
        this.currentyear = currentyear;
        this.stand = stand;
        this.pendingfees = pendingfees;
    }

    @NonNull
    public String getUsername() { return username; }
    public void setUsername(@NonNull String username) { this.username = username; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPhoneNo() { return phoneNo; }
    public void setPhoneNo(String phoneNo) { this.phoneNo = phoneNo; }

    public String getBus_no() { return bus_no; }
    public void setBus_no(String bus_no) { this.bus_no = bus_no; }

    public String getBranch() { return branch; }
    public void setBranch(String branch) { this.branch = branch; }

    public String getCurrentyear() { return currentyear; }
    public void setCurrentyear(String currentyear) { this.currentyear = currentyear; }

    public String getStand() { return stand; }
    public void setStand(String stand) { this.stand = stand; }

    public String getPendingfees() { return pendingfees; }
    public void setPendingfees(String pendingfees) { this.pendingfees = pendingfees; }

    public String getLastbooked_date() { return lastbooked_date; }
    public void setLastbooked_date(String lastbooked_date) { this.lastbooked_date = lastbooked_date; }
}
