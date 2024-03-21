package com.irpinnovative.qrscanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.irpinnovative.qrscanner.Listener.AppListener;
import com.irpinnovative.qrscanner.Models.GetAllUserData;
import com.irpinnovative.qrscanner.Models.GetAllUserResponse;
import com.irpinnovative.qrscanner.Models.QRScanResponse;
import com.irpinnovative.qrscanner.Models.QRScanResponseData;
import com.irpinnovative.qrscanner.Models.UserAcceptResponse;
import com.irpinnovative.qrscanner.Models.UserRegistrationResponse;
import com.irpinnovative.qrscanner.Network.ApiController;
import com.irpinnovative.qrscanner.Network.NetworkUtils;
import com.irpinnovative.qrscanner.databinding.ActivityMainBinding;
import com.irpinnovative.qrscanner.databinding.RegisterLayoutBinding;
import com.irpinnovative.qrscanner.roomdatabase.DatabaseUtil;
import com.irpinnovative.qrscanner.viewModel.MainViewModel;

public class MainActivity extends AppCompatActivity {
    public static ActivityMainBinding binding;
    private Dialog dialog, dataDialog;
    private ApiController apiController;
    private MainViewModel viewModel;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        apiController = new ApiController(this);
        progressDialog = new ProgressDialog(this);
        binding.qrScan.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), ScannerViewActivity.class));
        });
        binding.btnSubmit.setOnClickListener(v -> {
            if (binding.qrValue.getText().toString().equalsIgnoreCase("")){
                Toast.makeText(this, "Scan QR Code", Toast.LENGTH_SHORT).show();
            }else {
                if (NetworkUtils.isConnectedToInternet(this)) {
                    getScanData(binding.qrValue.getText().toString());
                }else {
                    Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
                }

            }
        });
        viewModel.isLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if( aBoolean ){
                    //Start loader
                    progressDialog.setMessage("Loading");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                }else {
                    // Stop loader
                    progressDialog.dismiss();
                }
            }
        });

        viewModel.getErrorList().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnSync.setOnClickListener(v -> {
            if (NetworkUtils.isConnectedToInternet(this)) {
                viewModel.getAllUsersResponse().observe(this, new Observer<GetAllUserResponse>() {
                    @Override
                    public void onChanged(GetAllUserResponse getAllUserResponse) {
                        if (getAllUserResponse.status){
                            Toast.makeText(MainActivity.this,"Get All Users Data!!!", Toast.LENGTH_SHORT).show();
                            DatabaseUtil.on().getGetAllUserDataDao().nukeTable();
                            DatabaseUtil.on().getGetAllUserDataDao().insertBulk(getAllUserResponse.data);
                        }else {
                            Toast.makeText(MainActivity.this, getAllUserResponse.message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                viewModel.getAllUsers();
            }else {
                Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
            }

        });



        binding.btnAddManually.setOnClickListener(v -> {
            dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            RegisterLayoutBinding binding1 = RegisterLayoutBinding.inflate(getLayoutInflater());
            binding1.setMainViewModel(viewModel);
            dialog.setContentView(binding1.getRoot());
            dialog.setCanceledOnTouchOutside(false);
            ImageView close = dialog.findViewById(R.id.close);




            if (NetworkUtils.isConnectedToInternet(this)) {
                viewModel.getUserRegisterResponse().observe(this, new Observer<UserRegistrationResponse>() {
                    @Override
                    public void onChanged(UserRegistrationResponse userRegistrationResponse) {
                        if(userRegistrationResponse.isStatus()){
                            Toast.makeText(MainActivity.this, userRegistrationResponse.getMessage().toString(), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
//                            viewModel.getAllUsers();
                        }else {
                            Toast.makeText(MainActivity.this, userRegistrationResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }else {
                Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
            }

//            EditText fullName = dialog.findViewById(R.id.user_full_name);
//            EditText mobileNo = dialog.findViewById(R.id.user_contact_no);
//            EditText email = dialog.findViewById(R.id.user_email);
//            Button btnRegister = dialog.findViewById(R.id.btn_register);
            close.setOnClickListener(view -> {
                dialog.dismiss();
            });
//            btnRegister.setOnClickListener(view -> {
//                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
//                if (fullName.getText().toString().equalsIgnoreCase("")){
//                    fullName.requestFocus();
//                    fullName.setError("Enter full name");
//                }else if (mobileNo.getText().toString().equalsIgnoreCase("")){
//                    mobileNo.requestFocus();
//                    mobileNo.setError("Enter mobile no");
//                }else if (mobileNo.getText().toString().length()<10){
//                    mobileNo.requestFocus();
//                    mobileNo.setError("Enter valid mobile no");
//                }
////                else if (email.getText().toString().equalsIgnoreCase("")){
////                    email.requestFocus();
////                    email.setError("Enter email id");
////                }else if (!email.getText().toString().matches(emailPattern)){
////                    email.requestFocus();
////                    email.setError("Enter your valid email id");
////                }
//                else {
//                    getRegister(fullName.getText().toString(),mobileNo.getText().toString(),email.getText().toString());
//                }
//            });



            dialog.show();
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dialog.getWindow().setGravity(Gravity.BOTTOM);

        });

    }

    private void getScanData(String mobileNo){
        GetAllUserData data = DatabaseUtil.on().checkMobileNumber(mobileNo);
        if(data != null) {
             if (data.getIs_present().equalsIgnoreCase("Yes")){
                 Toast.makeText(this, "User Already Scanned", Toast.LENGTH_SHORT).show();
             }else {
                 setUserData(data);
                 JsonObject jsonObject = new JsonObject();
                 jsonObject.addProperty("mobile_no",mobileNo);
                 apiController.getQRScan(jsonObject, new AppListener.OnUserQRScanListener() {
                     @Override
                     public void onSuccess(QRScanResponse qrScanResponse) {
                         if (qrScanResponse.isStatus()){
                             Toast.makeText(MainActivity.this, qrScanResponse.getMessage(), Toast.LENGTH_SHORT).show();
//                            setUserData(qrScanResponse.getUserData());
                             binding.qrValue.setText("");
                         }else {
                             Toast.makeText(MainActivity.this, qrScanResponse.getMessage(), Toast.LENGTH_SHORT).show();
                             binding.qrValue.setText("");
                         }
                     }

                     @Override
                     public void onFailure(String message) {
                         Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                         binding.qrValue.setText("");

                     }
                 });
             }

         } else {

         }

//

    }

    private void setUserData(GetAllUserData userData) {
        dataDialog = new Dialog(this);
        dataDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dataDialog.setContentView(R.layout.user_data_layout);
        dataDialog.setCanceledOnTouchOutside(false);
        TextView fullName = dataDialog.findViewById(R.id.user_name);
        TextView mobileNo = dataDialog.findViewById(R.id.user_contact_no);
        TextView email = dataDialog.findViewById(R.id.user_email);
        Button btnAccept = dataDialog.findViewById(R.id.btn_accept);
        Button btnClose = dataDialog.findViewById(R.id.btn_close);
        btnAccept.setOnClickListener(v -> {
            getAccept(userData.getMobile_no());
        });
        btnClose.setOnClickListener(view -> {
            binding.qrValue.setText("");
            dataDialog.dismiss();
        });
        fullName.setText("Name : "+userData.getName());
        mobileNo.setText("Mobile No. : "+userData.getMobile_no());
        email.setText("Email Id : "+userData.getEmail_id());



        dataDialog.show();
        dataDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dataDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dataDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dataDialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void getRegister(String name, String mobile, String email){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name",name);
        jsonObject.addProperty("mobile_no",mobile);
        jsonObject.addProperty("email_id",email);
        Log.e("TAG", "getRegister: "+jsonObject);
        apiController.geUserRegister(jsonObject, new AppListener.OnUserRegisterListener() {
            @Override
            public void onSuccess(UserRegistrationResponse userRegistrationResponse) {
                if (userRegistrationResponse.isStatus()){
                    dialog.dismiss();
                    Toast.makeText(MainActivity.this, userRegistrationResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }else {
                    dialog.dismiss();
                    Toast.makeText(MainActivity.this, userRegistrationResponse.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(String message) {
                dialog.dismiss();
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void getAccept(String mobileNo){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("mobile_no",mobileNo);
        Log.e("TAG", "getAccept: "+jsonObject );
        apiController.geUserAccept(jsonObject, new AppListener.OnUserAcceptListener() {
            @Override
            public void onSuccess(UserAcceptResponse userAcceptResponse) {
                if (userAcceptResponse.isStatus()){
                    dataDialog.dismiss();
                    Toast.makeText(MainActivity.this, userAcceptResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    DatabaseUtil.on().getGetAllUserDataDao().update_User(mobileNo, "Yes");
//                    viewModel.getAllUsers();
                }else {
                    dataDialog.dismiss();
                    Toast.makeText(MainActivity.this, userAcceptResponse.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(String message) {
                dataDialog.dismiss();
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();

            }
        });
    }
    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

}