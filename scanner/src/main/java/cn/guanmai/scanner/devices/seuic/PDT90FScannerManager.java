package cn.guanmai.scanner.devices.seuic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.view.KeyEvent;

import cn.guanmai.scanner.IScannerManager;
import cn.guanmai.scanner.SupporterManager;

public class PDT90FScannerManager implements IScannerManager {
    private static PDT90FScannerManager instance;
    private Context activity;
    public static final String ACTION_DATA_CODE_RECEIVED = "com.android.server.scannerservice.broadcast";
    public static final String ACTION_SCANNER_SETTING = "com.android.scanner.service_settings";
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

    private PDT90FScannerManager(Context activity) {
        this.activity = activity;
    }

    public static PDT90FScannerManager getInstance(Context activity) {
        if (instance == null) {
            synchronized (PDT90FScannerManager.class) {
                if (instance == null) {
                    instance = new PDT90FScannerManager(activity);
                }
            }
        }
        return instance;
    }

    @Override
    public void init() {
        initSetting();
        registerReceiver();
        listener.onScannerServiceConnected();
    }

    private void initSetting() {
        Intent intent = new Intent(ACTION_SCANNER_SETTING);
        intent.putExtra("endchar", "NONE");
        intent.putExtra("scan_continue", false);
        intent.putExtra("barcode_send_mode","BROADCAST");
        intent.putExtra("action_barcode_broadcast", "com.android.server.scannerservice.broadcast");
        intent.putExtra("key_barcode_broadcast", "scannerdata");
        activity.sendBroadcast(intent);
    }

    @Override
    public void recycle() {
        activity.unregisterReceiver(receiver);
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
        Intent intent = new Intent("com.android.scanner.ENABLED");
        intent.putExtra("enabled", enable);
        activity.sendBroadcast(intent);
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
        Intent intent = new Intent(ACTION_SCANNER_SETTING);
        intent.putExtra("scan_continue", bool);
        activity.sendBroadcast(intent);
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_DATA_CODE_RECEIVED);
        intentFilter.setPriority(Integer.MAX_VALUE);
        activity.registerReceiver(receiver, intentFilter);
    }
}
