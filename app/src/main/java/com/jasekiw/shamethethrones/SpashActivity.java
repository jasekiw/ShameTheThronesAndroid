package com.jasekiw.shamethethrones;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SpashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
