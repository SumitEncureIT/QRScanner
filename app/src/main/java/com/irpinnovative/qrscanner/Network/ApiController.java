package com.irpinnovative.qrscanner.Network;

import android.content.Context;

import com.google.gson.JsonObject;
import com.irpinnovative.qrscanner.Interface.ApiService;
import com.irpinnovative.qrscanner.Listener.AppListener;
import com.irpinnovative.qrscanner.Models.QRScanResponse;
import com.irpinnovative.qrscanner.Models.UserAcceptResponse;
import com.irpinnovative.qrscanner.Models.UserRegistrationResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ApiController {
    private Context context;
    private ApiService apiService;


    public ApiController(Context context) {
        this.context = context;
        apiService = RetrofitClient.getInterface();

    }

    public void getQRScan(JsonObject jsonObject, final AppListener.OnUserQRScanListener onUserQRScanListener){
        apiService.getScanData(jsonObject)
                .enqueue(new Callback<QRScanResponse>() {
                    @Override
                    public void onResponse(Call<QRScanResponse> call, Response<QRScanResponse> response) {
                        if (response.isSuccessful()){
                            if (response.body().isStatus()){
                                onUserQRScanListener.onSuccess(response.body());
                            }else {
                                onUserQRScanListener.onFailure(response.body().getMessage());
                            }
                        }else {
                            onUserQRScanListener.onFailure("Something went wrong");
                        }
                    }

                    @Override
                    public void onFailure(Call<QRScanResponse> call, Throwable t) {
                        onUserQRScanListener.onFailure(t.getMessage());
                    }
                });
    }

    public void geUserRegister(JsonObject jsonObject, final AppListener.OnUserRegisterListener onUserRegisterListener){
        apiService.getUserRegister(jsonObject)
                .enqueue(new Callback<UserRegistrationResponse>() {
                    @Override
                    public void onResponse(Call<UserRegistrationResponse> call, Response<UserRegistrationResponse> response) {
                        if (response.isSuccessful()){
                            if (response.body().isStatus()){
                                onUserRegisterListener.onSuccess(response.body());
                            }else {
                                onUserRegisterListener.onFailure(response.body().getMessage());
                            }
                        }else {
                            onUserRegisterListener.onFailure("Something went wrong");
                        }
                    }

                    @Override
                    public void onFailure(Call<UserRegistrationResponse> call, Throwable t) {
                        onUserRegisterListener.onFailure(t.getMessage());
                    }
                });
    }


    public void geUserAccept(JsonObject jsonObject, final AppListener.OnUserAcceptListener onUserAcceptListener){
        apiService.getUserAccept(jsonObject)
                .enqueue(new Callback<UserAcceptResponse>() {
                    @Override
                    public void onResponse(Call<UserAcceptResponse> call, Response<UserAcceptResponse> response) {
                        if (response.isSuccessful()){
                            if (response.body().isStatus()){
                                onUserAcceptListener.onSuccess(response.body());
                            }else {
                                onUserAcceptListener.onFailure(response.body().getMessage());
                            }
                        }else {
                            onUserAcceptListener.onFailure("Something went wrong");
                        }
                    }

                    @Override
                    public void onFailure(Call<UserAcceptResponse> call, Throwable t) {
                        onUserAcceptListener.onFailure(t.getMessage());
                    }
                });
    }

}
