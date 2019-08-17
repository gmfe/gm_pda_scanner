package cn.guanmai.scanner;

import android.content.Context;
import android.content.Intent;

public class IDataScannerInterface {
    /********************************************扫描接口定义常量******************************/
    //打开与关闭扫描头
    public static final String KEY_BARCODE_ENABLESCANNER_ACTION = "android.intent.action.BARCODESCAN";
    //开始扫描
    public static final String KEY_BARCODE_STARTSCAN_ACTION = "android.intent.action.BARCODESTARTSCAN";
    //停止扫描
    public static final String KEY_BARCODE_STOPSCAN_ACTION = "android.intent.action.BARCODESTOPSCAN";

    //锁定扫描键
    public static final String KEY_LOCK_SCAN_ACTION = "android.intent.action.BARCODELOCKSCANKEY";
    //解锁扫描键
    public static final String KEY_UNLOCK_SCAN_ACTION = "android.intent.action.BARCODEUNLOCKSCANKEY";
    //声音android.intent.action.BEEP
    public static final String KEY_BEEP_ACTION = "android.intent.action.BEEP";
    //扫描失败提示音
    public static final String KEY_FAILUREBEEP_ACTION = "android.intent.action.FAILUREBEEP";
    //震动android.intent.action.VIBRATE
    public static final String KEY_VIBRATE_ACTION = "android.intent.action.VIBRATE";
    //是否广播模式
    public static final String KEY_OUTPUT_ACTION = "android.intent.action.BARCODEOUTPUT";
    //广播设置编码格式
    public static final String KEY_CHARSET_ACTION = "android.intent.actionCHARSET";
    //省电模式
    public static final String KEY_POWER_ACTION = "android.intent.action.POWER";
    //附加内容
    public static final String KEY_TERMINATOR_ACTION = "android.intent.TERMINATOR";
    //通知栏图标显示android.intent.action.SHOWNOTICEICON
    public static final String KEY_SHOWNOTICEICON_ACTION  = "android.intent.action.SHOWNOTICEICON";
    //APP图标显示android.intent.action.SHOWAPPICON
    public static final String KEY_SHOWICON_ACTION  = "android.intent.action.SHOWAPPICON";

    //打开扫描设置界面
    public static final String KEY_SHOWISCANUI = "com.android.auto.iscan.show_setting_ui";

    //添加前缀
    public static final String KEY_PREFIX_ACTION = "android.intent.action.PREFIX";
    //后缀
    public static final String KEY_SUFFIX_ACTION = "android.intent.action.SUFFIX";
    //截取左字符
    public static final String KEY_TRIMLEFT_ACTION = "android.intent.action.TRIMLEFT";
    //截取右字符
    public static final String KEY_TRIMRIGHT_ACTION = "android.intent.action.TRIMRIGHT";
    //右上侧Led灯光控制
    public static final String KEY_LIGHT_ACTION = "android.intent.action.LIGHT";
    //设置超时时间
    public static final String KEY_TIMEOUT_ACTION = "android.intent.action.TIMEOUT";
    //过滤特定字符
    public static final String KEY_FILTERCHARACTER_ACTION = "android.intent.action.FILTERCHARACTER";
    //连扫
    public static final String KEY_CONTINUCESCAN_ACTION = "android.intent.action.BARCODECONTINUCESCAN";
    //连续扫描间隔时间
    public static final String KEY_INTERVALTIME_ACTION = "android.intent.action.INTERVALTIME";
    //是否删除编辑框内容
    public static final String KEY_DELELCTED_ACTION = "android.intent.action.DELELCTED";
    //恢复默认设置
    public static final String KEY_RESET_ACTION = "android.intent.action.RESET";
    //扫描按键配置
    public static final String SCANKEY_CONFIG_ACTION = "android.intent.action.scankeyConfig";

    //扫描失败广播
    public static final String KEY_FAILUREBROADCAST_ACTION = "android.intent.action.FAILUREBROADCAST";

