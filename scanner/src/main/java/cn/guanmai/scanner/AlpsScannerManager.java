package cn.guanmai.scanner;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.KeyEvent;

import com.zltd.industry.ScannerManager;

import java.io.UnsupportedEncodingException;

public class AlpsScannerManager implements IScannerManager {
    private Handler handler = new Handler();
    private Context activity;
    private static AlpsScannerManager instance;
    private ScannerManager mScannerManager;
    private SupporterManager.IScanListener listener;

    private ScannerManager.IScannerStatusListener mIScannerStatusListener = new ScannerManager.IScannerStatusListener() {
        @Override
        public void onScannerStatusChanage(int i) {

        }

        @Override
        public void onScannerResultChanage(final byte[] bytes) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    String s = null;
                    try {
                        s = new String(bytes, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    if (s != null) {
                        listener.onScannerResultChange(s);
                    }
                }
            });
        }
    };

    private AlpsScannerManager(Context activity) {
        this.activity = activity;
    }

    static AlpsScannerManager getInstance(Context activity) {
        if (instance == null) {
            synchronized (AlpsScannerManager.class) {
                if (instance == null) {
                    instance = new AlpsScannerManager(activity);
                }
            }
        }
        return instance;
    }

    @Override
    public void init() {
        mScannerManager = ScannerManager.getInstance();
        mScannerManager.scannerEnable(true);
        mScannerManager.setScanMode(ScannerManager.SCAN_SINGLE_MODE);
        mScannerManager.setDataTransferType(ScannerManager.TRANSFER_BY_API);
        mScannerManager.addScannerStatusListener(mIScannerStatusListener);
    }

    @Override
    public void recycle() {
        if (mScannerManager != null) {
            mScannerManager.removeScannerStatusListener(mIScannerStatusListener);
        }
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
        return -1;
    }

    @Override
    public void scannerEnable(boolean enable) {
        mScannerManager.scannerEnable(enable);
    }

    @Override
    public void setScanMode(String mode) {
        switch (mode) {
            case "single":
                mScannerManager.setScanMode(ScannerManager.SCAN_SINGLE_MODE);
                break;
            case "continuous":
                mScannerManager.setScanMode(ScannerManager.SCAN_CONTINUOUS_MODE);
                break;
            case "keyHold":
                mScannerManager.setScanMode(ScannerManager.SCAN_KEY_HOLD_MODE);
                break;
            default:
                break;
        }

    }

    @Override
    public void setDataTransferType(String type) {
        switch (type) {
            case "api":
                mScannerManager.setDataTransferType(ScannerManager.TRANSFER_BY_API);
                break;
            case "editText":
                mScannerManager.setDataTransferType(ScannerManager.TRANSFER_BY_EDITTEXT);
                break;
            case "key":
                mScannerManager.setDataTransferType(ScannerManager.TRANSFER_BY_KEY);
                break;
            default:
                break;
        }

    }

    @Override
    public void singleScan(boolean bool) {
        if (bool) {
            mScannerManager.startKeyHoldScan();
        } else {
            mScannerManager.stopKeyHoldScan();
        }
    }

    @Override
    public void continuousScan(boolean bool) {
        if (bool) {
            mScannerManager.startContinuousScan();
        } else {
            mScannerManager.stopContinuousScan();
        }
    }
}
