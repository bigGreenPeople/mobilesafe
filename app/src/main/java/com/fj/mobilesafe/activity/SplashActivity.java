package com.fj.mobilesafe.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import com.fj.mobilesafe.R;
import com.fj.mobilesafe.utils.StreamUtil;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SplashActivity extends AppCompatActivity {
    protected static final String tag = "SplashActivity";
    //更新版本状态码
    protected static final int UPDATE_VERSION = 100;
    //进入主界面状态码
    protected static final int ENTER_HOME = 101;
    //url错误状态码
    protected static final int URL_ERROR = 401;

    private TextView tx_version_name;
    private int mLocalVersionCode;

    private String versionDes;
    private String versionCode;
    private String downloadUrl;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case UPDATE_VERSION:
                    break;
                case ENTER_HOME:
                    enterHome();
                    break;
                case URL_ERROR:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Log.i("test:", "fujie2");
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
     * 进入主界面
     */
    protected void enterHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
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
                long startTime = System.currentTimeMillis();
                Message msg = Message.obtain();
                try {
                    URL url = new URL("https://qutu02.com/app/game_open/debug/testJson");
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                    urlConnection.setConnectTimeout(2000);
                    urlConnection.setReadTimeout(2000);

                    //默认就是get请求方式,
//					connection.setRequestMethod("POST");

                    if (urlConnection.getResponseCode() == 200) {
                        InputStream inputStream = urlConnection.getInputStream();
                        String requestJson = StreamUtil.streamToString(inputStream);
                        Log.i(tag, requestJson);

                        JSONObject jsonObject = new JSONObject(requestJson);
                        String versionName = jsonObject.getString("versionName");
                        versionDes = jsonObject.getString("versionDes");
                        versionCode = jsonObject.getString("versionCode");
                        downloadUrl = jsonObject.getString("downloadUrl");
                        Log.i(tag, versionName);
                        Log.i(tag, versionDes);
                        Log.i(tag, versionCode);
                        Log.i(tag, downloadUrl);

                        if (mLocalVersionCode < Integer.parseInt(versionCode)) {
                            msg.what = UPDATE_VERSION;
                        } else {
                            msg.what = ENTER_HOME;
                        }
                    }

                } catch (Exception e) {
                    msg.what = URL_ERROR;
                    e.printStackTrace();
                } finally {
                    //时间延时
                    long endTime = System.currentTimeMillis();
                    if (endTime - startTime < 4000) {
                        try {
                            Thread.sleep(4000-(endTime-startTime));
                        }catch (Exception time_e){
                            time_e.printStackTrace();
                        }
                    }
                    handler.sendMessage(msg);
                }
            }
        }.start();
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
