package com.example.kellihe_emil.traveldiary;

import android.app.Activity;
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



