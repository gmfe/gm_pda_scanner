// IScanInterface.aidl
package com.sunmi.scanner;

// Declare any non-default types here with import statements

interface IScanInterface {
 /**
     * 触发开始与停止扫码
     * key.getAction()==KeyEvent.ACTION_UP 触发开始扫码
     * key.getAction()==KeyEvent.ACTION_DWON 触发停止扫码
     */
    void sendKeyEvent(in KeyEvent key);
    /**
     * 触发开始扫码
     */
    void scan();
    /**
     * 触发停止扫码
     */
    void stop();
    /**
     * 获取扫码头类型
     * 100-->NONE
     * 101-->P2Lite
     * 102-->l2-newland
     * 103-->l2-zabra
     */
    int getScannerModel();
}
