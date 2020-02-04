package com.fj.mobilesafe;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.fj.mobilesafe.db.dao.BlackNumberDao;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("com.fj.mobilesafe", appContext.getPackageName());
    }

    @Test
    public void test01() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        BlackNumberDao dao = BlackNumberDao.getInstance(appContext);
        dao.insert("15079247509", "2");
    }
}
