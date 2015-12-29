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
 ...
  <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/> 
  <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/> 
  <uses-permission android:name="android.permission.READ_PHONE_STATE"/> 
  <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
 ...

 ...
  <activity
    android:name=".wxapi.WXEntryActivity"
     android:exported="true"
     android:theme="@android:style/Theme.Translucent.NoTitleBar" 
   />
 ...
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
        WritableMap params = Arguments.createMap();
        WritableMap map = Arguments.createMap();
        if (WeChatModule.currentAction.equals(WeChatModule.ACTION_LOGIN)) {
            map.putInt("errCode", errCode);
            switch (errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    //用户同意
                    String code = ((SendAuth.Resp) baseResp).code;
                    String state = ((SendAuth.Resp) baseResp).state;
     @              map.putString("code", code);                        
     @              map.putString("state", state);
     @              map.putBoolean("success", true);
                    break;
                case BaseResp.ErrCode.ERR_AUTH_DENIED:
                    //用户拒绝授权
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    //用户取消
                default:
                    //其他情况
     @              map.putBoolean("success", false);
                    break;
            }
     @       params.putMap("response", map);
            WeChatModule.reactApplicationContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
     @              .emit("finishedAuth", params);
        }else{
            switch (errCode) {
                case BaseResp.ErrCode.ERR_OK:
     @              map.putBoolean("success", true);
                    break;
                case BaseResp.ErrCode.ERR_COMM://一般错误
                case BaseResp.ErrCode.ERR_USER_CANCEL://用户取消
                case BaseResp.ErrCode.ERR_SENT_FAILED://发送失败
                case BaseResp.ErrCode.ERR_AUTH_DENIED://认证被否决
                case BaseResp.ErrCode.ERR_UNSUPPORT://不支持错误
                default:
     @              map.putBoolean("success", false);
                    break;
            }
     @      params.putMap("response", map);
            WeChatModule.reactApplicationContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
     @              .emit("finishedShare", params); @
        }
        finish();
    }
    // @所标记的key可以根据需要自行更改,对应你js文件中的key即可 / @ the key tag can be changed freely, and you can do the key of your JS file.
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

var shareWebPageOptions = {
  link: 'https://github.com/beefe/react-native-wechat-android', //分享的网页的链接
  tagName: 'test tagName',
  thumbSize: 150,       //分享网页的缩略图大小
  title: 'this is share title',
  desc: 'this is my share desc',
  thumbImage: 'http://img1.imgtn.bdimg.com/it/u=3924416677,403957246&fm=21&gp=0.jpg',  //分享的网页缩略图的url
};

var shareLocalImageOptions = {
  imageSourceType: 0,
  thumbSize: 150,               //分享本地图片的缩略图大小
  localPath: '/mnt/sdcard/temp.png',   //分享的本地图片的完整路径
};

var shareRemoteImageOptions = {
  imageSourceType: 1,
  thumbSize: 150,               //分享网络图片的缩略图大小
  scene: 0,
  remoteUrl: 'https://avatars3.githubusercontent.com/u/3015681?v=3&s=460',     //分享的网络图片的url
};

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
          ToastAndroid.show('已安装',ToastAndroid.SHORT);
        }else{
          ToastAndroid.show('未安装',ToastAndroid.SHORT);
        }
      },
      (err) => {
        ToastAndroid.show(err,ToastAndroid.SHORT);
      },
    );
  },
  _isWXAppSupportAPI(){
    WeChatAndroid.isWXAppSupportAPI(
      (isSupport) => {
        ToastAndroid.show(isSupport + '',ToastAndroid.SHORT);
      },
      (err) => {
        ToastAndroid.show(err,ToastAndroid.SHORT);
      }
    );
  },
  _sendAuthRequest(){
    WeChatAndroid.sendAuthReq(null,(err) => {
       ToastAndroid.show(err,ToastAndroid.SHORT);
    });
  },
  _sendWebPageToSession(){
    shareWebPageOptions.scene = 0;
    WeChatAndroid.sendLinkURL(shareWebPageOptions,(err) => {
      ToastAndroid.show(err,ToastAndroid.SHORT);
    });
  },
  _sendWebPageToTimeline(){
    shareWebPageOptions.scene = 1;
    WeChatAndroid.sendLinkURL(shareWebPageOptions,(err) => {
      ToastAndroid.show(err,ToastAndroid.SHORT);
    });
  },
  _sendWebPageToFavorite(){
    shareWebPageOptions.scene = 2;
    WeChatAndroid.sendLinkURL(shareWebPageOptions,(err) => {
      ToastAndroid.show(err,ToastAndroid.SHORT);
    });
  },
  _sendLocalImageToSession(){
    shareLocalImageOptions.scene = 0;
    WeChatAndroid.sendImage(shareLocalImageOptions,(err) => {
      ToastAndroid.show(err,ToastAndroid.SHORT);
    });
  },
  _sendLocalImageToTimeline(){
    shareLocalImageOptions.scene = 1;
    WeChatAndroid.sendImage(shareLocalImageOptions,(err) => {
      ToastAndroid.show(err,ToastAndroid.SHORT);
    });
  },
  _sendLocalImageToFavorite(){
    shareLocalImageOptions.scene = 2;
    WeChatAndroid.sendImage(shareLocalImageOptions,(err) => {
      ToastAndroid.show(err,ToastAndroid.SHORT);
    });
  },
  _sendRemoteImageToSession(){
    WeChatAndroid.sendImage(shareRemoteImageOptions,(err) => {
      ToastAndroid.show(err,ToastAndroid.SHORT);
    });
  },
  componentWillMount: function(){
    DeviceEventEmitter.addListener('finishedAuth',function(event){
      var success = event.response.success;
      if(success){
         ToastAndroid.show(
            ' code = ' + JSON.stringify(event.response.code) + 
            ' state = ' + JSON.stringify(event.response.state),
            ToastAndroid.LONG
          );
      }else{
        ToastAndroid.show('授权失败',ToastAndroid.SHORT);
      }
    });
    DeviceEventEmitter.addListener('finishedShare',function(event){
      var success = event.response.success;
      if(success){
        ToastAndroid.show('分享成功',ToastAndroid.SHORT);
      }else{
        ToastAndroid.show('分享失败',ToastAndroid.SHORT);
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
        <Text style={styles.text} onPress={this._isWXAppSupportAPI} >
          是否微信支持的API
        </Text>
        <Text style={styles.text} onPress={this._sendAuthRequest} >
          微信登录
        </Text>
        <Text style={styles.text} onPress={this._sendWebPageToSession} >
          分享网页给朋友
        </Text>
        <Text style={styles.text} onPress={this._sendWebPageToTimeline} >
          分享网页到朋友圈
        </Text>
        <Text style={styles.text} onPress={this._sendWebPageToFavorite} >
          分享网页到收藏
        </Text>
        <Text style={styles.text} onPress={this._sendLocalImageToSession} >
          分享本地图片给朋友
        </Text>
        <Text style={styles.text} onPress={this._sendLocalImageToTimeline} >
          分享本地图片到朋友圈
        </Text>
        <Text style={styles.text} onPress={this._sendLocalImageToFavorite} >
          分享本地图片到收藏
        </Text>
        <Text style={styles.text} onPress={this._sendRemoteImageToSession} >
          分享网络图片给朋友
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
