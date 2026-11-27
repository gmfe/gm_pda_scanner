package cn.guanmai.scanner.devices.ct58;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.device.ScanManager;
import android.device.scanner.configuration.PropertyID;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.KeyEvent;

import cn.guanmai.scanner.IScannerManager;
import cn.guanmai.scanner.LogUtil;
import cn.guanmai.scanner.SupporterManager;

public class CT58ScannerManager implements IScannerManager {
    private Handler handler = new Handler(Looper.getMainLooper());
    private Context mContext;
    private static CT58ScannerManager instance;
    private SupporterManager.IScanListener listener;

    private static String ACTION_DATA_CODE_RECEIVED = ScanManager.ACTION_DECODE;
    public static String SCAN_RESULT = ScanManager.BARCODE_STRING_TAG;

    ScanManager mScanManager = new ScanManager();

    // 广播接收器是否处于已注册状态：Activity 重建（转屏等配置变更）会再次走 init/onDestroy，
    // 没有该标记时 unregisterReceiver 会对未注册的接收器抛 IllegalArgumentException 直接 FATAL 崩溃
    private boolean isReceiverRegistered = false;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    String result = intent.getStringExtra(SCAN_RESULT);
                    LogUtil.printLog("[RECV] data=" + result);
                    if (!TextUtils.isEmpty(result)) {
                        listener.onScannerResultChange(result);
                    }
                }
            });
        }
    };

    private CT58ScannerManager(Context activity) {
        this.mContext = activity;
    }

    public static CT58ScannerManager getInstance(Context activity) {
        if (instance == null) {
            synchronized (CT58ScannerManager.class) {
                if (instance == null) {
                    instance = new CT58ScannerManager(activity);
                }
            }
        }
        return instance;
    }

    @Override
    public void init() {
        //设置扫描模式
        mScanManager.switchOutputMode(0);
        //设置扫描参数
        String[] params = mScanManager.getParameterString(new int[]{PropertyID.WEDGE_INTENT_ACTION_NAME, PropertyID.WEDGE_INTENT_DATA_STRING_TAG});
        if (params != null && params.length == 2) {
            ACTION_DATA_CODE_RECEIVED = params[0];
            SCAN_RESULT = params[1];
        }

        if (TextUtils.isEmpty(ACTION_DATA_CODE_RECEIVED)) {
            ACTION_DATA_CODE_RECEIVED = ScanManager.ACTION_DECODE;
        }
        if (TextUtils.isEmpty(SCAN_RESULT)) {
            SCAN_RESULT = ScanManager.BARCODE_STRING_TAG;
        }

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_DATA_CODE_RECEIVED);
        // Activity 重建会再次 init：先安全注销旧注册再重新注册，避免重复注册导致扫码回调翻倍；
        // 注册过程不能向外抛异常——否则 MainActivity.onCreate 中断，后续 destroy 时
        // recycle 注销"从未注册成功"的接收器会触发 FATAL 崩溃
        safeUnregister();
        try {
            mContext.registerReceiver(receiver, intentFilter);
            isReceiverRegistered = true;
        } catch (Exception e) {
            // 注册失败（如 Activity 已销毁等竞态）：保持未注册状态，本次扫码不可用但不崩溃
            isReceiverRegistered = false;
        }

        listener.onScannerServiceConnected();
    }

    @Override
    public void recycle() {
        safeUnregister();
    }

    // 幂等注销：仅在已注册时注销，并吞掉"未注册"异常（生命周期竞态下系统可能已注销）
    private void safeUnregister() {
        if (isReceiverRegistered) {
            try {
                mContext.unregisterReceiver(receiver);
            } catch (IllegalArgumentException e) {
                // 接收器已处于未注册状态，无需处理
            }
            isReceiverRegistered = false;
        }
    }

    @Override
    public void setScannerListener(@NonNull SupporterManager.IScanListener listener) {
        this.listener = listener;
    }

    @Override
    public void sendKeyEvent(KeyEvent key) {
        LogUtil.printLog("[sendKeyEvent] value=" + key);
    }

    @Override
    public int getScannerModel() {
        LogUtil.printLog("[getScannerModel] value=" + 0);
        return 0;
    }

    @Override
    public void scannerEnable(boolean enable) {
        LogUtil.printLog("[scannerEnable] value=" + enable);
    }

    @Override
    public void setScanMode(String mode) {
        LogUtil.printLog("[setScanMode] value=" + mode);
    }

    @Override
    public void setDataTransferType(String type) {
        LogUtil.printLog("[setDataTransferType] value=" + type);
    }

    @Override
    public void singleScan(boolean bool) {
        LogUtil.printLog("[singleScan] value=" + bool);
        if (bool) {
            mScanManager.startDecode();
        } else {
            mScanManager.stopDecode();
        }
    }

    @Override
    public void continuousScan(boolean bool) {
        LogUtil.printLog("[continuousScan] value=" + bool);
    }
}
