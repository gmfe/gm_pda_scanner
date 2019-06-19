package cn.guanmai.scanner;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.KeyEvent;

public class SupporterManager<T extends IScannerManager> {
    private static SupporterManager<IScannerManager> instance;
    private T scannerManager;

    /**
     * SUNMI: 商米科技 L2 手持扫码设备
     * ALPS: 直连天地的扫码 PDA, 型号 N2S000
     * QCOM: 东大集成
     * SEUIC: 东大集成
     * UBX: 优博讯
     */
    public enum ScannerSupporter {
        SUNMI("SUNMI"), alps("alps"), SEUIC("SEUIC"), UBX("UBX"), OTHER("OTHER");

        ScannerSupporter(String name) {
        }
    }

    public SupporterManager(Context context) {
        ScannerSupporter scannerSupporter;
        try {
            scannerSupporter = ScannerSupporter.valueOf(Build.MANUFACTURER);
        } catch (Exception e) {
            scannerSupporter = ScannerSupporter.OTHER;
        }
        switch (scannerSupporter) {
            case SUNMI:
                scannerManager = (T) SunmiScannerManager.getInstance(context);
                break;
            case alps:
                scannerManager = (T) AlpsScannerManager.getInstance(context);
                break;
            case SEUIC:
                scannerManager = (T) SEUICScannerManager.getInstance(context);
                break;
            case UBX:
                scannerManager = (T) UBXScannerManager.getInstance(context);
                break;
            default:
                scannerManager = (T) new OtherScannerManager(context);
                break;
        }
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
