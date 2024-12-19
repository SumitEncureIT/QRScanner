package com.irpinnovative.qrscanner;

import android.Manifest;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;


public class ScannerViewActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Dexter.withContext(getApplicationContext())
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
//                        ScanOptions options = new ScanOptions();
//                        options.setPrompt("");
//                        options.setBeepEnabled(false);
////                        options.setTorchEnabled(true);
//                        options.setOrientationLocked(true);
//                        options.setCameraId(0);
//                        options.setCaptureActivity(CaptureAct.class);
//                        barcodeLauncher.launch(options);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                    permissionToken.continuePermissionRequest();
                    }
                }).check();
    }
    // Register the launcher and result handler
    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if(result.getContents() == null) {
                    Toast.makeText(ScannerViewActivity.this, "Scan failed.", Toast.LENGTH_LONG).show();
                    onBackPressed();
                } else {
//                    Toast.makeText(ScannerViewActivity.this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                    MainActivity.binding.qrValue.setText(result.getContents());
                    onBackPressed();
                }
            });



    @Override
    protected void onResume() {
        super.onResume();

    }
}