package com.lovejoy777_num_switch2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by lovejoy on 02/10/13.
 */
public class menu extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button numBt1 = (Button) findViewById(R.id.numBt1);
        Button numBt2 = (Button) findViewById(R.id.numBt2);
        Button tut3 = (Button) findViewById(R.id.tutorial3);
        Button tut4 = (Button) findViewById(R.id.tutorial4);
        Button over = (Button) findViewById(R.id.overview);
        Button set = (Button) findViewById(R.id.setup);
        Button about = (Button) findViewById(R.id.about);
        Button btBtn1 = (Button) findViewById(R.id.btmain);
        Button btBtn2 = (Button) findViewById(R.id.macAddressBtn);


        numBt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent("com.lovejoy777_num_switch2.BT2MAIN"));
            }
        });
        numBt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent("com.lovejoy777_num_switch2.TUTORIALTWO"));
            }
        });
        tut3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent("com.lovejoy777_num_switch2.TUTORIALTHREE"));
            }
        });
        tut4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent("com.lovejoy777_num_switch2.TUTORIALFOUR"));
            }
        });

        over.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent("com.lovejoy777_num_switch2.OVERVIEW"));
            }
        });

        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent("com.lovejoy777_num_switch2.SETUP"));
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent("com.lovejoy777_num_switch2.ABOUT"));
            }
        });

        btBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent("com.lovejoy777_num_switch2.BTMAIN"));
            }
        });

        btBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent("com.lovejoy777_num_switch2.MACADDRESSBTN"));
            }
        });


    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
