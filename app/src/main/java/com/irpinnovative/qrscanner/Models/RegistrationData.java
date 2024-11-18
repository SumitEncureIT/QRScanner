package com.irpinnovative.qrscanner.Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.irpinnovative.qrscanner.roomdatabase.TableNames;

@Entity(tableName = TableNames.TABLE_REGISTRATION)
public class RegistrationData {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String fullName;
    private String mobileNo;
    private String emailId;


    public RegistrationData(String fullName, String mobileNo, String emailId) {
        this.fullName = fullName;
        this.mobileNo = mobileNo;
        this.emailId = emailId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }
}
