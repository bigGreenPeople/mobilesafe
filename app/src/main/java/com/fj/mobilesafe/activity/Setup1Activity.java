package com.fj.mobilesafe.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;

import com.fj.mobilesafe.R;
import com.fj.mobilesafe.utils.ConstantValue;
import com.fj.mobilesafe.utils.SpUtil;
import com.fj.mobilesafe.view.SettingItemView;

public class Setup1Activity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup1);
    }


    public void nextPage(View view) {
        Intent intent = new Intent(this, Setup2Activity.class);
        startActivity(intent);
        finish();
    }
}
