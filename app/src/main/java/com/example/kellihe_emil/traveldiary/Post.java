package com.example.kellihe_emil.traveldiary;

//Michael Augello, McKenna Buck, Emily Kelliher, Rachid Macer
//CS-480 Term Project
//May 1, 2018
//Post Activity

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class Post extends Activity implements View.OnClickListener{
    private Button button;

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
        msg.putExtra(Intent.EXTRA_TEXT, "Hope you're still enjoying class.");
        msg.putExtra(Intent.EXTRA_SUBJECT, "Email Demo");

        //check to be sure email is installed on handset
        if (msg.resolveActivity(getPackageManager()) != null) {
        startActivity(msg);
        }

        }
    }



