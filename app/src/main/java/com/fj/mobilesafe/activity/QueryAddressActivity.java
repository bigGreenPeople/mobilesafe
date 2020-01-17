package com.fj.mobilesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.fj.mobilesafe.R;

public class QueryAddressActivity extends Activity {
    private EditText et_phone;
    private Button bt_query;
    private TextView tv_query_result;
    private String mAddress;
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            //4,控件使用查询结果
            tv_query_result.setText(mAddress);
        }

        ;
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_address);
        initUI();

    }

    private void initUI() {
        et_phone = (EditText) findViewById(R.id.et_phone);
        bt_query = (Button) findViewById(R.id.bt_query);
        tv_query_result = (TextView) findViewById(R.id.tv_query_result);


    }
}
