package cn.guanmai.scanner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Build;

public class BroadcastUtil {
    public static void registerReceiver(Context context, BroadcastReceiver receiver, IntentFilter filters) {
        if (context == null || receiver == null || filters == null) {
            return;
        }

        if (Build.VERSION.SDK_INT >= 34) {
            context.registerReceiver(receiver, filters, 0x4);
        } else {
            context.registerReceiver(receiver, filters);
        }
    }
}
