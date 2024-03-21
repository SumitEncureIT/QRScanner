package com.irpinnovative.qrscanner.viewModel;


import android.util.Log;

import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;
import com.irpinnovative.qrscanner.Interface.ApiService;
import com.irpinnovative.qrscanner.Models.GetAllUserResponse;
import com.irpinnovative.qrscanner.Models.UserRegistrationResponse;
import com.irpinnovative.qrscanner.Network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends ViewModel {
    public ObservableField<String> userName= new ObservableField<>();
    public ObservableField<String> userMobile= new ObservableField<>();
    public ObservableField<String> userEmail= new ObservableField<>();
    private MutableLiveData<UserRegistrationResponse> getRegister = new MutableLiveData<>();

    // Get all users data
    private MutableLiveData<GetAllUserResponse> getAllUSers = new MutableLiveData<>();
    private MutableLiveData<String> errorList = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private ApiService retrofitService; // Assume you have a Retrofit service interface

    public MainViewModel() {
        // Initialize your Retrofit service
        retrofitService = RetrofitClient.getInterface();
    }
    public LiveData<Boolean> isLoading() {
        return isLoading;
    }

    public void userRegistration() {
        isLoading.setValue(true);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name",userName.get());
        jsonObject.addProperty("mobile_no",userMobile.get());
        jsonObject.addProperty("email_id",userEmail.get());
        Log.e("TAG", "getRegister: "+jsonObject);
        if (isValidateData()) {
            Call<UserRegistrationResponse> call = retrofitService.getUserRegister(jsonObject);
            call.enqueue(new Callback<UserRegistrationResponse>() {
                @Override
                public void onResponse(Call<UserRegistrationResponse> call, Response<UserRegistrationResponse> response) {
                    Log.e("TAG", "onResponse: "+response);
                    isLoading.setValue(false);
                    if (response.code() == 200) {
                        // Update LiveData with response body
                        if (response.body() != null && response.body().isStatus()) {
                            getRegister.setValue(response.body());
                        } else {
                            errorList.setValue(response.message());
                        }
                    } else {
                        // Handle error
                        // You can set an error message or handle the error in any way you want
                        errorList.setValue("Invalid Response from server");
                    }
                }

                @Override
                public void onFailure(Call<UserRegistrationResponse> call, Throwable t) {
                    errorList.setValue("Invalid Response from server failure");
                    isLoading.setValue(false);
                }
            });
        } else {
            isLoading.setValue(false);
//            errorList.setValue("Empty Category Id");
        }
    }


    public void getAllUsers() {
        isLoading.setValue(true);
//        if (isValidateData()) {
            Call<GetAllUserResponse> call = retrofitService.getAllUsers();
            call.enqueue(new Callback<GetAllUserResponse>() {
                @Override
                public void onResponse(Call<GetAllUserResponse> call, Response<GetAllUserResponse> response) {
                    Log.e("TAG", "onResponse: "+response);
                    isLoading.setValue(false);
                    if (response.code() == 200) {
                        // Update LiveData with response body
                        if (response.body() != null && response.body().status) {
                            getAllUSers.setValue(response.body());
                        } else {
                            errorList.setValue(response.message());
                        }
                    } else {
                        // Handle error
                        // You can set an error message or handle the error in any way you want
                        errorList.setValue("Invalid Response from server");
                    }
                }

                @Override
                public void onFailure(Call<GetAllUserResponse> call, Throwable t) {
                    errorList.setValue("Invalid Response from server failure");
                    isLoading.setValue(false);
                }
            });
//        }
//        else {
//            isLoading.setValue(false);
////            errorList.setValue("Empty Category Id");
//        }
    }

    private boolean isValidateData() {
        if (userName == null || userName.get() == null){
            errorList.setValue("Enter your full name");
            return false;
        }else  if (userMobile == null || userMobile.get() == null){
            errorList.setValue("Enter your mobile no");
            return false;
        }else  if (userMobile == null || userMobile.get().length() < 10){
            errorList.setValue("Enter your valid mobile no");
            return false;
        }
        return true;
    }

    public MutableLiveData<UserRegistrationResponse> getUserRegisterResponse() {
        return getRegister;
    }

    public MutableLiveData<GetAllUserResponse> getAllUsersResponse() {
        return getAllUSers;
    }

    public MutableLiveData<String> getErrorList() {
        return errorList;
    }
}



