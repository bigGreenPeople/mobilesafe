package com.fj.mobilesafe.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.fj.mobilesafe.R;
import com.fj.mobilesafe.utils.StreamUtil;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class SplashActivity extends AppCompatActivity {
    protected static final String tag = "SplashActivity";

    private TextView tx_version_name;
    private int mLocalVersionCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_main);
        Log.i("test:", "fujie");
        initUI();
        initData();
    }

    /**
     * 初始化UI
     */
    private void initUI() {
        tx_version_name = (TextView) findViewById(R.id.tv_version_name);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        String versionName = getVersionName();
        tx_version_name.setText(versionName);

        //本地版本号
        mLocalVersionCode = getVersionCode();
        //3,获取服务器版本号(客户端发请求,服务端给响应,(json,xml))
        //http://www.oxxx.com/update74.json?key=value  返回200 请求成功,流的方式将数据读取下来
        //json中内容包含:
        /* 更新版本的版本名称
         * 新版本的描述信息
         * 服务器版本号
         * 新版本apk下载地址*/
        checkVersion();
    }

    /**
     * 检测版本号
     */
    private void checkVersion() {
        new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://10.0.2.2:8080/update74.json");
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                    urlConnection.setConnectTimeout(2000);
                    urlConnection.setReadTimeout(2000);

                    //默认就是get请求方式,
//					connection.setRequestMethod("POST");

                    if (urlConnection.getResponseCode() == 200) {
                        InputStream inputStream = urlConnection.getInputStream();
                        String requestJson = StreamUtil.streamToString(inputStream);
                        Log.i(tag, requestJson);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    /**
     * 获取应用版本名称
     *
     * @return 应用版本名称 null 异常
     */
    private String getVersionName() {
        //得到包管理者
        PackageManager packageManager = getPackageManager();
        //获取包名信息
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            //获取版本
            return packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取应用版本号
     *
     * @return 应用版本名称 0 异常
     */
    private int getVersionCode() {
        //得到包管理者
        PackageManager packageManager = getPackageManager();
        //获取包名信息
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            //获取版本
            return packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
