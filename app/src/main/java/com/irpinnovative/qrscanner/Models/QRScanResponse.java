package com.irpinnovative.qrscanner.Models;

import com.google.gson.annotations.SerializedName;

public class QRScanResponse{

	@SerializedName("userData")
	private QRScanResponseData userData;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private boolean status;

	public void setUserData(QRScanResponseData userData){
		this.userData = userData;
	}

	public QRScanResponseData getUserData(){
		return userData;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setStatus(boolean status){
		this.status = status;
	}

	public boolean isStatus(){
		return status;
	}
}