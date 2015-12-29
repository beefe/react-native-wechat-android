# react-native-wechat-android
react-native 的微信SDK辅助包，支持微信登录、微信分享、微信支付。


## 提供以下方法 / Method

* [registerApp](https://github.com/beefe/react-native-wechat-android#registerappappidcallback)
* [isWXAppInstalled](https://github.com/beefe/react-native-wechat-android#iswxappinstalledcallbackerrcallback)
* [isWXAppSupportAPI](https://github.com/beefe/react-native-wechat-android#iswxappsupportapicallbackerrcallback)
* [sendAuthReq](https://github.com/beefe/react-native-wechat-android#sendauthreqoptionserrcallback)
* [sendLinkURL](https://github.com/beefe/react-native-wechat-android#sendlinkurloptionserrcallback)
* [sendImage](https://github.com/beefe/react-native-wechat-android#sendimageoptionserrcallback)
* [weChatPay](https://github.com/beefe/react-native-wechat-android#wechatpayoptionserrcallback)


### registerApp(appId,callback)

appId : 在微信开放平台申请的AppID

callback : 返回注册结果(true/false)

使用示例：

```javascript
WeChatAndroid.registerApp(appId,(registerOK) => {
 ToastAndroid.show(registerOK + '',ToastAndroid.SHORT);
});
```

### isWXAppInstalled(callback,errCallback)

callback : 返回是否安装微信(true/false)

errCallback : 返回错误信息

使用示例：

```javascript
WeChatAndroid.isWXAppInstalled(
 (isInstalled) => {
    ToastAndroid.show(isInstalled + '',ToastAndroid.SHORT);
 },
 (err) => {
  ToastAndroid.show(err,ToastAndroid.SHORT);
 }
);
```

### isWXAppSupportAPI(callback,errCallback)

callback : 返回安装的微信是否为支持的版本(true/false)

errCallback : 返回错误信息

使用示例：

```javascript
WeChatAndroid.isWXAppSupportAPI(
 (isSupport) => {
  ToastAndroid.show(isSupport + '',ToastAndroid.SHORT);
 },
 (err) => {
  ToastAndroid.show(err,ToastAndroid.SHORT);
 }
);
```

### sendAuthReq(options,errCallback)

options : 微信登录需要的参数

errCallback : 返回错误信息

使用示例：

```javascript
WeChatAndroid.sendAuthReq(null,(err) => {
 ToastAndroid.show(err,ToastAndroid.SHORT);
});
```

### sendLinkURL(options,errCallback)

options : 微信分享网页需要的参数

errCallback : 返回错误信息

使用示例：

```javascript
WeChatAndroid.sendLinkURL(shareWebPageOptions,(err) => {
 ToastAndroid.show(err,ToastAndroid.SHORT);
});
```

### sendImage(options,errCallback)

options : 微信分享图片需要的参数

errCallback : 返回错误信息

使用示例：

```javascript
WeChatAndroid.sendImage(shareImageOptions,(err) => {
 ToastAndroid.show(err,ToastAndroid.SHORT);
});
```

### weChatPay(options,errCallback)

options : 微信支付需要的参数

errCallback : 返回错误信息

使用示例：

```javascript
WeChatAndroid.weChatPay(shareOptions,(err) => {
 ToastAndroid.show(err,ToastAndroid.SHORT);
});
```

## [安装及使用方法 / Installation and How to use](https://github.com/beefe/react-native-wechat-android/blob/master/HELP.md)

## Run Renderings
<center>
    <img src="https://github.com/beefe/react-native-wechat-android/blob/master/ScreenShot/wait_auth.jpeg"
    width="300" height="520"/>
    <img src="https://github.com/beefe/react-native-wechat-android/blob/master/ScreenShot/local_to_favorite.jpeg"
    width="300" height="520"/>
    <img src="https://github.com/beefe/react-native-wechat-android/blob/master/ScreenShot/local_to_timeline.jpeg"
    width="300" height="520"/>
    <img src="https://github.com/beefe/react-native-wechat-android/blob/master/ScreenShot/webpage_to_favorite.jpeg"
    width="300" height="520"/>
    <img src="https://github.com/beefe/react-native-wechat-android/blob/master/ScreenShot/webpage_to_timeline.jpeg"
    width="300" height="520"/>
    <img src="https://github.com/beefe/react-native-wechat-android/blob/master/ScreenShot/local_to_favorite_screenshot.jpeg"
    width="300" height="520"/>
</center>

## [Demo download](https://github.com/beefe/react-native-wechat-android/blob/master/apk/demo.apk?raw=true)

## Notes
打包apk请参考[Generating Signed APK](http://facebook.github.io/react-native/docs/signed-apk-android.html#content)
