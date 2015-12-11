# react-native-wechat-android
react-native 的微信SDK辅助包，目前仅支持微信登录。

The WeChat sdk help library for react-native , currently only support wechat login .

## 安装及使用方法 / Installation and How to use
#### 第一步 : 安装npm包 / Step 1 - NPM Install

```shell
npm install --save react-native-wechat-android
```
#### 第二步 : 更新settings.gradle / Step 2 - Update Gradle Settings

```gradle
// 文件路径：android/settings.gradle 
// file: android/settings.gradle

...

include ':reactwechat', ':app' 
project(':reactwechat').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-wechat-android')

// 如果有其他的library，这样添加：
// if there are more library , add this:
 
// include ':app' , ':libraryone' , ':librarytwo' , 'more...'
// project(':libraryonename').projectDir = new File(rootProject.projectDir, '../node_modules/libraryonemodule')
// project(':librarytwoname').projectDir = new File(rootProject.projectDir, '../node_modules/librarytwomodule')
// more..
```

#### 第三步 : 更新app的build.gradle / Step 3 - Update app Gradle Build

```gradle
// 文件路径：android/app/build.gradle
// file: android/app/build.gradle
...

dependencies {
    ...
    compile project(':reactwechat')
}
```

#### 第四步 : 注册包 / Step 4 - Register React Package

```java
...
import com.heng.wechat.WeChatPackage;

public class MainActivity extends Activity implements DefaultHardwareBackBtnHandler {

    private ReactInstanceManager mReactInstanceManager;
    private ReactRootView mReactRootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mReactRootView = new ReactRootView(this);
        mReactInstanceManager = ReactInstanceManager.builder()
                .setApplication(getApplication())
                .setBundleAssetName("index.android.bundle")
                .setJSMainModuleName("index.android")
                .addPackage(new MainReactPackage())
                .addPackage(new WeChatPackage()) // 注册WeChatPackage / register wechat package
                .setUseDeveloperSupport(BuildConfig.DEBUG)
                .setInitialLifecycleState(LifecycleState.RESUMED)
                .build();
        ...
    }
...

```

