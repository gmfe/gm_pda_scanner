package cn.guanmai.scanner;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.seuic.scanner.DecodeInfo;
import com.seuic.scanner.DecodeInfoCallBack;
import com.seuic.scanner.Scanner;
import com.seuic.scanner.ScannerFactory;
import com.seuic.scanner.ScannerKey;

public class SEUICScannerManager implements IScannerManager {
    private Handler handler = new Handler(Looper.getMainLooper());
    private Context activity;
    private static SEUICScannerManager instance;
    private Scanner mScanner;
    private SupporterManager.IScanListener listener;

    private DecodeInfoCallBack mDecodeInfoCallBack = new DecodeInfoCallBack() {
        @Override
        public void onDecodeComplete(final DecodeInfo decodeInfo) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    String s = decodeInfo.barcode;
                    Log.e("TAG", "result=" + s);
                    if (s != null) {
                        listener.onScannerResultChange(s);
                    }
                }
            });
        }
    };

    private SEUICScannerManager(Context activity) {
        this.activity = activity;
    }

    static SEUICScannerManager getInstance(Context activity) {
        if (instance == null) {
            synchronized (SEUICScannerManager.class) {
                if (instance == null) {
                    instance = new SEUICScannerManager(activity);
                }
            }
        }
        return instance;
    }

    @Override
    public void init() {
        mScanner = ScannerFactory.getScanner(activity);
        boolean isOpen = mScanner.open();
        if (isOpen) {
            Toast.makeText(activity, "扫描头已初始化", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(activity, "扫描头初始化失败，请重试!", Toast.LENGTH_SHORT).show();
        }
        mScanner.setDecodeInfoCallBack(mDecodeInfoCallBack);
        new Thread(new Runnable() {
            @Override
            public void run() {
                int ret1 = ScannerKey.open();
                if (ret1 > -1) {
                    while (true) {
                        int ret = ScannerKey.getKeyEvent();
                        if (ret > -1) {
                            switch (ret) {
                                case ScannerKey.KEY_DOWN:
                                    mScanner.startScan();
                                    break;
                                case ScannerKey.KEY_UP:
                                    mScanner.stopScan();
                                    break;
                            }
                        }
                    }
                }
            }
        }).start();
    }

    @Override
    public void recycle() {
        mScanner.close();
        ScannerKey.close();
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
        if (enable) {
            mScanner.enable();
        } else {
            mScanner.disable();
        }
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
            mScanner.startScan();
        } else {
            mScanner.stopScan();
        }
    }

    @Override
    public void continuousScan(boolean bool) {

    }
}
