package cn.guanmai.scanner;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;

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
        // 东集新固件（如 CRUISE GeP，Android 16）上，物理扫码键事件由系统 hardkey 服务
        // 路由给预装「扫描工具」(com.seuic.scanner.activity) 内的 ScanService 处理；
        // 该服务不常驻时按扫码键无响应（下方 ScannerKey.open() 返回 -1 读不到按键）。
        // 本管理器只会运行在东集设备上（SupporterManager 按厂商/型号分发到此处），
        // 启动时显式拉起该服务，解除必须手动打开「扫描工具」的人工依赖。
        // 移植自 gm_android_webview MR !4（feature/driver_seuic_scan_fix），下沉到库统一收敛。
        startScanToolService();

        mScanner = ScannerFactory.getScanner(activity);
        boolean isOpen = mScanner.open();
        if (isOpen) {
            listener.onScannerServiceConnected();
        } else {
            listener.onScannerInitFail();
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

    /**
     * 拉起东集预装「扫描工具」的 ScanService（扫码按键接收服务）。
     * 服务 exported=true 且常驻即可（界面可退出）；重复启动为系统幂等操作。
     */
    private void startScanToolService() {
        try {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.seuic.scanner.activity",
                    "com.seuic.scanner.service.ScanService"));
            // Android 8+ 启动外部服务需前台化（服务自身已声明 foregroundServiceType）
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                activity.startForegroundService(intent);
            } else {
                activity.startService(intent);
            }
        } catch (Exception e) {
            // 老固件无该组件或系统限制导致启动失败时，仅记录，不阻断扫描初始化
            Log.e("Scanner", "拉起东集 ScanService 失败", e);
        }
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
