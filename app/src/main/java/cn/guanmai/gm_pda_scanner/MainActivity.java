package cn.guanmai.gm_pda_scanner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import cn.guanmai.scanner.SupporterManager;

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
        mScannerManager = new SupporterManager(this, new SupporterManager.IScanListener() {
            @Override
            public void onScannerResultChange(String result) {
                String s = mTextView.getText().toString() + "\n";
                mTextView.setText(s + result);
            }

            @Override
            public void onScannerServiceConnected() {
                Toast.makeText(MainActivity.this, "扫描头初始化成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onScannerServiceDisconnected() {

            }

            @Override
            public void onScannerInitFail() {
                Toast.makeText(MainActivity.this, "无法获取扫描头，请重试！", Toast.LENGTH_SHORT).show();
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
