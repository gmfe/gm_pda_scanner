package cn.guanmai.scanner;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.widget.Toast;

public class OtherScannerManager implements IScannerManager {
    private Context mContext;

    public OtherScannerManager(Context context) {
        this.mContext = context;
    }

    @Override
    public void init() {
        Toast.makeText(mContext, "无法获取扫描头", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void recycle() {

    }

    @Override
    public void setScannerListener(@NonNull SupporterManager.IScanListener listener) {

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

    }

    @Override
    public void continuousScan(boolean bool) {

    }
}
