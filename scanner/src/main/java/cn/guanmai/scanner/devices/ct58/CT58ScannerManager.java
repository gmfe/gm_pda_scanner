package cn.guanmai.scanner.devices.ct58;

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

public class CT58ScannerManager implements IScannerManager {
    private Handler handler = new Handler(Looper.getMainLooper());
    private Context mContext;
    private static CT58ScannerManager instance;
    private SupporterManager.IScanListener listener;

    private static String ACTION_DATA_CODE_RECEIVED = ScanManager.ACTION_DECODE;
    public static String SCAN_RESULT = ScanManager.BARCODE_STRING_TAG;

    ScanManager mScanManager = new ScanManager();

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

    private CT58ScannerManager(Context activity) {
        this.mContext = activity;
    }

    public static CT58ScannerManager getInstance(Context activity) {
        if (instance == null) {
            synchronized (CT58ScannerManager.class) {
                if (instance == null) {
                    instance = new CT58ScannerManager(activity);
                }
            }
        }
        return instance;
    }

    @Override
    public void init() {
        //设置扫描模式
        mScanManager.switchOutputMode(0);
        //设置扫描参数
        String[] params = mScanManager.getParameterString(new int[]{PropertyID.WEDGE_INTENT_ACTION_NAME, PropertyID.WEDGE_INTENT_DATA_STRING_TAG});
        if (params != null && params.length == 2) {
            ACTION_DATA_CODE_RECEIVED = params[0];
            SCAN_RESULT = params[1];
        }

        if (TextUtils.isEmpty(ACTION_DATA_CODE_RECEIVED)) {
            ACTION_DATA_CODE_RECEIVED = ScanManager.ACTION_DECODE;
        }
        if (TextUtils.isEmpty(SCAN_RESULT)) {
            SCAN_RESULT = ScanManager.BARCODE_STRING_TAG;
        }

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
        if (bool) {
            mScanManager.startDecode();
        } else {
            mScanManager.stopDecode();
        }
    }

    @Override
    public void continuousScan(boolean bool) {
        LogUtil.printLog("[continuousScan] value=" + bool);
    }
}