    //设置解码数量
    public static final String KEY_SETMAXMULTIREADCOUNT_ACTION = "android.intent.action.MAXMULTIREADCOUNT";
    /****************************************************************************************************/

    /********************************************系统接口定义常量******************************/
    static final String  SET_STATUSBAR_EXPAND = "com.android.set.statusbar_expand";
    static final String  SET_USB_DEBUG = "com.android.set.usb_debug";
    static final String  SET_INSTALL_PACKAGE = "com.android.set.install.package";
    static final String  SET_SCREEN_LOCK = "com.android.set.screen_lock";
    static final String  SET_CFG_WAKEUP_ANYKEY = "com.android.set.cfg.wakeup.anykey";
    static final String  SET_UNINSTALL_PACKAGE= "com.android.set.uninstall.package";
    static final String  SET_SYSTEM_TIME="com.android.set.system.time";
    static final String  SET_KEYBOARD_CHANGE = "com.android.disable.keyboard.change";
    static final String SET_INSTALL_PACKAGE_WITH_SILENCE = "com.android.set.install.packege.with.silence";
    static final String SET_INSTALL_PACKAGE_EXTRA_APK_PATH = "com.android.set.install.packege.extra.apk.path";
    static final String SET_INSTALL_PACKAGE_EXTRA_TIPS_FORMAT = "com.android.set.install.packege.extra.tips.format";
    static final String SET_SIMULATION_KEYBOARD = "com.android.simulation.keyboard";
    static final String SET_SIMULATION_KEYBOARD_STRING = "com.android.simulation.keyboard.string";
    /****************************************************************************************************/

    private Context mContext;
    private static IDataScannerInterface androidjni;

    public IDataScannerInterface(Context context) {
        mContext = context;

    }

    /*********扫描 控制接口*********************/

    /*************************************/

    //	1.打开扫描设置界面
    public void ShowUI(){
        if(mContext != null){
            Intent intent = new Intent(KEY_SHOWISCANUI);
            mContext.sendBroadcast(intent);
        }
    }

    //	2.打开扫描头电源
    public void open(){
        if(mContext != null){
            Intent intent = new Intent(KEY_BARCODE_ENABLESCANNER_ACTION);
            intent.putExtra(KEY_BARCODE_ENABLESCANNER_ACTION, true);
            mContext.sendBroadcast(intent);
        }
    }

    //2.关闭扫描头电源
    public void  close(){
        if(mContext != null){
            Intent intent = new Intent(KEY_BARCODE_ENABLESCANNER_ACTION);
            intent.putExtra(KEY_BARCODE_ENABLESCANNER_ACTION, false);
            mContext.sendBroadcast(intent);
        }

    }

    // 3. 触发扫描头，扫描头出光
	/*
	 * 此函数和 scan_stop 配合使用可以在程序中软件触发扫描头。当扫描头处于空闲状
 态时,调用 scan_start 可以触发扫描头出光扫描。扫描完毕或超时后,必须调用
scan_start 恢复扫描头状态。
	 *
	 * */
    public void  scan_start(){

        if(mContext != null){
            Intent intent = new Intent(KEY_BARCODE_STARTSCAN_ACTION);
            mContext.sendBroadcast(intent);
        }
    }

    //4.停止扫描头解码，扫描头灭光
    /**
     * 此函数和 scan_stop 配合使用可以在程序中软件触发扫描头。当应用程序调用
     scan_start 触发扫描头出光扫描后, 必须调用 scan_stop 恢复扫描头状态。

     */
    public void scan_stop(){
        if(mContext != null){
            Intent intent = new Intent(KEY_BARCODE_STOPSCAN_ACTION);
            mContext.sendBroadcast(intent);
        }
    }

    /**锁定设备的扫描按键，锁定后，只能通过iScan定义的扫描按键控制扫描，用户无法自定义按键。
     */
    public void  lockScanKey(){
        if(mContext != null){
            Intent intent = new Intent(KEY_LOCK_SCAN_ACTION);
            mContext.sendBroadcast(intent);
        }
    }

