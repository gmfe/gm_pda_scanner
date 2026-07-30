package cn.guanmai.scanner;

import android.content.Context;
import android.content.IntentFilter;
import android.os.Build;
import android.util.Log;

/**
 * 专门用于 iData T1S（Android 14）机型。
 *
 * <p>复用 {@link IDataScannerManager} 的全部逻辑，仅覆盖 {@link #registerReceiver()}：
 * <ul>
 *   <li>Android 13+（targetSdk 33+）运行时注册“非系统广播”的接收器时，
 *       必须显式指定 {@link Context#RECEIVER_EXPORTED} 或
 *       {@link Context#RECEIVER_NOT_EXPORTED}，否则会抛 {@link SecurityException} 导致闪退。
 *       T1S 的扫码结果由 iData 扫描服务跨进程广播发出，因此必须用
 *       {@link Context#RECEIVER_EXPORTED}（用 NOT_EXPORTED 会收不到扫码结果）。</li>
 *   <li>额外用 try/catch 兜底：即便标志判断出现偏差，也保证不会闪退，
 *       最多是扫码失效（此时看 logcat 的 "IDataT1S" 标签排查）。</li>
 * </ul>
 *
 * <p>注：idata / T1 仍使用原 {@link IDataScannerManager}（老机型，Android 版本较低），
 * 本类只影响 T1S，避免改动波及其他已验证机型。
 */
public class IDataT1SScannerManager extends IDataScannerManager {

    private static IDataT1SScannerManager instance;

    private IDataT1SScannerManager(Context context) {
        super(context);
    }

    public static synchronized IDataT1SScannerManager getInstance(Context context) {
        if (instance == null) {
            instance = new IDataT1SScannerManager(context);
        }
        return instance;
    }

    @Override
    protected void registerReceiver() {
        try {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(RES_ACTION);
            if (Build.VERSION.SDK_INT >= 34) {
                // Android 14：必须显式声明导出标志，否则 SecurityException 闪退；
                // 扫码结果来自 iData 扫描服务（跨进程），故用 RECEIVER_EXPORTED。
                activity.registerReceiver(receiver, intentFilter, Context.RECEIVER_EXPORTED);
            } else {
                activity.registerReceiver(receiver, intentFilter);
            }
        } catch (Exception e) {
            // 兜底：保证不会因为注册广播而闪退；若走到这里，扫码会失效，需结合日志排查。
            Log.e("IDataT1S", "registerReceiver failed", e);
        }
    }
}
