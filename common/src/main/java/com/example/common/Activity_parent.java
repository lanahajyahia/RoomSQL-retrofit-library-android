package com.example.common;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


import com.google.android.material.button.MaterialButton;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class Activity_parent extends AppCompatActivity {


    //    private MaterialButton main_BTN_apiCall;
//    private MaterialButton main_BTN_showAllLogs;
//    private MaterialButton main_BTN_average;
    private TextView txt_entrytime, txt_last_exittime, txt_alltimes;


    private long startTimeStamp = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent);

        txt_last_exittime = findViewById(R.id.txt_last_exittime);
        txt_entrytime = findViewById(R.id.txt_entrytime);
        txt_alltimes = findViewById(R.id.txt_alltimes);

        String currentDateTimeString = new SimpleDateFormat("dd/MM HH:mm:ss").format(new Date());
        txt_entrytime.setText("Entry time: " + currentDateTimeString);


        readLogs();
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

                            txt_alltimes.setText("all times: " + String.format("%02d min, %02d sec",
                                    TimeUnit.MILLISECONDS.toMinutes(sum),
                                    TimeUnit.MILLISECONDS.toSeconds(sum) -
                                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(sum))));
                            sb.append(new SimpleDateFormat("dd/MM HH:mm:ss").format(tLogs.get(tLogs.size() - 1).time));
//                            tLogs.get(tLogs.size() - 1).id + " " +
                            try {
                                Log.d("pttt", "Sum: " + sum + "   avg: " + (sum / count));
                                //  sb.append("Sum: " + sum + "   avg: " + (sum / count));

                            } catch (Exception e) {
                                Log.d("pttt", "Invalid Data");
                                sb.append("Invalid Data");
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //   txt_alltimes.setText(finalSum);
                                    txt_last_exittime.setText("last exit: " + sb.toString());
                                }
                            });
                        }
                    }
                });
            }
        }).start();
    }
//    private void readLogs() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                MyTimeLogger.getInstance().getAllLogsByTag("activity_main_time",new MyTimeLogger.CallBack_Logs() {
//                    @Override
//                    public void dataReady(List<TLog> tLogs) {
//                        int sum = 0;
//                        int count = 0;
//                        StringBuilder sb = new StringBuilder("");
//                        for (TLog tLog : tLogs) {
//                            sb.append(tLog.id + " " +
//                                    new SimpleDateFormat("dd/MM HH:mm:ss").format(tLog.time)+ " " +
//                                    tLog.tag + " " +
//                                    "\n");
//                            Log.d("pttt",
//                                    tLog.id + " " +
//                                            new SimpleDateFormat("dd/MM HH:mm:ss").format(tLog.time)+ " " +
//                                            tLog.tag + " " +
//                                            tLog.duration);
//
//                            count++;
//                            sum += tLog.duration;
//                            txt_alltimes.setText("all times: " + sum);
//                        }
//
//
////                        try {
////                            Log.d("pttt", "Sum: " + sum + "   avg: " + (sum / count));
////                          //  sb.append("Sum: " + sum + "   avg: " + (sum / count));
////
////                        } catch (Exception e) {
////                            Log.d("pttt", "Invalid Data");
////                            sb.append("Invalid Data");
////                        }
//
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                             //   txt_alltimes.setText(finalSum);
//                                txt_last_exittime.setText(sb.toString());
//                            }
//                        });
//                    }
//                });
//            }
//        }).start();
//    }

    private void apiCall() {
        final String LINK = "https://pastebin.com/raw/WtvLGNXJ";

        new Thread(new Runnable() {

            public void run() {
                String data = "";

                try {
                    long startTimeStamp = System.currentTimeMillis();
                    URL url = new URL(LINK); //My text file location
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(60000);
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String str;
                    while ((str = in.readLine()) != null) {
                        data += str;
                    }
                    in.close();
                    long duration = System.currentTimeMillis() - startTimeStamp;
                    Log.d("pttt", "Api success in " + duration + " ms");

                    MyTimeLogger.getInstance().addTlogTime("download_json_time", (int) duration);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


}
//        txt_title = findViewById(R.id.txt_title);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                String lines = "";
//                try {
//                    // Create a URL for the desired page
//                    URL url = new URL("https://pastebin.com/raw/WypPzJCt"); //My text file location
//                    //First open the connection
//                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                    conn.setConnectTimeout(60000); // timing out in a minute
//
//                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//
//                    String str;
//                    while ((str = in.readLine()) != null) {
//                        lines += str;
//                    }
//                    in.close();
//                } catch (Exception e) {
//                    Log.d("pttt exc", e.toString());
//                }
//                Log.d("pttt line", lines);
//            }
//        }).start();
//  //  protected void setTextTitle(String textString) {
////        txt_title.setText(textString);
////    }