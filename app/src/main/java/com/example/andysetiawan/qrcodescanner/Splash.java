package com.example.andysetiawan.qrcodescanner;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * Created by Andy Setiawan on 3/18/2016.
 */
public class Splash extends Activity{

    MediaPlayer mpSplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        final ImageView logoSplah = (ImageView) findViewById (R.id.LogoSplash);
        final Animation animLogoSplash = AnimationUtils.loadAnimation(getBaseContext(), R.anim.fade_in2);
        mpSplash = MediaPlayer.create(this, R.raw.sound_effect_splash);
        mpSplash.start();


        logoSplah.startAnimation(animLogoSplash);

        Thread logoTimer = new Thread(){
            public void run(){
                try{
                    int logoTimer = 0;
                    while (logoTimer < 3000){
                        sleep(100);
                        logoTimer = logoTimer + 100;
                    }
                    Intent i = new Intent(Splash.this, MainActivity.class);
                    startActivity(i);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }

                finally {
                    finish();
                }
            }
        };
        logoTimer.start();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mpSplash.release();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mpSplash.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mpSplash.start();
    }
}