#### 第五步 : 添加微信SDK / Step 5 Add WeChat SDK
把wechat/libs/libammsdk.jar复制到android/app/libs文件夹下，或者去微信开放平台的资源中心 点击[Android资源下载](https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=open1419319167)下载[Android开发工具包](https://res.wx.qq.com/open/zh_CN/htmledition/res/dev/download/sdk/Android2_SDK238f8d.zip)

copy wechat/libs/libammsdk.jar to android/app/libs , or go WeChat open resource center click [Android资源下载](https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=open1419319167) to download [Android开发工具包](https://res.wx.qq.com/open/zh_CN/htmledition/res/dev/download/sdk/Android2_SDK238f8d.zip)

#### 第六步 : 添加WXEntryActivity并在AndroidManifest.xml中注册 / Step 6 - Add WXEntryActivity and register in AndroidManifest.xml
在你的包名相应目录下新建一个wxapi目录，并在该wxapi目录下创建一个WXEntryActivity类，该类继承自Activity（例如应用程序的包名为com.heng，则WXEntryActivity应该位于com.heng.wxapi包下）,并在AndroidManifest.xml文件中添加如下代码：

Create a wxapi directory in your package name, and create a WXEntryActivity class in the wxapi directory, which is inherited from the Activity (for example, the package name is com.heng, then the WXEntryActivity should be located in the com.heng.wxapi), and add the following code in the AndroidManifest.xml file

```xml
  <activity
    android:name=".wxapi.WXEntryActivity"
    android:exported="true"
    android:theme="@android:style/Theme.Translucent.NoTitleBar" 
  />
```

#### 第七步 : 为添加的WXEntryActivity实现IWXAPIEventHandler接口及相应的方法 / Step 7 -  To add the WXEntryActivity to achieve the IWXAPIEventHandler interface and the corresponding method

```java
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        WeChatModule.wxApi.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        WeChatModule.wxApi.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        int errCode = baseResp.errCode;
        String code;
        String state;
        switch (errCode) {
            case BaseResp.ErrCode.ERR_OK:
                //用户同意 / user agree
                code = ((SendAuth.Resp) baseResp).code;
                state = ((SendAuth.Resp) baseResp).state;
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                //用户拒绝授权 / user refuse authorize
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                //用户取消 / user cancel
            default:
                code = "";
                state = "";
                break;
        }
        WritableMap params = Arguments.createMap();
        WritableMap map = Arguments.createMap();
        map.putInt("errCode", errCode); @
        map.putString("code", code);    @
        map.putString("state", state);  @
        params.putMap("response", map); @
        WeChatModule.reactApplicationContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit("finishedAuth", params); @
        finish();
      
        // @所标记的key可以自由更改,对应你的js文件的key即可 / @ the key tag can be changed freely, and you can do the key of your JS file.
}
```


#### 第八步 : 在你的JS文件中使用  /  Step 8 - Require and use in Javascript

```js
// 比如在index.android.js中使用
// example : index.android.js

'use strict';

var React = require('react-native');
var {
  AppRegistry,
  StyleSheet,
  ToastAndroid,
  DeviceEventEmitter,
  Text,
  View,
} = React;

var WeChatAndroid = require('react-native-wechat-android');

var appId = 'wx...';   // 你的AppId / you AppId
var state = '12344214';  //用于保持请求和回调的状态，授权请求后原样带回给第三方。该参数可用于防止csrf攻击（跨站请求伪造攻击），建议第三方带上该参数，可设置为简单的随机数加session进行校验 / Used to maintain the status of the request and the callback, the authorization request is returned to the third party. This parameter can be used to prevent CSRF attacks (CSRF attack), proposed third party with the parameter can be set to a simple random number with session check

var MyProject = React.createClass({
  _registerApp(){
    WeChatAndroid.registerApp(appId,(registerOK) => {
      ToastAndroid.show(registerOK + '',ToastAndroid.SHORT);
    });
  },
  _isWXAppInstalled(){
    WeChatAndroid.isWXAppInstalled(
      (isInstalled) => {
        if(isInstalled){
          ToastAndroid.show('已安装 / installed',ToastAndroid.SHORT);
        }else{
          ToastAndroid.show('未安装 / not installed',ToastAndroid.SHORT);
        }
      }
    );
  },
  _sendAuthRequest(){
    WeChatAndroid.sendAuthRequest(state);
  },
  componentWillMount: function(){
    DeviceEventEmitter.addListener('finishedAuth',function(event){
      var errCode = event.response.errCode;
      switch(errCode){
        case 0:
          // 授权成功 / authorize success
          ToastAndroid.show(
            ' code = ' + JSON.stringify(event.response.code) + 
            ' state = ' + JSON.stringify(event.response.state),
            ToastAndroid.LONG
          );
          break;  
        case -2:
          // 用户取消授权 / user cancel authorize
          ToastAndroid.show('授权已取消 / authorize canceled ',ToastAndroid.SHORT);
          break;
        case -4:
          // 用户拒绝授权 / user refuse authorize
          ToastAndroid.show('授权被拒绝 / authorize to be rejected',ToastAndroid.SHORT);
          break;
        default:
          ToastAndroid.show('未知错误 / unknown error ',ToastAndroid.SHORT);
          break;
      }
    });
  },
  render: function() {
    return (
      <View style={styles.container}>
        <Text style={styles.text} onPress={this._registerApp} >
          注册到微信
        </Text>
        <Text style={styles.text} onPress={this._isWXAppInstalled} >
          是否安装微信
        </Text>
        <Text style={styles.text} onPress={this._sendAuthRequest} >
          微信登录
        </Text>
      </View>
    );
  }
});

var styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  text: {
    fontSize: 20,
    textAlign: 'center',
    color: '#333333',
    margin: 10,
  },
});

AppRegistry.registerComponent('MyProject', () => MyProject);

```

## Run Renderings
<center>
    <img src="https://github.com/beefe/react-native-wechat-android/blob/master/ScreenShot/screenshotone.jpeg"
    width="300" height="450"/>
    <img src="https://github.com/beefe/react-native-wechat-android/blob/master/ScreenShot/screenshottwo.jpeg"
    width="300" height="450"/>
</center>

## Notes
打包apk请参考[Generating Signed APK](http://facebook.github.io/react-native/docs/signed-apk-android.html#content)
