package cn.guanmai.gm_pda_scanner;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import cn.guanmai.scanner.AlpsScannerManager;
import cn.guanmai.scanner.SEUICScannerManager;
import cn.guanmai.scanner.SunmiScannerManager;
import cn.guanmai.scanner.SupporterManager;
import cn.guanmai.scanner.UBXScannerManager;

public class MainActivity extends AppCompatActivity {
    private SupporterManager mScannerManager;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = findViewById(R.id.result);
        Button clearText = findViewById(R.id.clear);
        clearText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTextView.setText("扫描结果");
            }
        });
        initScanner();
    }

    private void initScanner() {
        mScannerManager = new SupporterManager(this);
        mScannerManager.setScannerListener(new SupporterManager.IScanListener() {
            @Override
            public void onScannerResultChange(String result) {
                String s = mTextView.getText().toString() + "\n";
                mTextView.setText(s + result);
            }

            @Override
            public void onScannerServiceConnected() {

            }

            @Override
            public void onScannerServiceDisconnected() {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mScannerManager != null) {
            mScannerManager.recycle();
        }
    }
}
