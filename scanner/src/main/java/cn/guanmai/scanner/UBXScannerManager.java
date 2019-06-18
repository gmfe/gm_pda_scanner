package cn.guanmai.scanner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.device.ScanManager;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

public class UBXScannerManager implements IScannerManager {
    private Handler handler = new Handler(Looper.getMainLooper());
    private Context activity;
    private static UBXScannerManager instance;
    public static final String ACTION_DATA_CODE_RECEIVED = "android.intent.ACTION_DECODE_DATA";
    private SupporterManager.IScanListener listener;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    String code = intent.getStringExtra(ScanManager.BARCODE_STRING_TAG);
                    if (code != null && !code.isEmpty()) {
                        listener.onScannerResultChange(code);
                    }
                }
            });
        }
    };
    private ScanManager mScanManager;

    private UBXScannerManager(Context activity) {
        this.activity = activity;
    }

    static UBXScannerManager getInstance(Context activity) {
        if (instance == null) {
            synchronized (UBXScannerManager.class) {
                if (instance == null) {
                    instance = new UBXScannerManager(activity);
                }
            }
        }
        return instance;
    }

    @Override
    public void init() {
        mScanManager = new ScanManager();
        boolean b = mScanManager.openScanner();
        if (b) {
            Toast.makeText(activity, "扫描头已初始化", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(activity, "扫描头初始化失败，请重试!", Toast.LENGTH_SHORT).show();
        }
        registerReceiver();
    }

    @Override
    public void recycle() {
        mScanManager.closeScanner();
        activity.unregisterReceiver(receiver);
        this.listener = null;
    }

    @Override
    public void setScannerListener(@NonNull SupporterManager.IScanListener listener) {
        this.listener = listener;
    }

    @Override
    public void sendKeyEvent(KeyEvent key) {

    }

    @Override
    public int getScannerModel() {
        return 0;
    }

    @Override
    public void scannerEnable(boolean enable) {

    }

    @Override
    public void setScanMode(String mode) {

    }

    @Override
    public void setDataTransferType(String type) {

    }

    @Override
    public void singleScan(boolean bool) {
        if (bool) {
            mScanManager.startDecode();
        } else {
            mScanManager.stopDecode();
        }
    }

    @Override
    public void continuousScan(boolean bool) {

    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_DATA_CODE_RECEIVED);
        activity.registerReceiver(receiver, intentFilter);
    }
}
