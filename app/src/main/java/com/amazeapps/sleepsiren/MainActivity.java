package com.amazeapps.sleepsiren;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    Button button;
    Button stop;
    static private SensorManager sensorManager;
    static private Sensor sensor;
    private long lastUpdate=0;
    private float last_x, last_y,last_z;
    private static final int SHAKE_THRESHOLD=50;
    static MediaPlayer mediaPlayer;

    TextView thres;
    TextView speed_t;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        thres = findViewById(R.id.textView);
        speed_t = findViewById(R.id.textView2);

        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        button = findViewById(R.id.button);
        stop = findViewById(R.id.button_stop);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sensorManager.registerListener(MainActivity.this,sensor,SensorManager.SENSOR_DELAY_NORMAL);
                mediaPlayer = MediaPlayer.create(MainActivity.this,R.raw.buzz);

            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                sensorManager.unregisterListener(MainActivity.this);
            }
        });
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {


        thres.setText(String.valueOf(SHAKE_THRESHOLD));
        thres = findViewById(R.id.textView);
        speed_t = findViewById(R.id.textView2);


       Sensor sensor = sensorEvent.sensor;
       if(sensor.getType() == Sensor.TYPE_ACCELEROMETER)
       {
           float x= sensorEvent.values[0];
           float y = sensorEvent.values[1];
           float z = sensorEvent.values[2];

           long cur_time = System.currentTimeMillis();

           if(cur_time-lastUpdate>100)
           {
               long diff = cur_time-lastUpdate;
               lastUpdate = cur_time;

               float speed = Math.abs(x+y+z-last_x-last_y-last_z)/diff*1000;

               speed_t.setText(String.format("%.2f",speed));

               if(speed>SHAKE_THRESHOLD){
                   speed_t.setTextColor(Color.RED);
                   mediaPlayer.start();
//                   sensorManager.unregisterListener(this);
               }
               else
               {
                   speed_t.setTextColor(thres.getTextColors());
               }

               last_x = x;
               last_y = y;
               last_z = z;
           }
       }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
