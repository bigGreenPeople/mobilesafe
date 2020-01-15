package com.fj.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.fj.mobilesafe.R;
import com.fj.mobilesafe.utils.ConstantValue;
import com.fj.mobilesafe.utils.SpUtil;
import com.fj.mobilesafe.utils.ToastUtil;

public class Setup3Activity extends Activity {
    public static final String tag = "Setup3Activity";
    private EditText et_phone_number;
    private Button bt_select_number;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);
        initUI();
    }

    private void initUI() {
        //显示电话号码的输入框
        et_phone_number = (EditText) findViewById(R.id.et_phone_number);
        //获取联系人电话号码回显过程
        String phone = SpUtil.getString(this, ConstantValue.CONTACT_PHONE, "");

        et_phone_number.setText(phone);
        bt_select_number = (Button) findViewById(R.id.bt_select_number);
        bt_select_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ContactListActivity.class);
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(tag, resultCode + "");
        if (data != null) {
            String phone = data.getStringExtra("phone");

            phone = phone.replace("-", "").replace(" ", "").trim();
            et_phone_number.setText(phone);

            //3,存储联系人至sp中
            SpUtil.putString(getApplicationContext(), ConstantValue.CONTACT_PHONE, phone);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 下一页
     *
     * @param view
     */
    public void nextPage(View view) {
        //点击按钮以后,需要获取输入框中的联系人,再做下一页操作
        String phone = et_phone_number.getText().toString();
        if(!TextUtils.isEmpty(phone)){
            Intent intent = new Intent(this, Setup4Activity.class);
            startActivity(intent);
            finish();
            //如果现在是输入电话号码,则需要去保存
            SpUtil.putString(getApplicationContext(), ConstantValue.CONTACT_PHONE, phone);
            overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);

        }else{
            ToastUtil.show(this,"请输入电话号码");
        }

    }

    /**
     * 上一页
     *
     * @param view
     */
    public void prePage(View view) {
        Intent intent = new Intent(this, Setup2Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.pre_in_anim, R.anim.pre_out_anim);

    }
}
