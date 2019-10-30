package cn.guanmai.scanner;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;

import cn.guanmai.scanner.devices.pda.PDAScannerManager;
import cn.guanmai.scanner.devices.jb.HT380KScannerManager;
import cn.guanmai.scanner.devices.newland.MT6210ScannerManager;
import cn.guanmai.scanner.devices.sg6900.SG6900ScannerManager;

public class SupporterManager<T extends IScannerManager> {
    private static SupporterManager<IScannerManager> instance;
    private T scannerManager;

    /**
     * SUNMI: 商米科技 L2 手持扫码设备
     * ALPS: 智联天地的扫码 PDA, 型号 N2S000
     * SEUIC: 东大集成
     * UBX: 优博讯
     * 型号SG6900: 深圳市思感科技有限公司
     * 型号HT380K: 深圳市捷宝科技有限公司
     * 型号 NLS-MT6210，制造商 Newland
     * 型号 NLS-MT9210，制造商 Newland
     */
    public enum ScannerSupporter {
        SUNMI("SUNMI"), alps("alps"), SEUIC("SEUIC"), UBX("UBX"), OTHER("OTHER"), idata("idata"), SG6900("SG6900"), HT380K("HT380K"), MT6210("NLS-MT6210"), MT9210("NLS-MT9210"), PDA("PDA");

        private String name;

        ScannerSupporter(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    private ScannerSupporter getSupporter() {
        Log.e("TAG", "型号" + Build.MODEL + "，厂商" + Build.MANUFACTURER);
        // 先根据型号适配
        for (ScannerSupporter supporter : ScannerSupporter.values()) {
            if (supporter.getName().equals(Build.MODEL)) {
                return supporter;
            }
        }
        // 型号没有，再尝试根据厂商适配
        for (ScannerSupporter supporter : ScannerSupporter.values()) {
            if (supporter.getName().equals(Build.MANUFACTURER)) {
                return supporter;
            }
        }
        return ScannerSupporter.OTHER;
    }

    public SupporterManager(Context context, @NonNull IScanListener listener) {
        ScannerSupporter scannerSupporter = getSupporter();
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
            case MT6210:
            case MT9210:
                scannerManager = (T) MT6210ScannerManager.getInstance(context);
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
            case PDA:
                scannerManager = (T) PDAScannerManager.getInstance(context);
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
