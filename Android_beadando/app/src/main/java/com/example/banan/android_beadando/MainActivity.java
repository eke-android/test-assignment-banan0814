package com.example.banan.android_beadando;

import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;

public class MainActivity extends AppCompatActivity {
    Button lightOnButton;
    Button strobeOnButton;
    Camera cam;
    public boolean isFlashOn = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lightOnButton = (Button)findViewById(R.id.LightOn) ;
        strobeOnButton = (Button)findViewById(R.id.StrobeOn);
        lightOnButton.setOnClickListener(lightOnClick);
        strobeOnButton.setOnClickListener(strobeOnClick);

    }

    private View.OnClickListener lightOnClick = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            if(!isFlashOn){
            try{
                cam.open();
                Parameters p = cam.getParameters();
                p.setFlashMode(Parameters.FLASH_MODE_TORCH);
                cam.setParameters(p);
                cam.startPreview();
                isFlashOn = true;
                lightOnButton.setText("Light off");
            }catch (Exception e){
                Toast.makeText(MainActivity.this, "The device doesn't have flashlight", Toast.LENGTH_SHORT).show();
            }}
            else {
                cam.stopPreview();
                cam.release();
               isFlashOn = false;
            }
        }
    };

    private View.OnClickListener strobeOnClick = new View.OnClickListener(){
        @Override
        public void onClick(View v) {

        }
    };

}
