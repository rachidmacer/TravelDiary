package com.example.kellihe_emil.traveldiary;

//Michael Augello, McKenna Buck, Emily Kelliher, Rachid Macer
//CS-480 Term Project
//May 1, 2018
//Post Activity

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class Post extends Activity implements View.OnClickListener{
    private Button button;
    private String file = "travel.txt";
    private String emailbody;

    private NotificationManager mNotificationManager;
    private Notification notifyDetails;
    private int SIMPLE_NOTFICATION_ID;
    private String contentText = "Your text has been created!";
    private String tickerText = "New Alert, Click Me!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);

        //for animation purposes, shows a sun because its vacation!
        ImageView img = (ImageView) findViewById(R.id.simple_anim);
        img.setBackgroundResource(R.drawable.simple_animation);

        AnimationRoutine1 task1 = new AnimationRoutine1();
        AnimationRoutine2 task2 = new AnimationRoutine2();

        Timer t = new Timer();
        t.schedule(task1, 1000);
        Timer t2 = new Timer();
        t2.schedule(task2, 5000);

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        //create intent for action when notification selected
        //from expanded status bar
        Intent notifyIntent = new Intent(this, TravelLog.class);

		/*
		  Intent notifyIntent = new Intent();
		  notifyIntent.setComponent(new ComponentName("com.course.example",
		                  "com.course.example.IOTest"));
		 */

        //create pending intent to wrap intent so that it
        //will fire when notification selected.
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //build notification object and set parameters
        notifyDetails =
                new Notification.Builder(this)
                        .setContentIntent(pendingIntent)

                          //set Notification text and icon
                        .setContentText(contentText)
                        .setSmallIcon(R.drawable.droid)

                        .setTicker(tickerText)            //set status bar text

                        .setWhen(System.currentTimeMillis())    //timestamp when event occurs

                        .setAutoCancel(true)     //cancel Notification after clicking on it

                        //set Android to vibrate when notified
                        .setVibrate(new long[]{1000, 1000, 1000, 1000})

                        // flash LED (color, on in millisec, off)
                        //doesn't work for all handsets
                        .setLights(Integer.MAX_VALUE, 500, 500)

                        .build();



    }



            class AnimationRoutine1 extends TimerTask {

                @Override
                public void run() {
                    ImageView img = (ImageView) findViewById(R.id.simple_anim);
                    AnimationDrawable frameAnimation = (AnimationDrawable) img.getBackground();
                    frameAnimation.start();
                }
            }

            class AnimationRoutine2 extends TimerTask {

                @Override
                public void run() {
                    ImageView img = (ImageView) findViewById(R.id.simple_anim);
                    AnimationDrawable frameAnimation = (AnimationDrawable) img.getBackground();
                }
            }

        public void onClick(View v)
        {

        Intent msg = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"));
        msg.putExtra(Intent.EXTRA_EMAIL, new String[]{"", ""});
        msg.putExtra(Intent.EXTRA_CC, new String[]{"", ""});
        msg.putExtra(Intent.EXTRA_BCC, new String[]{"", ""});
            try {
                //open stream for reading from file
                InputStream in = openFileInput(file);
                InputStreamReader isr = new InputStreamReader(in);
                BufferedReader reader = new BufferedReader(isr);
                String str = null;

                int count = 0;
                while ((str = reader.readLine()) != null) {
                    count++; // count number of records read
                    emailbody += str;
                }
            }catch (IOException e) {

            }
        msg.putExtra(Intent.EXTRA_TEXT, emailbody);
        msg.putExtra(Intent.EXTRA_SUBJECT, "Travel Log");


        //check to be sure email is installed on handset
        if (msg.resolveActivity(getPackageManager()) != null) {
        startActivity(msg);
        }

        }


    }



