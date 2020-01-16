package com.fj.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

import com.fj.mobilesafe.R;
import com.fj.mobilesafe.utils.ConstantValue;
import com.fj.mobilesafe.utils.SpUtil;

public class SmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        boolean open_security = SpUtil.getBoolean(context, ConstantValue.OPEN_SECURITY, false);

        if (open_security) {
            Object[] objects = (Object[]) intent.getExtras().get("pdus");
            for (Object object : objects) {
                SmsMessage sms = SmsMessage.createFromPdu((byte[]) object);
                String originatingAddress = sms.getOriginatingAddress();
                String messageBody = sms.getMessageBody();

                if (messageBody.contains("#*alarm*#")) {
                    //7,播放音乐(准备音乐,MediaPlayer)
                    MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.ylzs);
                    mediaPlayer.setLooping(true);
                    mediaPlayer.start();
                }
            }
        }
    }
}
