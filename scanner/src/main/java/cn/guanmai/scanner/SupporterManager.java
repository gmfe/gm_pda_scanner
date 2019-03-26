package cn.guanmai.scanner;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.KeyEvent;

public class SupporterManager<T extends IScannerManager> {
    private static SupporterManager<IScannerManager> instance;
    private IScannerManager scannerManager;

    /**
     * SUNMI: 商米科技 L2 手持扫码设备
     * ZLTD: 直连天地的扫码 PDA, 型号 N2S000
     */
    public enum ScannerSupporter {
        SUNMI, ALPS
    }

    public SupporterManager(Context context, ScannerSupporter supporter) {
        switch (supporter) {
            case SUNMI:
                scannerManager = SunmiScannerManager.getInstance(context);
                break;
            case ALPS:
                scannerManager = AlpsScannerManager.getInstance(context);
                break;
        }
    }

    public void init() {
        scannerManager.init();
    }

    public void recycle() {
        scannerManager.recycle();
    }

    public void setScannerListener(@NonNull IScanListener listener) {
        scannerManager.setScannerListener(listener);
    }

    public void sendKeyEvent(KeyEvent key) {
        scannerManager.sendKeyEvent(key);
    }

    public int getScannerModel() {
        return scannerManager.getScannerModel();
    }

    public void scannerEnable(Boolean enable) {
        scannerManager.scannerEnable(enable);
    }

    public void setScanMode(String mode) {
        scannerManager.setScanMode(mode);
    }

    public void setDataTransferType(String type) {
        scannerManager.setDataTransferType(type);
    }

    public void singleScan(Boolean bool) {
        scannerManager.singleScan(bool);
    }

    public void continuousScan(Boolean bool) {
        scannerManager.continuousScan(bool);
    }

    public interface IScanListener {
        void onScannerResultChange(String result);

        void onScannerServiceConnected();

        void onScannerServiceDisconnected();
    }
}
