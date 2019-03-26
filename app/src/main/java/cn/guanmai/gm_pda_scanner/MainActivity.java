package cn.guanmai.gm_pda_scanner;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cn.guanmai.scanner.AlpsScannerManager;
import cn.guanmai.scanner.SunmiScannerManager;
import cn.guanmai.scanner.SupporterManager;

public class MainActivity extends AppCompatActivity {
    private SupporterManager mScannerManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initScanner();
    }

    private void initScanner() {
        String deviceBrand = Build.BRAND;
        if ("SUNMI".equals(deviceBrand)) {
            mScannerManager = new SupporterManager<SunmiScannerManager>(this, SupporterManager.ScannerSupporter.SUNMI);
        } else if ("alps".equals(deviceBrand)) {
            mScannerManager = new SupporterManager<AlpsScannerManager>(this, SupporterManager.ScannerSupporter.ALPS);
        }
        if (mScannerManager != null) {
            mScannerManager.setScannerListener(new SupporterManager.IScanListener() {
                @Override
                public void onScannerResultChange(String result) {

                }

                @Override
                public void onScannerServiceConnected() {

                }

                @Override
                public void onScannerServiceDisconnected() {

                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mScannerManager != null) {
            mScannerManager.recycle();
        }
    }
}
