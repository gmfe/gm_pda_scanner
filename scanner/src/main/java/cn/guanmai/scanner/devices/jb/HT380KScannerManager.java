package cn.guanmai.scanner.devices.jb;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.view.KeyEvent;

import cn.guanmai.scanner.IScannerManager;
import cn.guanmai.scanner.SupporterManager;

public class HT380KScannerManager implements IScannerManager {
    private static HT380KScannerManager instance;
    private Context activity;
    public static final String ACTION_DATA_CODE_RECEIVED = "com.android.server.scannerservice.broadcast";
    private static final String DATA = "scannerdata";
    private SupporterManager.IScanListener listener;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String code = intent.getStringExtra(DATA);
            if (code != null && !code.isEmpty()) {
                listener.onScannerResultChange(code);
            }
        }
    };

    private HT380KScannerManager(Context activity) {
        this.activity = activity;
    }

    public static HT380KScannerManager getInstance(Context activity) {
        if (instance == null) {
            synchronized (HT380KScannerManager.class) {
                if (instance == null) {
                    instance = new HT380KScannerManager(activity);
                }
            }
        }
        return instance;
    }

    @Override
    public void init() {
        registerReceiver();
    }

    @Override
    public void recycle() {

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
