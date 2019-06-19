package cn.guanmai.scanner;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;

import com.sunmi.scanner.IScanInterface;

public class SunmiScannerManager implements IScannerManager {
    private Handler handler = new Handler(Looper.getMainLooper());
    public static final String ACTION_DATA_CODE_RECEIVED = "com.sunmi.scanner.ACTION_DATA_CODE_RECEIVED";
    private static final String DATA = "data";
    private Context activity;
    private static IScanInterface scanInterface;
    private static SunmiScannerManager instance;
    private Intent serviceIntent;
    private SupporterManager.IScanListener listener;
    private boolean singleScanFlag = false;

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            if (listener != null) {
                listener.onScannerServiceConnected();
            } else {
                listener.onScannerInitFail();
            }
            scanInterface = IScanInterface.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            if (listener != null) {
                listener.onScannerServiceDisconnected();
            }
            scanInterface = null;
        }
    };

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    String code = intent.getStringExtra(DATA);
                    if (code != null && !code.isEmpty()) {
                        if (listener != null) {
                            listener.onScannerResultChange(code);
                        }
                        if (singleScanFlag) {
                            singleScanFlag = false;
                            try {
                                scanInterface.stop();
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });
        }
    };

    private SunmiScannerManager(Context activity) {
        this.activity = activity;
    }

    static SunmiScannerManager getInstance(Context activity) {
        if (instance == null) {
            synchronized (SunmiScannerManager.class) {
                if (instance == null) {
                    instance = new SunmiScannerManager(activity);
                }
            }
        }
        return instance;
    }

    @Override
    public void init() {
        bindService();
        registerReceiver();
    }


    @Override
    public void recycle() {
        activity.stopService(serviceIntent);
        activity.unregisterReceiver(receiver);
        this.listener = null;
    }

    @Override
    public void setScannerListener(@NonNull SupporterManager.IScanListener listener) {
        this.listener = listener;
    }

    @Override
    public void sendKeyEvent(KeyEvent key) {
        try {
            scanInterface.sendKeyEvent(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取扫描头类型
     *
     * @return int 100 -> NONE 101 -> P2Lite 102 -> L2-newLane 103 -> L2 -zabra
     */
    @Override
    public int getScannerModel() {
        try {
            return scanInterface.getScannerModel();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public void scannerEnable(boolean enable) {
        Log.d("pda", "此设备不支持该方法");
    }

    @Override
    public void setScanMode(String mode) {

    }

    @Override
    public void setDataTransferType(String type) {

    }

    @Override
    public void singleScan(boolean bool) {
        try {
            if (bool) {
                scanInterface.scan();
                singleScanFlag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void continuousScan(boolean bool) {
        try {
            if (bool) {
                scanInterface.scan();
            } else {
                scanInterface.stop();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void bindService() {
        serviceIntent = new Intent();
        serviceIntent.setPackage("com.sunmi.scanner");
        serviceIntent.setAction("com.sunmi.scanner.IScanInterface");
        activity.bindService(serviceIntent, conn, Service.BIND_AUTO_CREATE);
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_DATA_CODE_RECEIVED);
        activity.registerReceiver(receiver, intentFilter);
    }
}
