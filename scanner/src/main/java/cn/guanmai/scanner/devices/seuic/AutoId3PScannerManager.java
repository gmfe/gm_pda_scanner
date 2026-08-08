package cn.guanmai.scanner.devices.seuic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;

import cn.guanmai.scanner.BroadcastUtil;
import cn.guanmai.scanner.IScannerManager;
import cn.guanmai.scanner.SupporterManager;

/**
 * 东集 AUTOID 3P(及同代固件)扫码适配。
 *
 * 这类机型在新固件上 {@code ScannerKey} 读不到物理按键、且其轮询循环会把 stopScan 灌爆，
 * 因此不再走 {@code SEUICScannerManager} 的 API 模式，改为与东集自带「扫描工具」一致的
 * 广播模式：扫码服务自行处理物理按键（亮灯、解码），解码后通过广播发结果，APP 注册接收器取结果。
 * 实现参考 {@code PDT90FScannerManager}（同为东集广播模式）。
 *
 * 通过 {@code com.android.scanner.service_settings} 主动把扫码输出强制为广播模式，
 * 避免依赖设备预配置。
 */
public class AutoId3PScannerManager implements IScannerManager {
    private static final String TAG = "AutoId3PScanner";

    /** 扫码结果广播 action（东集默认值） */
    public static final String ACTION_DATA_CODE_RECEIVED = "com.android.server.scannerservice.broadcast";
    /** 扫码服务设置广播 */
    public static final String ACTION_SCANNER_SETTING = "com.android.scanner.service_settings";
    /** 扫码结果 extra key */
    private static final String DATA = "scannerdata";

    private static AutoId3PScannerManager instance;
    private Context activity;
    private SupporterManager.IScanListener listener;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null) {
                return;
            }
            String code = intent.getStringExtra(DATA);
            Log.d(TAG, "scan result: " + code);
            if (!TextUtils.isEmpty(code) && listener != null) {
                listener.onScannerResultChange(code);
            }
        }
    };

    private AutoId3PScannerManager(Context activity) {
        this.activity = activity;
    }

    public static AutoId3PScannerManager getInstance(Context activity) {
        if (instance == null) {
            synchronized (AutoId3PScannerManager.class) {
                if (instance == null) {
                    instance = new AutoId3PScannerManager(activity);
                }
            }
        }
        return instance;
    }

    @Override
    public void init() {
        initSetting();
        registerReceiver();
        if (listener != null) {
            listener.onScannerServiceConnected();
        }
    }

    /** 强制把扫码服务输出设为广播模式（action/key 用东集默认值），不依赖设备预配置 */
    private void initSetting() {
        try {
            Intent intent = new Intent(ACTION_SCANNER_SETTING);
            intent.putExtra("endchar", "NONE");
            intent.putExtra("scan_continue", false);
            intent.putExtra("barcode_send_mode", "BROADCAST");
            intent.putExtra("action_barcode_broadcast", ACTION_DATA_CODE_RECEIVED);
            intent.putExtra("key_barcode_broadcast", DATA);
            activity.sendBroadcast(intent);
        } catch (Exception e) {
            Log.w(TAG, "initSetting failed", e);
        }
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_DATA_CODE_RECEIVED);
        intentFilter.setPriority(Integer.MAX_VALUE);
        BroadcastUtil.registerReceiver(activity, receiver, intentFilter);
    }

    @Override
    public void recycle() {
        try {
            activity.unregisterReceiver(receiver);
        } catch (Exception e) {
            Log.w(TAG, "unregister receiver failed", e);
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
        // 通过广播触发扫码服务开始/结束
        if (activity != null) {
            activity.sendBroadcast(new Intent(bool ? "com.scan.onStartScan" : "com.scan.onEndScan"));
        }
    }

    @Override
    public void continuousScan(boolean bool) {
        Intent intent = new Intent(ACTION_SCANNER_SETTING);
        intent.putExtra("scan_continue", bool);
        activity.sendBroadcast(intent);
    }
}
