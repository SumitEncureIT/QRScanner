package com.irpinnovative.qrscanner.Listener;


import com.irpinnovative.qrscanner.Models.QRScanResponse;
import com.irpinnovative.qrscanner.Models.UserAcceptResponse;
import com.irpinnovative.qrscanner.Models.UserRegistrationResponse;

public class AppListener {

    public interface OnUserQRScanListener{
        void onSuccess(QRScanResponse qrScanResponse);
        void onFailure(String message);
    }

    public interface OnUserRegisterListener{
        void onSuccess(UserRegistrationResponse userRegistrationResponse);
        void onFailure(String message);
    }


    public interface OnUserAcceptListener{
        void onSuccess(UserAcceptResponse userAcceptResponse);
        void onFailure(String message);
    }


}
