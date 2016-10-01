package com.oxisys.client;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.Manifest.permission;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class Client extends Activity {

    private ImageView barcodeCheckMark;
    private SurfaceView cameraView;
    private CameraSource cameraSource;
    private BarcodeDetector barcodeDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission_group.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission.CAMERA)) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 0);
            }
        } else {

        }

        setContentView(R.layout.client);
        barcodeCheckMark = (ImageView) findViewById(R.id.barcode_found);
        setupCamera();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //setupCamera();

                } else {

                }
                return;
            }
        }
    }

    private void setupCamera() {
        cameraView = (SurfaceView) findViewById(R.id.cameraview);

        barcodeDetector =
                new BarcodeDetector.Builder(this)
                        .setBarcodeFormats(Barcode.ALL_FORMATS)
                        .build();

        cameraSource = new CameraSource
                .Builder(this, barcodeDetector)
                .setAutoFocusEnabled(true)
                .setRequestedPreviewSize(640, 480)
                .build();

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {

            Timer timer;

            boolean isShuttingDown = true;

            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    barcodeCheckMark.setVisibility(View.INVISIBLE);
            }};

            Barcode lastFound;

            @Override
            public void release() {
                timer.purge();
            }

            @Override
            public void receiveDetections(final Detector.Detections<Barcode> detections) {

                boolean hasDetections = detections.getDetectedItems().size() > 0 ? true : false;

                if(hasDetections) {
                    timerTask.cancel();
                    lastFound = detections.getDetectedItems().valueAt(0);
                    isShuttingDown = false;
                    barcodeCheckMark.post(new Runnable() {
                        @Override
                        public void run() {
                            barcodeCheckMark.setVisibility(View.VISIBLE);
                        }
                    });
                } else {
                    if(!isShuttingDown){
                        if(timer == null){
                            timer = new Timer();
                        }
                        timer.schedule(timerTask, 1500);
                    }
                    isShuttingDown = true;
                }
            }
        });

        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (checkSelfPermission(permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                        cameraSource.start(cameraView.getHolder());
                } catch (IOException ie) {
                    Log.e("CAMERA SOURCE", ie.getMessage());
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }


            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });
    }
}
