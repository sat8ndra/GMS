package com.lusat.max.gms;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

/**
 * Created by Satyendra on 2/26/2016.
 */
public class Splash extends Activity {
    MediaPlayer ourSong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        Thread timer = new Thread(){
            public void run(){
                try{
                    sleep(500);
                } catch (InterruptedException e){
                    e.printStackTrace();
                } finally {
                    Intent openStartingPoint = new Intent("com.lusat.max.gms.Menu");
                    startActivity(openStartingPoint);
                }
            }
        };
        timer.start();
    }
}
