package com.example.kellihe_emil.traveldiary;

//Michael Augello, McKenna Buck, Emily Kelliher, Rachid Macer
//CS-480 Term Project
//May 1, 2018
//Post Activity

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;
import android.graphics.drawable.AnimationDrawable;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class Post extends Activity implements View.OnClickListener{
    private Button button;
    private String file = "travel.txt";
    private String emailbody;

    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder = null;
    private int SIMPLE_NOTFICATION_ID;
    private String contentText = "Your post has been created!";
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

        mNotificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        //As of API 26 Notification Channels must be assigned to a channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("default",
                    "Channel foobar",
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Channel description");
            channel.setLightColor(Color.GREEN);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mNotificationManager.createNotificationChannel(channel);
        }
        //create intent for action when notification selected
        //from expanded status bar
        Intent notifyIntent = new Intent(this, TravelLog.class);

        //create pending intent to wrap intent so that it
        //will fire when notification selected.
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        //set parameter values for Notification
        mBuilder = new NotificationCompat.Builder(this, "default")
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.droid)
                .setContentTitle(tickerText)
                .setContentText(contentText)
                .setAutoCancel(true)     //cancel Notification after clicking on it
                .setSound(Uri.parse("android.resource://com.course.example.notify/"+R.raw.photon))
                //set Android to vibrate when notified
                .setVibrate(new long[] {1000, 1000, 2000, 2000})
                //allow heads up notification; otherwise use PRIORITY_DEFAULT
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        mNotificationManager.notify(SIMPLE_NOTFICATION_ID, mBuilder.build());
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
                String str = "";

                int count = 0;
                while ((str = reader.readLine()) != null) {
                    count++; // count number of records read
                    emailbody += str;
                }
            } catch (IOException e) {}
        msg.putExtra(Intent.EXTRA_TEXT, emailbody);
        msg.putExtra(Intent.EXTRA_SUBJECT, "Travel Log");


        //check to be sure email is installed on handset
        if (msg.resolveActivity(getPackageManager()) != null) {
        startActivity(msg);
        }

        }


    }



