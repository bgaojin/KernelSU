package me.weishu.kernelsu.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;

/**
 * @author: wrp
 * @date : 2024/9/28
 * @desc :
 */
public class VolumeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        System.out.println("action="+action);
        if ("android.media.VOLUME_CHANGED_ACTION".equals(action)) {
            KeyEvent keyEvent = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
            int eventAction = keyEvent.getAction();
            if (eventAction==KeyEvent.ACTION_DOWN){
                System.out.println("音量下");
            }else if (eventAction==KeyEvent.ACTION_UP){
                System.out.println("音量加");
            }
        }
    }
}
