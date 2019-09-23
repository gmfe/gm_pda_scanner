# gm_pad_scanner
集成了 pda 厂商的二次开发 SDK，使 app 能够对扫描头进行配置、开关和获取扫描结果等操作

### 已适配厂商

- [商米科技](https://docs.sunmi.com/)
- [智联天地](http://www.zhiliantiandi.com/)
- [江苏东大集成电路系统工程技术有限公司](https://www.seuic.com/)
- [优博讯](http://www.urovo.com/)
- [无锡盈达聚力科技 idata 95w](http://www.idatachina.com/)
- [深圳市思感科技有限公司](http://www.hualuoyun.com/)
- [深圳市捷宝科技有限公司](http://5002237.shop.52bjw.cn/)

### Demo

Android Studio 直接运行 app module 即可

### 安装

1. 在项目的 `build.gradle` 中添加：

   ```groovy
   ...
   allprojects {
       repositories {
           ...
           maven { url 'https://jitpack.io' }
       }
   }
   ...
   ```

2. 在 module 的 `build.gradle` 中添加：

   ```groovy
   ...
   dependencies {
   	...
       implementation 'com.github.gmfe:gm_pda_scanner:{versionName}'
   }
   ...
   ```

### 使用

```java
SupporterManager sm = new SupporterManager(this, new SupporterManager.IScanListener() {
    @Override
    public void onScannerResultChange(String result) {
        // scan result callback
    }

    @Override
    public void onScannerServiceConnected() {
        // init succeed
    }

    @Override
    public void onScannerServiceDisconnected() {
        // stop listener
    }

    @Override
    public void onScannerInitFail() {
        // init fail
    }
});
```

