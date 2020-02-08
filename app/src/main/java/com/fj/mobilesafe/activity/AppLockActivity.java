package com.fj.mobilesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.fj.mobilesafe.R;
import com.fj.mobilesafe.db.dao.AppLockDao;
import com.fj.mobilesafe.db.domain.AppInfo;
import com.fj.mobilesafe.engine.AppInfoProvider;

import java.util.ArrayList;
import java.util.List;

public class AppLockActivity extends Activity {
    private Button bt_unlock,bt_lock;
    private LinearLayout ll_unlock,ll_lock;
    private TextView tv_unlock,tv_lock;
    private ListView lv_unlock,lv_lock;
    private List<AppInfo> mAppInfoList;
    private List<AppInfo> mLockList;
    private List<AppInfo> mUnLockList;
    private AppLockDao mDao;
    private Handler mHandler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            //6.接收到消息,填充已加锁和未加锁的数据适配器
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_lock);
        initUI();
        initData();
    }

    /**
     * 区分已加锁和未加锁应用的集合
     */
    private void initData() {
        new Thread(){
            public void run() {
                //1.获取所有手机中的应用
                mAppInfoList = AppInfoProvider.getAppInfoList(getApplicationContext());
                //2.区分已加锁应用和未加锁应用
                mLockList = new ArrayList<AppInfo>();
                mUnLockList = new ArrayList<AppInfo>();

                //3.获取数据库中已加锁应用包名的的结合
                mDao = AppLockDao.getInstance(getApplicationContext());
                List<String> lockPackageList = mDao.findAll();
                for (AppInfo appInfo : mAppInfoList) {
                    //4,如果循环到的应用的包名,在数据库中,则说明是已加锁应用
                    if(lockPackageList.contains(appInfo.packageName)){
                        mLockList.add(appInfo);
                    }else{
                        mUnLockList.add(appInfo);
                    }
                }
                //5.告知主线程,可以使用维护的数据
            };
        }.start();
    }

    private void initUI() {
        bt_unlock = (Button) findViewById(R.id.bt_unlock);
        bt_lock = (Button) findViewById(R.id.bt_lock);

        ll_unlock = (LinearLayout) findViewById(R.id.ll_unlock);
        ll_lock = (LinearLayout) findViewById(R.id.ll_lock);

        tv_unlock = (TextView) findViewById(R.id.tv_unlock);
        tv_lock = (TextView) findViewById(R.id.tv_lock);

        lv_unlock = (ListView) findViewById(R.id.lv_unlock);
        lv_lock = (ListView) findViewById(R.id.lv_lock);
    }
}
