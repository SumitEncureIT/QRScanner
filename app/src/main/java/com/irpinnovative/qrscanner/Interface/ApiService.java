package com.irpinnovative.qrscanner.Interface;

import com.google.gson.JsonObject;
import com.irpinnovative.qrscanner.Models.GetAllUserResponse;
import com.irpinnovative.qrscanner.Models.QRScanResponse;
import com.irpinnovative.qrscanner.Models.UserAcceptResponse;
import com.irpinnovative.qrscanner.Models.UserRegistrationResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {

    @POST("scanQr")
    Call<QRScanResponse> getScanData(@Body JsonObject jsonObject);

    @POST("reg")
    Call<UserRegistrationResponse> getUserRegister(@Body JsonObject jsonObject);

    @POST("acceptQr")
    Call<UserAcceptResponse> getUserAccept(@Body JsonObject jsonObject);

    @GET("getRegisteredUserData")
    Call<GetAllUserResponse> getAllUsers();


}



