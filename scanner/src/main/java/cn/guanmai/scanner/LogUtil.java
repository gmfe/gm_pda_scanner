package cn.guanmai.scanner;

import android.util.Log;

public class LogUtil {
    public static boolean openLog = false;

    public static void printLog(String log) {
        if (openLog) {
            Log.i("Scanner_TAG", log);
        }
    }
}
