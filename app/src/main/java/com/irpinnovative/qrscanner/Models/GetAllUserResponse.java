package com.irpinnovative.qrscanner.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetAllUserResponse {
    @Expose
    @SerializedName("status")
    public boolean status;
    @Expose
    @SerializedName("message")
    public String message;
    @Expose
    @SerializedName("data")
    public ArrayList<GetAllUserData> data;
}
