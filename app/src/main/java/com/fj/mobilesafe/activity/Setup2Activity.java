package com.fj.mobilesafe.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.fj.mobilesafe.R;
import com.fj.mobilesafe.utils.ConstantValue;
import com.fj.mobilesafe.utils.SpUtil;
import com.fj.mobilesafe.utils.ToastUtil;
import com.fj.mobilesafe.view.SettingItemView;

public class Setup2Activity extends BaseSetupActivity {
    private SettingItemView siv_sim_bound;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);
        initUI();
    }

    @Override
    protected void showNextPage() {
        String serialNumber = SpUtil.getString(this, ConstantValue.SIM_NUMBER, "");
        if (!TextUtils.isEmpty(serialNumber)) {
            Intent intent = new Intent(getApplicationContext(), Setup3Activity.class);
            startActivity(intent);
            finish();

            overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
        } else {
            ToastUtil.show(this, "请绑定sim卡");
        }
    }

    @Override
    protected void showPrePage() {
        Intent intent = new Intent(this, Setup1Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.pre_in_anim, R.anim.pre_out_anim);
    }

    private void initUI() {
        siv_sim_bound = (SettingItemView) findViewById(R.id.siv_sim_bound);
        //1,回显(读取已有的绑定状态,用作显示,sp中是否存储了sim卡的序列号)
        String sim_number = SpUtil.getString(this, ConstantValue.SIM_NUMBER, "");
        if (TextUtils.isEmpty(sim_number)) {
            siv_sim_bound.setCheck(false);
        } else {
            siv_sim_bound.setCheck(true);
        }
        //设置点击事件
        siv_sim_bound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //3,获取原有的状态
                boolean check = siv_sim_bound.isCheck();
                //设置新的状态
                siv_sim_bound.setCheck(!check);
                if (!check) {
                    // 设置新的sim卡
                    TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    try {
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        String simSerialNumber = telephonyManager.getSimSerialNumber();
                        //6.3存储
                        SpUtil.putString(getApplicationContext(), ConstantValue.SIM_NUMBER, simSerialNumber);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    // 删除sim卡
                    SpUtil.remove(getApplicationContext(), ConstantValue.SIM_NUMBER);
                }
            }
        });
    }

}
