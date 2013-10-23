package com.lovejoy777_num_switch2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;


/**
 * Created by lovejoy on 15/10/13.
 */
public class MacAddress extends Activity {
    public final static String EXTRA_MESSAGE = "lovejoy777_num_switch2.mac_address";

    /**
     * Called when the user clicks the Send button
     */
    public void sendMessage(View view) {
        Intent intent = new Intent(this, BT2Main.class);
        EditText editText = (EditText) findViewById(R.id.num1MacET);
        String num1address = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, num1address);
        startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mac_address);
    }


}