package com.irpinnovative.qrscanner.Models;

import com.google.gson.annotations.SerializedName;

public class QRScanResponseData {

	@SerializedName("email_id")
	private String emailId;

	@SerializedName("will_you_be_attend")
	private String willYouBeAttend;

	@SerializedName("is_present")
	private String isPresent;

	@SerializedName("comments")
	private String comments;

	@SerializedName("qr_url")
	private String qrUrl;

	@SerializedName("name")
	private String name;

	@SerializedName("mobile_no")
	private String mobileNo;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id")
	private String id;

	@SerializedName("ques_answer")
	private String quesAnswer;

	public void setEmailId(String emailId){
		this.emailId = emailId;
	}

	public String getEmailId(){
		return emailId;
	}

	public void setWillYouBeAttend(String willYouBeAttend){
		this.willYouBeAttend = willYouBeAttend;
	}

	public String getWillYouBeAttend(){
		return willYouBeAttend;
	}

	public void setIsPresent(String isPresent){
		this.isPresent = isPresent;
	}

	public String getIsPresent(){
		return isPresent;
	}

	public void setComments(String comments){
		this.comments = comments;
	}

	public String getComments(){
		return comments;
	}

	public void setQrUrl(String qrUrl){
		this.qrUrl = qrUrl;
	}

	public String getQrUrl(){
		return qrUrl;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setMobileNo(String mobileNo){
		this.mobileNo = mobileNo;
	}

	public String getMobileNo(){
		return mobileNo;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setQuesAnswer(String quesAnswer){
		this.quesAnswer = quesAnswer;
	}

	public String getQuesAnswer(){
		return quesAnswer;
	}
}