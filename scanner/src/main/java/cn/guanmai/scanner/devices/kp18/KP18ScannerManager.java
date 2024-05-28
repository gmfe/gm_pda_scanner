package cn.guanmai.scanner.devices.kp18;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.device.ScanManager;
import android.device.scanner.configuration.PropertyID;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.KeyEvent;

import cn.guanmai.scanner.IScannerManager;
import cn.guanmai.scanner.LogUtil;
import cn.guanmai.scanner.SupporterManager;

public class KP18ScannerManager implements IScannerManager {
    private Handler handler = new Handler(Looper.getMainLooper());
    private Context mContext;
    private static KP18ScannerManager instance;
    private SupporterManager.IScanListener listener;

    private static String ACTION_DATA_CODE_RECEIVED = "com.kte.scan.result";
    public static String SCAN_RESULT = "code";

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    String result = intent.getStringExtra(SCAN_RESULT);
                    LogUtil.printLog("[RECV] data=" + result);
                    if (!TextUtils.isEmpty(result)) {
                        listener.onScannerResultChange(result);
                    }
                }
            });
        }
    };

    private KP18ScannerManager(Context activity) {
        this.mContext = activity;
    }

    public static KP18ScannerManager getInstance(Context activity) {
        if (instance == null) {
            synchronized (KP18ScannerManager.class) {
                if (instance == null) {
                    instance = new KP18ScannerManager(activity);
                }
            }
        }
        return instance;
    }

    @Override
    public void init() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_DATA_CODE_RECEIVED);
        mContext.registerReceiver(receiver, intentFilter);

        listener.onScannerServiceConnected();
    }

    @Override
    public void recycle() {
        mContext.unregisterReceiver(receiver);
    }

    @Override
    public void setScannerListener(@NonNull SupporterManager.IScanListener listener) {
        this.listener = listener;
    }

    @Override
    public void sendKeyEvent(KeyEvent key) {
        LogUtil.printLog("[sendKeyEvent] value=" + key);
    }

    @Override
    public int getScannerModel() {
        LogUtil.printLog("[getScannerModel] value=" + 0);
        return 0;
    }

    @Override
    public void scannerEnable(boolean enable) {
        LogUtil.printLog("[scannerEnable] value=" + enable);
    }

    @Override
    public void setScanMode(String mode) {
        LogUtil.printLog("[setScanMode] value=" + mode);
    }

    @Override
    public void setDataTransferType(String type) {
        LogUtil.printLog("[setDataTransferType] value=" + type);
    }

    @Override
    public void singleScan(boolean bool) {
        LogUtil.printLog("[singleScan] value=" + bool);
    }

    @Override
    public void continuousScan(boolean bool) {
        LogUtil.printLog("[continuousScan] value=" + bool);
    }
}