    /******
     * 解除对扫描按键的锁定。解除后iScan无法控制扫描键，用户可自定义按键。
     */
    public void unlockScanKey(){
        if(mContext != null){
            Intent intent = new Intent(KEY_UNLOCK_SCAN_ACTION);
            mContext.sendBroadcast(intent);
        }
    }

    /**KEY_CHARSET_ACTION   广播设置编码格式
     * 0  <item>ASCII</item>
     1  <item>GB2312</item>
     2  <item>GBK</item>
     3  <item>GB18030</item>
     4  <item>UTF-8</item>*/

    public void setCharSetMode(int mode){
        if(mContext != null){
            Intent intent = new Intent(KEY_CHARSET_ACTION);
            intent.putExtra(KEY_CHARSET_ACTION, mode);
            mContext.sendBroadcast(intent);
        }
    }


    //设置一次最多解码数量
    public void setMaxMultireadCount(int mode){
        if(mContext != null){
            Intent intent = new Intent(KEY_SETMAXMULTIREADCOUNT_ACTION);
            intent.putExtra(KEY_SETMAXMULTIREADCOUNT_ACTION, mode);
            mContext.sendBroadcast(intent);
        }
    }




    /**扫描头的输出模式
     * mode 0:扫描结果直接发送到焦点编辑框内
     * mode 1:扫描结果以广播模式发送，应用程序需要注册action为“android.intent.action.SCANRESULT”的广播接收器，在广播机的 onReceive(Context context, Intent arg1) 方法中,通过如下语句
     String  barocode=arg1.getStringExtra("value");
     int barocodelen=arg1.getIntExtra("length",0);
     分别获得 条码值,条码长度,条码类型
     */
    public void setOutputMode(int mode){
        if(mContext != null){
            Intent intent = new Intent(KEY_OUTPUT_ACTION);
            intent.putExtra(KEY_OUTPUT_ACTION, mode);
            mContext.sendBroadcast(intent);
        }
    }

    /**8 是否播放声音*/
    public void enablePlayBeep(boolean enable){
        if(mContext != null){
            Intent intent = new Intent(KEY_BEEP_ACTION);
            intent.putExtra(KEY_BEEP_ACTION, enable);
            mContext.sendBroadcast(intent);
        }
    }

    /**扫描失败是否播放声音*/
    public void enableFailurePlayBeep(boolean enable){
        if(mContext != null){
            Intent intent = new Intent(KEY_FAILUREBEEP_ACTION);
            intent.putExtra(KEY_FAILUREBEEP_ACTION, enable);
            mContext.sendBroadcast(intent);
        }
    }


    /**9 是否震动*/
    public void enablePlayVibrate(boolean enable){
        if(mContext != null){
            Intent intent = new Intent(KEY_VIBRATE_ACTION);
            intent.putExtra(KEY_VIBRATE_ACTION, enable);
            mContext.sendBroadcast(intent);
        }
    }

    /**KEY_POWER_ACTION   省电模式
     * true 开启，该模式下屏幕锁屏后将无法扫描
     * false 关闭，该模式相反*/
    public void enablePower(boolean enable){
        if(mContext != null){
            Intent intent = new Intent(KEY_POWER_ACTION);
            intent.putExtra(KEY_POWER_ACTION, enable);
            mContext.sendBroadcast(intent);
        }
    }

    /**  附加回车、换行等
     * 0 <item>无</item>
     1 <item>附加回车键</item>
     2 <item>附加TAB键</item>
     3 <item>附加换行符</item>*/
    public void  enableAddKeyValue(int value){
        if(mContext != null){
            Intent intent = new Intent(KEY_TERMINATOR_ACTION);
            intent.putExtra(KEY_TERMINATOR_ACTION, value);
            mContext.sendBroadcast(intent);
        }
    }

    /************************************************************/

    //KEY_PREFIX_ACTION 添加前缀
    public void  addPrefix(String  text){
        if(mContext != null){
            Intent intent = new Intent(KEY_PREFIX_ACTION);
            intent.putExtra(KEY_PREFIX_ACTION, text);
            mContext.sendBroadcast(intent);
        }
    }

