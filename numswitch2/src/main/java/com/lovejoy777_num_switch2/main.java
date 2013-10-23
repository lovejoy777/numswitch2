package com.lovejoy777_num_switch2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class main extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        Thread logoTimer = new Thread() {
            public void run() {
                try {
                    sleep(1000);
                    Intent menuIntent = new Intent("com.lovejoy777_num_switch2.MENU");
                    startActivity(menuIntent);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    finish();
                }
            }

        };
        logoTimer.start();


    }

}
