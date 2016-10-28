package com.example.banan.android_beadando;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {


    Button lightOnButton;
    Button strobeOnButton;
    SeekBar strobeSpeedbar;

    private CameraManager mCameraManager;
    private String mCameraId;
    public boolean isFlashOn = false;
    private MediaPlayer mp;
    Boolean isFlashAvailable = false;
    Boolean strobeON=false;
    private Handler strobe = new Handler();
    int strobeSpeed = 1;
    boolean isOnState = false;
    boolean strobeFinished = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            mCameraId = mCameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        isFlashAvailable = getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        lightOnButton = (Button)findViewById(R.id.LightOn) ;
        strobeOnButton = (Button)findViewById(R.id.StrobeOn);
        lightOnButton.setOnClickListener(lightOnClick);
        strobeOnButton.setOnClickListener(strobeOnClick);
        strobeSpeedbar = (SeekBar)findViewById(R.id.seekBar);
        strobeSpeedbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(

        ) {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    strobeSpeed = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

        private View.OnClickListener lightOnClick = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            if(!strobeON) {
                if (!isFlashOn && isFlashAvailable) {
                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            mCameraManager.setTorchMode(mCameraId, true);
                            isFlashOn = true;
                            lightOnButton.setText("Light off");
                            strobeON = false;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    isFlashOn = true;
                    lightOnButton.setText("Light off");
                } else if (isFlashAvailable) {
                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            mCameraManager.setTorchMode(mCameraId, false);
                            lightOnButton.setText("Light on");
                            isFlashOn = false;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(MainActivity.this, "The device doesn't have flashlight", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(MainActivity.this, "Please turn the strobe off first", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private View.OnClickListener strobeOnClick = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            if(isFlashOn)
            {
                if (!strobeON)
                {
                   strobe.postDelayed(runnable, strobeSpeed);
                    strobeON = true;
                    strobeOnButton.setText("Strobe OFF");
                    strobeFinished = false;



                }
                else
                {
                    strobeON=false;
                    strobeOnButton.setText("Strobe ON");


                }
            }
            else
            {
                Toast.makeText(MainActivity.this, "Flashlight should be ON", Toast.LENGTH_SHORT).show();
            }

        }
    };

    public void strobeing(){
        if(!isOnState) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mCameraManager.setTorchMode(mCameraId, true);
                    isOnState = true;
                    if(!strobeON) strobeFinished = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mCameraManager.setTorchMode(mCameraId, false);
                    isOnState = false;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
      /* do what you need to do */
            strobeing();
      /* and here comes the "trick" */
            if(!strobeFinished)
            strobe.postDelayed(this, strobeSpeed);
        }
    };
}