    //KEY_SUFFIX_ACTION添加后缀
    public void  addSuffix(String  text){
        if(mContext != null){
            Intent intent = new Intent(KEY_SUFFIX_ACTION);
            intent.putExtra(KEY_SUFFIX_ACTION, text);
            mContext.sendBroadcast(intent);
        }
    }

    //截取左字符KEY_TRIMLEFT_ACTION
    public void   interceptTrimleft  (int  num){
        if(mContext != null){
            Intent intent = new Intent(KEY_TRIMLEFT_ACTION);
            intent.putExtra(KEY_TRIMLEFT_ACTION, num);
            mContext.sendBroadcast(intent);
        }
    }

    //截取右字符KEY_TRIMRIGHT_ACTION
    public void   interceptTrimright  (int  num){
        if(mContext != null){
            Intent intent = new Intent(KEY_TRIMRIGHT_ACTION);
            intent.putExtra(KEY_TRIMRIGHT_ACTION, num);
            mContext.sendBroadcast(intent);
        }
    }

    //KEY_LIGHT_ACTION 右侧Led灯光控制
    public void   lightSet (boolean enable ){
        if(mContext != null){
            Intent intent = new Intent(KEY_LIGHT_ACTION);
            intent.putExtra(KEY_LIGHT_ACTION, enable);
            mContext.sendBroadcast(intent);
        }
    }

    //KEY_TIMEOUT_ACTION 设置超时时间
    public void   timeOutSet(int  value){
        if(mContext != null){
            Intent intent = new Intent(KEY_TIMEOUT_ACTION);
            intent.putExtra(KEY_TIMEOUT_ACTION, value);
            mContext.sendBroadcast(intent);
        }
    }

    //KEY_FILTERCHARACTER_ACTION  //过滤特定字符
    public void   filterCharacter (String text ){
        if(mContext != null){
            Intent intent = new Intent(KEY_FILTERCHARACTER_ACTION);
            intent.putExtra(KEY_FILTERCHARACTER_ACTION, text);
            mContext.sendBroadcast(intent);
        }
    }

    //KEY_CONTINUCESCAN_ACTION  是否连扫
    public void   continceScan (boolean enable ){
        if(mContext != null){
            Intent intent = new Intent(KEY_CONTINUCESCAN_ACTION);
            intent.putExtra(KEY_CONTINUCESCAN_ACTION, enable);
            mContext.sendBroadcast(intent);
        }
    }

    //KEY_INTERVALTIME_ACTION  连续扫描间隔时间
    public void  intervalSet(int  value){
        if(mContext != null){
            Intent intent = new Intent(KEY_INTERVALTIME_ACTION);
            intent.putExtra(KEY_INTERVALTIME_ACTION, value);
            mContext.sendBroadcast(intent);
        }
    }
    //KEY_FAILUREBROADCAST_ACTION 扫描失败广播
    public void   SetErrorBroadCast (boolean enable ){
        if(mContext != null){
            Intent intent = new Intent(KEY_FAILUREBROADCAST_ACTION);
            intent.putExtra(KEY_FAILUREBROADCAST_ACTION, enable);
            mContext.sendBroadcast(intent);
        }
    }

    //KEY_RESET_ACTION  恢复默认设置
    public void resultScan(){
        if(mContext != null){
            Intent intent = new Intent(KEY_RESET_ACTION);
            mContext.sendBroadcast(intent);
        }
    }

    //SCANKEY_CONFIG_ACTION   扫描按键配置
    //KEYCODE 按键名称   value有两个0，1  0表示扫描，1不做任何操作
    public void scanKeySet(int  keycode,int value){
        if(mContext != null){
            Intent intent = new Intent(SCANKEY_CONFIG_ACTION);
            intent.putExtra("KEYCODE", keycode);
            intent.putExtra("value", value);
            mContext.sendBroadcast(intent);
        }
    }
}
