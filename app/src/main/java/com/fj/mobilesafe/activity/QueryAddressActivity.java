package com.fj.mobilesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.fj.mobilesafe.R;
import com.fj.mobilesafe.engine.AddressDao;

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

        bt_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = et_phone.getText().toString();

                if(!TextUtils.isEmpty(phone)){
                    //2,查询是耗时操作,开启子线程
                    query(phone);
                }else{

                }
            }
        });
    }

    /**
     * 耗时操作
     * 获取电话号码归属地
     * @param phone	查询电话号码
     */
    protected void query(final String phone) {
        new Thread(){
            public void run() {
                mAddress = AddressDao.getAddress(phone);
                //3,消息机制,告知主线程查询结束,可以去使用查询结果
                mHandler.sendEmptyMessage(0);
            };
        }.start();
    }
}
