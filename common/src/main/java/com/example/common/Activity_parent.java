package com.example.common;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;


import com.google.android.material.textview.MaterialTextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class Activity_parent extends AppCompatActivity {


    private TextView txt_entrytime, txt_last_exittime, txt_alltimes;
    MaterialTextView txt_name, txt_cars, txt_open, txt_address;

    private long startTimeStamp = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent);

        init();

        time_func();

        car_func();
    }

    private void car_func() {
        downloadCars();
    }

    private void downloadCars() {
        new CarController().fetchCar(new CarController.CallBack_Car() {
            @Override
            public void car(Car car) {
                txt_name.setText("name: " + car.getName());
                if (car.isOpen()) {
                    txt_open.setText("opened");
                } else {
                    txt_open.setText("closed");
                }
                txt_address.setText("address: " + car.getAddress());
                txt_cars.setText("Cars: " + car.getCars());
            }
        });
    }

    private void init() {
        txt_last_exittime = findViewById(R.id.txt_last_exittime);
        txt_entrytime = findViewById(R.id.txt_entrytime);
        txt_alltimes = findViewById(R.id.txt_alltimes);
        txt_cars = findViewById(R.id.txt_cars);
        txt_name = findViewById(R.id.txt_name);
        txt_address = findViewById(R.id.txt_address);
        txt_open = findViewById(R.id.txt_open);

    }

    private void time_func() {
        String currentDateTimeString = new SimpleDateFormat("dd/MM HH:mm:ss").format(new Date());
        txt_entrytime.setText("Entry time: " + currentDateTimeString);


        readLogs();
    }

    private void second_func() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        startTimeStamp = System.currentTimeMillis();

    }

    @Override
    protected void onStop() {
        super.onStop();
        long duration = System.currentTimeMillis() - startTimeStamp;
        MyTimeLogger.getInstance().addTlogTime("activity_main_time", (int) duration);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        super.onPause();
        long duration = System.currentTimeMillis() - startTimeStamp;
        MyTimeLogger.getInstance().addTlogTime("activity_main_time", (int) duration);

    }

    @Override
    protected void onPause() {
        super.onPause();
        long duration = System.currentTimeMillis() - startTimeStamp;
        MyTimeLogger.getInstance().addTlogTime("activity_main_time", (int) duration);
    }

    private void readLogs() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MyTimeLogger.getInstance().getAllLogsByTag("activity_main_time", new MyTimeLogger.CallBack_Logs() {
                    @Override
                    public void dataReady(List<TLog> tLogs) {
                        int sum = 0;
                        int count = 0;
                        StringBuilder sb = new StringBuilder("");
                        if (tLogs.isEmpty()) {
                            txt_alltimes.setText("first time entring");
                            txt_last_exittime.setText("first time entring");
                        } else {
                            for (TLog tLog : tLogs) {

                                Log.d("pttt",
                                        tLog.id + " " +
                                                new SimpleDateFormat("dd/MM HH:mm:ss").format(tLog.time) + " " +
                                                tLog.tag + " " +
                                                tLog.duration);

                                count++;
                                sum += tLog.duration;

                            }
                            try {
                                Log.d("pttt", "Sum: " + sum + "   avg: " + (sum / count));

                            } catch (Exception e) {
                                Log.d("pttt", "Invalid Data");
                                sb.append("Invalid Data");
                            }
                            txt_alltimes.setText("all times: " + time_format(sum));
                            sb.append(new SimpleDateFormat("dd/MM HH:mm:ss").format(tLogs.get(tLogs.size() - 1).time));


                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    txt_last_exittime.setText("last exit: " + sb.toString());
                                }
                            });
                        }
                    }
                });
            }
        }).start();
    }

    private String time_format(int sum) {
        return String.format("%02d min, %02d sec",
                TimeUnit.MILLISECONDS.toMinutes(sum),
                TimeUnit.MILLISECONDS.toSeconds(sum) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(sum)));
    }
}
