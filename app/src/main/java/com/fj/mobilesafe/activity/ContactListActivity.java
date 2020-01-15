package com.fj.mobilesafe.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fj.mobilesafe.R;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;

public class ContactListActivity extends Activity {

    protected static final String tag = "ContactListActivity";
    private ListView lv_contact;
    private List<HashMap<String, String>> contactList = new ArrayList<HashMap<String, String>>();
    private MyAdapter mAdapter;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            mAdapter = new MyAdapter();
            lv_contact.setAdapter(mAdapter);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        initUI();
        initData();
    }

    public class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return contactList.size();
        }

        @Override
        public Object getItem(int position) {
            return contactList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View inflate = View.inflate(getApplicationContext(), R.layout.listview_contact_item, null);

            TextView tv_name = (TextView) inflate.findViewById(R.id.tv_name);
            TextView tv_phone = (TextView) inflate.findViewById(R.id.tv_phone);

            tv_name.setText(contactList.get(position).get("name"));
            tv_phone.setText(contactList.get(position).get("phone"));

            return inflate;
        }
    }

    /**
     * 获取系统联系人数据方法
     */
    private void initData() {
        new Thread() {
            @Override
            public void run() {
                //过去解析对象
                ContentResolver contentResolver = getContentResolver();
                //查询
                Cursor cursor = contentResolver.query(Uri.parse("content://com.android.contacts/raw_contacts"),
                        new String[]{"contact_id"},
                        null,
                        null,
                        null);
                contactList.clear();
                while (cursor.moveToNext()) {
                    String id = cursor.getString(0);
                    //4,根据用户唯一性id值,查询data表和mimetype表生成的视图,获取data以及mimetype字段
                    Cursor indexCursor = contentResolver.query(Uri.parse("content://com.android.contacts/data"),
                            new String[]{"data1", "mimetype"},
                            "raw_contact_id = ?",
                            new String[]{id},
                            null);
                    HashMap<String, String> hashMap = new HashMap<>();
                    while (indexCursor.moveToNext()) {
                        String data = indexCursor.getString(0);
                        String type = indexCursor.getString(1);

                        if (type.equals("vnd.android.cursor.item/phone_v2")) {
                            //数据非空判断
                            if (!TextUtils.isEmpty(data)) {
                                hashMap.put("phone", data);
                            }
                        } else if (type.equals("vnd.android.cursor.item/name")) {
                            if (!TextUtils.isEmpty(data)) {
                                hashMap.put("name", data);
                            }
                        }
                    }
                    indexCursor.close();
                    contactList.add(hashMap);
                    //7,消息机制,发送一个空的消息,告知主线程可以去使用子线程已经填充好的数据集合
                    mHandler.sendEmptyMessage(0);
                }
                cursor.close();
            }
        }.start();
    }

    private void initUI() {
        lv_contact = (ListView) findViewById(R.id.lv_contact);

        lv_contact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mAdapter != null) {
                    //得到选择的电话号码
                    HashMap<String, String> item = (HashMap<String, String>) mAdapter.getItem(position);
                    String phone = item.get("phone");

                    Intent intent = new Intent();
                    intent.putExtra("phone", phone);
                    setResult(0, intent);
                    finish();
                }

            }
        });
    }
}
