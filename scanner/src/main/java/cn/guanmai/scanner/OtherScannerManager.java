package cn.guanmai.scanner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.Toast;
import android.os.Build;

public class OtherScannerManager implements IScannerManager {
    private Handler handler = new Handler(Looper.getMainLooper());
    private Context mContext;
    private SupporterManager.IScanListener listener;


    private static final String START_SCAN_TRIGGER = "cn.guanmai.scanner.START";
    private static final String STOP_SCAN_TRIGGER = "cn.guanmai.scanner.STOP";
    private static final String DATA_RECEIVED_ACTION = "cn.guanmai.scanner.data";
    public static final String SCAN_RESULT = "string";

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    String result = intent.getStringExtra(SCAN_RESULT);
                    if (!TextUtils.isEmpty(result)) {
                        listener.onScannerResultChange(result);
                    }
                }
            });
        }
    };

    OtherScannerManager(Context context) {
        this.mContext = context;
    }

    @Override
    public void init() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DATA_RECEIVED_ACTION);
        // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
        //     mContext.registerReceiver(receiver, intentFilter, Context.RECEIVER_EXPORTED);
        // } else {
        //     mContext.registerReceiver(receiver, intentFilter);
        // }
        BroadcastUtil.registerReceiver(mContext, receiver, intentFilter);
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mContext, "匹配默认配置", Toast.LENGTH_SHORT).show();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        listener.onScannerServiceConnected();
                    }
                }, 800);
            }
        });
    }

    @Override
    public void recycle() {
        mContext.unregisterReceiver(receiver);
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

    }

    @Override
    public void setScanMode(String mode) {

    }

    @Override
    public void setDataTransferType(String type) {

    }

    @Override
    public void singleScan(boolean bool) {
        if (mContext != null) {
            if (bool) {
                mContext.sendBroadcast(new Intent(START_SCAN_TRIGGER));
            } else {
                mContext.sendBroadcast(new Intent(STOP_SCAN_TRIGGER));
            }
        }
    }

    @Override
    public void continuousScan(boolean bool) {

    }
}
