package cn.guanmai.scanner;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.KeyEvent;

import cn.guanmai.scanner.devices.jb.HT380KScannerManager;
import cn.guanmai.scanner.devices.sg6900.SG6900ScannerManager;

public class SupporterManager<T extends IScannerManager> {
    private static SupporterManager<IScannerManager> instance;
    private T scannerManager;

    /**
     * SUNMI: 商米科技 L2 手持扫码设备
     * ALPS: 直连天地的扫码 PDA, 型号 N2S000
     * QCOM: 东大集成
     * SEUIC: 东大集成
     * UBX: 优博讯
     * 型号SG6900: 深圳市思感科技有限公司
     * 型号HT380K: 深圳市捷宝科技有限公司
     */
    public enum ScannerSupporter {
        SUNMI("SUNMI"), alps("alps"), SEUIC("SEUIC"), UBX("UBX"), OTHER("OTHER"), idata("idata"), SG6900("SG6900"),
        HT380K("HT380K");

        ScannerSupporter(String name) {
        }
    }

    public SupporterManager(Context context, @NonNull IScanListener listener) {
        ScannerSupporter scannerSupporter;
        try {
            scannerSupporter = ScannerSupporter.valueOf(Build.MODEL); // 先匹配型号
        } catch (Exception e) {
            try {
                scannerSupporter = ScannerSupporter.valueOf(Build.MANUFACTURER); // 如果没有，则匹配厂商
            } catch (Exception e1) {
                scannerSupporter = ScannerSupporter.OTHER;
            }
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
            case idata:
                scannerManager = (T) IDataScannerManager.getInstance(context);
                break;
            case SG6900:
                scannerManager = (T) SG6900ScannerManager.getInstance(context);
                break;
            case HT380K:
                scannerManager = (T) HT380KScannerManager.getInstance(context);
                break;
            default:
                scannerManager = (T) new OtherScannerManager(context);
                break;
        }
        scannerManager.setScannerListener(listener);
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

        void onScannerInitFail();
    }
}
