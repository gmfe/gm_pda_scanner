package cn.guanmai.scanner.devices.sg6900;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.view.KeyEvent;

import java.io.IOException;

import cn.guanmai.scanner.IScannerManager;
import cn.guanmai.scanner.SupporterManager;

public class SG6900ScannerManager implements IScannerManager {
    private Context activity;
    private static SG6900ScannerManager instance;
    private SupporterManager.IScanListener listener;
    private ScanThread scanThread;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == ScanThread.SCAN) {
                String data = msg.getData().getString("data");
                if (data != null && !data.isEmpty()) {
                    listener.onScannerResultChange(data);
                }
            }
        }
    };

    private SG6900ScannerManager(Context activity) {
        this.activity = activity;
    }

    public static SG6900ScannerManager getInstance(Context activity) {
        if (instance == null) {
            synchronized (SG6900ScannerManager.class) {
                if (instance == null) {
                    instance = new SG6900ScannerManager(activity);
                }
            }
        }
        return instance;
    }

    @Override
    public void init() {
        try {
            scanThread = new ScanThread(mHandler);
            listener.onScannerServiceConnected();
        } catch (IOException e) {
            listener.onScannerInitFail();
            return;
        }
        scanThread.start();
    }

    @Override
    public void recycle() {
        scanThread.close();
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
        scanThread.scan();
    }

    @Override
    public void continuousScan(boolean bool) {

    }
}
