### 添加步骤：
* [安装rnpm包](#安装rnpm包)(已安装rnpm包的请忽略本步骤)
* [安装npm包](#安装npm包)
* [添加link](#添加link)
* [添加微信sdk](#添加微信sdk)(如果你的react-native >= 0.18.0，请忽略本步骤)
* [添加微信回调类](#添加微信回调类)
* [开始使用](#开始使用)

#### 安装rnpm包
```shell
$ npm install rnpm -g
```

#### 安装npm包
```shell
$ npm install --save react-native-wechat-android
```

#### 添加link
```shell
$ rnpm link react-native-wechat-android
```

#### 添加微信sdk
把wechat/libs/libammsdk.jar复制到android/app/libs文件夹下，或者去微信开放平台的资源中心 点击[Android资源下载](https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=open1419319167)下载[Android开发工具包](https://res.wx.qq.com/open/zh_CN/htmledition/res/dev/download/sdk/Android2_SDK238f8d.zip)


#### 添加微信回调类
在你的包名相应目录下创建回调类的目录，例如应用程序的包名为com.heng,在该目录应该为com.heng.wxapi(微信指定的回调路径，不能更改,否则无法获取回调结果),并在该wxapi目录下创建[WXEntryActivity.java](#wxentryactivityjava)（微信登录和微信分享的回调类）和[WXPayEntryActivity.java](#wxpayentryactivityjava)（微信支付的回调类，如果没有微信支付功能不需要此类）,均需要继承自Activity（extends Activity），并在AndroidManifest.xml文件中添加如下代码：
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
  android:theme="@android:style/Theme.Translucent.NoTitleBar" />
 <activity
  android:name=".wxapi.WXPayEntryActivity"
  android:exported="true"
  android:theme="@android:style/Theme.Translucent.NoTitleBar" />
 ...
```

##### WXEntryActivity.java
```java
package com.xx.wxapi;    // 这里改为你的包名

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.heng.wechat.WeChatModule;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

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
        // 下面@所标记的地方key值可以根据需要自行更改,对应你js文件中的key即可
        int errCode = baseResp.errCode;
        WritableMap params = Arguments.createMap();
        params.putInt("errCode", errCode);
        String eventName = null;
        switch (errCode) {
            case BaseResp.ErrCode.ERR_OK:
                params.putBoolean("success", true);                 // @
                if (baseResp instanceof SendAuth.Resp) {
                    String code = ((SendAuth.Resp) baseResp).code;
                    String state = ((SendAuth.Resp) baseResp).state;
                    params.putString("code", code);                 // @
                    params.putString("state", state);               // @
                    eventName = "finishedAuth";                     // @
                } else if (baseResp instanceof SendMessageToWX.Resp){
                    eventName = "finishedShare";                    // @
                }
                break;
            default:
                //其他情况
                params.putBoolean("success", false);                // @
                break;
        }
        WeChatModule.reactApplicationContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
            .emit(eventName, params);
        finish();
    }
}
```

##### WXPayEntryActivity.java

```java
package com.xxx.wxapi;           // 这里改为你的包名

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.heng.wechat.WeChatModule;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;


public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

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
        // 下面@所标记的地方key值可以根据需要自行更改,对应你js文件中的key即可
        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            int errCode = baseResp.errCode;
            WritableMap params = Arguments.createMap();
            params.putInt("errCode", errCode);                    // @
            switch (errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    params.putBoolean("success", true);           // @
                    break;
                default:
                    params.putBoolean("success", false);          // @
                    break;
            }
            WeChatModule.reactApplicationContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit("finishedPay", params);                 // @
        }
        finish();
    }
}
```


#### 开始使用 

```javascript
// 比如在index.android.js中使用
'use strict';
import React, {
	AppRegistry, 
	StyleSheet, 
	View,
	Text,
	ToastAndroid,
} from 'react-native';

import WeChat from 'react-native-wechat-android';

let appId = 'wx...';   // 你的AppId 

//分享视频
let videoOptions = {
      title: 'see you again mv',
      desc: '一起来怀念下吧',
      transaction: 'video',
      scene: 0,
      type: 5,

      videoUrl: 'http://www.iqiyi.com/v_19rrnlidhk.html?src=sharemodclk131212',
      thumbImage: 'http://zx.youdao.com/zx/wp-content/uploads/2015/04/6401.jpg',
}

class MyProject extends React.Component{
  _registerApp(){
    	WeChat.registerApp(appId,(err,registerOK) => {
      		ToastAndroid.show(registerOK + '',ToastAndroid.SHORT);
    	});
  }
  
  _openApp(){
  		WeChat.openWXApp((err,res) => {

  		});
  }
  
  _share(){
  		WeChat.sendReq(videoOptions,(err,sendOK) => {
		  });
  }
  componentWillMount: function(){
    DeviceEventEmitter.addListener('finishedShare',function(event){   
      var success = event.success;                           
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
        <Text style={styles.text} onPress={this._openApp} >
          打开微信
        </Text>
        <Text style={styles.text} onPress={this._share} >
          分享视频到微信
        </Text>
      </View>
    );
  }
};

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
