package com.fj.mobilesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.fj.mobilesafe.R;

public class HomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.i("test:", "fujie2");
    }
}
