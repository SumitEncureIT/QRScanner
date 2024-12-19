package com.irpinnovative.qrscanner.viewModel;


import android.util.Log;
import android.widget.Toast;

import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;
import com.irpinnovative.qrscanner.Interface.ApiService;
import com.irpinnovative.qrscanner.Models.GetAllUserResponse;
import com.irpinnovative.qrscanner.Models.RegistrationData;
import com.irpinnovative.qrscanner.Models.UserRegistrationResponse;
import com.irpinnovative.qrscanner.Network.RetrofitClient;
import com.irpinnovative.qrscanner.roomdatabase.DatabaseUtil;
import com.irpinnovative.qrscanner.roomdatabase.dao.GetAllUserDataDao;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends ViewModel {
    private GetAllUserDataDao mGetAllUserDataDao;
    public ObservableField<String> userName= new ObservableField<>();
    public ObservableField<String> userMobile= new ObservableField<>();
    public ObservableField<String> userEmail= new ObservableField<>();
    private MutableLiveData<UserRegistrationResponse> getRegister = new MutableLiveData<>();

    // Get all users data
    private MutableLiveData<GetAllUserResponse> getAllUSers = new MutableLiveData<>();
    private MutableLiveData<String> errorList = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<List<RegistrationData>> allRegistrations = new MutableLiveData<>();

    private final List<RegistrationData> registrations = new ArrayList<>();
    private ApiService retrofitService;

    private MutableLiveData<String> errorMessage = new MutableLiveData<>();
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public MainViewModel() {
        // Initialize your Retrofit service
        retrofitService = RetrofitClient.getInterface();
        mGetAllUserDataDao = DatabaseUtil.on().getGetAllUserDataDao();
//        database = AppDatabase.getInstance(application);
    }
    public LiveData<Boolean> isLoading() {
        return isLoading;
    }


    public LiveData<List<RegistrationData>> getAllRegistrations() {
        return allRegistrations;
    }
    // Insert a registration entry
    public void registerUser() {
        isLoading.setValue(true);

        if (isValidateData()) {
            // Simulate local data storage (e.g., database or list)
            RegistrationData newRegistration = new RegistrationData(userName.get(), userMobile.get(), userEmail.get());
            registrations.add(newRegistration);
            System.out.println("Mobile: " + userMobile.get());
            System.out.println("Email: " + userEmail.get());
            boolean isMobileExists = mGetAllUserDataDao.checkMobileNumber(userMobile.get()) > 0;
            boolean isEmailExists = mGetAllUserDataDao.checkEmailId(userEmail.get()) > 0;
            // Update LiveData
            allRegistrations.setValue(registrations);
            new Thread(() -> {
                try {
                    if (isMobileExists && isEmailExists) {
                        errorMessage.postValue("Mobile and email are already available");
                    } else if (isMobileExists) {
                        errorMessage.postValue("Mobile is already available");
                    } else if (isEmailExists) {
                        errorMessage.postValue("Email is already available");
                    } else {
                        mGetAllUserDataDao.insert(newRegistration);
                        // Fetch updated data
                        List<RegistrationData> updatedData = mGetAllUserDataDao.getAllRegistrationData();
                        allRegistrations.postValue(updatedData);
                        errorMessage.postValue("Data inserted successfully");
                        userName.set(null);
                        userMobile.set(null);
                    }

                } catch (Exception e) {
                    errorMessage.postValue("Error inserting data: " + e.getMessage());
                }
            }).start();


            isLoading.postValue(false);
        }

        isLoading.setValue(false);
    }

    public void userRegistration() {
        isLoading.setValue(true);

        new Thread(() -> {
            try {
                // Fetch all user data from the local database
                List<RegistrationData> localUserDataList = mGetAllUserDataDao.getAllRegistrationData();

                if (localUserDataList == null || localUserDataList.isEmpty()) {
                    errorList.postValue("No data available in the local database to send.");
                    isLoading.postValue(false);
                    return;
                }

                for (RegistrationData userData : localUserDataList) {
                    // Create JSON object for each user
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("name", userData.getFullName());
                    jsonObject.addProperty("mobile_no", userData.getMobileNo());
                    jsonObject.addProperty("email_id", userData.getEmailId());

                    // Log the data being sent for debugging
                    Log.e("TAG", "Sending Data: " + jsonObject);

                    // Call API for each user
                    Call<UserRegistrationResponse> call = retrofitService.getUserRegister(jsonObject);
                    call.enqueue(new Callback<UserRegistrationResponse>() {
                        @Override
                        public void onResponse(Call<UserRegistrationResponse> call, Response<UserRegistrationResponse> response) {
                            if (response.code() == 200 && response.body() != null && response.body().isStatus()) {
                                    new Thread(() -> mGetAllUserDataDao.deleteAll(userData)).start();
                                    errorMessage.postValue(response.body().getMessage());
                            } else {
                                if (response.body().getMessage().equalsIgnoreCase("Mobile no already exist")) {
                                    // If data exists on the server, delete it from the local database
                                    new Thread(() -> {
                                        mGetAllUserDataDao.deleteAll(userData);
                                        errorMessage.postValue("Deleted from local: " + userData.getFullName());
                                    }).start();
                                }
                                // Log failure for specific user
//                                errorMessage.postValue(response.body().getMessage());
                            }
                        }

                        @Override
                        public void onFailure(Call<UserRegistrationResponse> call, Throwable t) {
                            // Log failure
                            errorMessage.postValue(t.toString());
                        }
                    });

                    // Adding a delay between calls to avoid overwhelming the server
                    try {
                        Thread.sleep(1000); // 1-second delay
                    } catch (InterruptedException e) {
                        Log.e("TAG", "Thread interrupted", e);
                    }
                }

                isLoading.postValue(false);
            } catch (Exception e) {
                isLoading.postValue(false);
                errorList.postValue("Error while processing local data: " + e.getMessage());
            }
        }).start();
    }

//    public void userRegistration() {
//        isLoading.setValue(true);
//
//        JsonObject jsonObject = new JsonObject();
//        jsonObject.addProperty("name",userName.get());
//        jsonObject.addProperty("mobile_no",userMobile.get());
//        jsonObject.addProperty("email_id",userEmail.get());
//        Log.e("TAG", "getRegister: "+jsonObject);
//        if (isValidateData()) {
//            Call<UserRegistrationResponse> call = retrofitService.getUserRegister(jsonObject);
//            call.enqueue(new Callback<UserRegistrationResponse>() {
//                @Override
//                public void onResponse(Call<UserRegistrationResponse> call, Response<UserRegistrationResponse> response) {
//                    Log.e("TAG", "onResponse: "+response);
//                    isLoading.setValue(false);
//                    if (response.code() == 200) {
//                        // Update LiveData with response body
//                        if (response.body() != null && response.body().isStatus()) {
//                            userName.set(null);
//                            userMobile.set(null);
//                            userEmail.set(null);
//                            getRegister.setValue(response.body());
//                        } else {
//                            errorList.setValue(response.message());
//                        }
//                    } else {
//                        // Handle error
//                        // You can set an error message or handle the error in any way you want
//                        errorList.setValue("Invalid Response from server");
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<UserRegistrationResponse> call, Throwable t) {
//                    errorList.setValue("Invalid Response from server failure");
//                    isLoading.setValue(false);
//                }
//            });
//        } else {
//            isLoading.setValue(false);
////            errorList.setValue("Empty Category Id");
//        }
//    }


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



