package com.example.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private SensorEventListener sensorListener;

    private long lastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if (accelerometer != null) {
            sensorListener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent sensorEvent) {
                    // Valores del acelerómetro en m/s^2
                    float xAcc = sensorEvent.values[0];
                    float yAcc = sensorEvent.values[1];
                    float zAcc = sensorEvent.values[2];

                    // Actualizar los TextView con los valores del acelerómetro
                    DecimalFormat decimalFormat = new DecimalFormat("#.##");
                    xAcc = Float.parseFloat(decimalFormat.format(xAcc));
                    yAcc = Float.parseFloat(decimalFormat.format(yAcc));
                    zAcc = Float.parseFloat(decimalFormat.format(zAcc));

                    // Actualizar los TextView con los valores del acelerómetro
                    updateTextView(R.id.textViewXAcc, "X Acc: " + xAcc);
                    updateTextView(R.id.textViewYAcc, "Y Acc: " + yAcc);
                    updateTextView(R.id.textViewZAcc, "Z Acc: " + zAcc);

                    // Procesamiento o visualización adicional si es necesario...
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {
                    // Puedes ignorar esta callback por el momento
                }
            };

            // Registramos el listener para capturar los eventos del sensor
            sensorManager.registerListener(sensorListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        }
        RelativeLayout mainLayout = findViewById(R.id.mainLayout);
        mainLayout.setOnClickListener(view -> checkDoubleClick());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Desregistramos el listener para evitar posibles fugas de memoria
        if (sensorManager != null && sensorListener != null) {
            sensorManager.unregisterListener(sensorListener);
        }
    }



    private void updateTextView(int textViewId, String text) {
        TextView textView = findViewById(textViewId);
        if (textView != null) {
            textView.setText(text);
        }
    }




    // Método para verificar el doble clic
    private void checkDoubleClick() {
        long clickTime = System.currentTimeMillis();

        if (clickTime - lastClickTime < 500) {
            showToast("Has dado doble click!");
        }

        lastClickTime = clickTime;
    }


    // Método para mostrar un Toast
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
