package com.irpinnovative.qrscanner.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.irpinnovative.qrscanner.roomdatabase.TableNames;

@Entity(tableName = TableNames.TABLE_GETALLUSERDATA)
public class GetAllUserData {

    @PrimaryKey(autoGenerate = true)
    public int userid;

    @Expose
    @SerializedName("id")
    private String server_id;
    @Expose
    @SerializedName("name")
    private String name;
    @Expose
    @SerializedName("email_id")
    private String email_id;
    @Expose
    @SerializedName("will_you_be_attend")
    private String will_you_be_attend;
    @Expose
    @SerializedName("ques_answer")
    private String ques_answer;
    @Expose
    @SerializedName("comments")
    private String comments;
    @Expose
    @SerializedName("mobile_no")
    private String mobile_no;
    @Expose
    @SerializedName("qr_url")
    private String qr_url;
    @Expose
    @SerializedName("is_present")
    private String is_present;
    @Expose
    @SerializedName("send_whats_app_msg")
    private String send_whats_app_msg;
    @Expose
    @SerializedName("created_at")
    private String created_at;

    public int getId() {
        return userid;
    }

    public void setId(int id) {
        this.userid = id;
    }

    public String getServer_id() {
        return server_id;
    }

    public void setServer_id(String server_id) {
        this.server_id = server_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getWill_you_be_attend() {
        return will_you_be_attend;
    }

    public void setWill_you_be_attend(String will_you_be_attend) {
        this.will_you_be_attend = will_you_be_attend;
    }

    public String getQues_answer() {
        return ques_answer;
    }

    public void setQues_answer(String ques_answer) {
        this.ques_answer = ques_answer;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getQr_url() {
        return qr_url;
    }

    public void setQr_url(String qr_url) {
        this.qr_url = qr_url;
    }

    public String getIs_present() {
        return is_present;
    }

    public void setIs_present(String is_present) {
        this.is_present = is_present;
    }

    public String getSend_whats_app_msg() {
        return send_whats_app_msg;
    }

    public void setSend_whats_app_msg(String send_whats_app_msg) {
        this.send_whats_app_msg = send_whats_app_msg;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
