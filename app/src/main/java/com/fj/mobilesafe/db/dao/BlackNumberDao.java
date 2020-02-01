package com.fj.mobilesafe.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.fj.mobilesafe.db.BlackNumberOpenHelper;
import com.fj.mobilesafe.db.domain.BlackNumberInfo;
import com.fj.mobilesafe.utils.ConstantValue;

import java.util.ArrayList;
import java.util.List;

public class BlackNumberDao {
    private BlackNumberOpenHelper blackNumberOpenHelper;
    //2,声明一个当前类的对象
    private static BlackNumberDao blackNumberDao = null;

    //BlackNumberDao单例模式
    //1,私有化构造方法
    private BlackNumberDao(Context context) {
        BlackNumberOpenHelper blackNumberOpenHelper = new BlackNumberOpenHelper(context);
    }

    public static BlackNumberDao getInstance(Context context) {
        if (blackNumberDao == null) {
            blackNumberDao = new BlackNumberDao(context);
        }
        return blackNumberDao;
    }


    /**
     * 增加一个条目
     *
     * @param phone 拦截的电话号码
     * @param mode  拦截类型(1:短信	2:电话	3:拦截所有(短信+电话))
     */
    public void insert(String phone, String mode) {
        SQLiteDatabase writableDatabase = blackNumberOpenHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("phone", phone);
        values.put("mode", mode);
        writableDatabase.insert("blacknumber", null, values);

        writableDatabase.close();
    }


    /**
     * 从数据库中删除一条电话号码
     *
     * @param phone 删除电话号码
     */
    public void delete(String phone) {
        SQLiteDatabase writableDatabase = blackNumberOpenHelper.getWritableDatabase();

        writableDatabase.delete("blacknumber", "phone = ?", new String[]{phone});

        writableDatabase.close();
    }


    /**
     * 根据电话号码去,更新拦截模式
     *
     * @param phone 更新拦截模式的电话号码
     * @param mode  要更新为的模式(1:短信	2:电话	3:拦截所有(短信+电话)
     */
    public void update(String phone, String mode) {
        SQLiteDatabase writableDatabase = blackNumberOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("mode", mode);
        writableDatabase.update("blacknumber", values, "phone = ?", new String[]{phone});

        writableDatabase.close();
    }

    /**
     * @return 查询到数据库中所有的号码以及拦截类型所在的集合
     */
    public List<BlackNumberInfo> findAll() {
        SQLiteDatabase writableDatabase = blackNumberOpenHelper.getWritableDatabase();
        Cursor cursor = writableDatabase.query("blacknumber",
                new String[]{"phone", "mode"},
                null, null, null, null,
                "_id desc");
        List<BlackNumberInfo> blackNumberList = new ArrayList<BlackNumberInfo>();
        while (cursor.moveToNext()) {
            BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
            blackNumberInfo.phone = cursor.getString(0);
            blackNumberInfo.mode = cursor.getString(1);
            blackNumberList.add(blackNumberInfo);
        }
        cursor.close();
        writableDatabase.close();

        return blackNumberList;
    }

    /**
     * 每次查询20条数据
     *
     * @param index 查询的索引值
     */
    public List<BlackNumberInfo> find(int index) {
        return null;
    }
}
