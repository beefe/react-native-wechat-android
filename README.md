# react-native-wechat-android
react-native 的微信SDK辅助包，支持微信登录、微信分享、微信支付。


## 提供以下方法 / Method

* [registerApp(appId,callback)](https://github.com/beefe/react-native-wechat-android#registerApp)
* isWXAppInstalled(callback,errCallback) 
* isWXAppSupportAPI(callback,errCallback)
* sendAuthReq(options,errCallback)
* sendLinkURL(options,errCallback)
* sendImage(options,errCallback)
* weChatPay(options,errCallback)


### registerApp
appId : 在微信开放平台申请的AppID

callback : 返回注册结果(true/false)

使用示例：
```javascript
WeChatAndroid.registerApp(appId,(registerOK) => {
 ToastAndroid.show(registerOK + '',ToastAndroid.SHORT);
});
```

* isWXAppInstalled(callback,errCallback)
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

* isWXAppSupportAPI(callback,errCallback)
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

* sendAuthReq(options,errCallback)
```javascript
WeChatAndroid.sendAuthReq(null,(err) => {
 ToastAndroid.show(err,ToastAndroid.SHORT);
});
```

* sendLinkURL(options,errCallback)
```javascript
WeChatAndroid.sendLinkURL(shareOptions,(err) => {
 ToastAndroid.show(err,ToastAndroid.SHORT);
});
```

* weChatPay(options,errCallback)
```javascript
WeChatAndroid.weChatPay(shareOptions,(err) => {
 ToastAndroid.show(err,ToastAndroid.SHORT);
});
```
[options](https://github.com/beefe/react-native-wechat-android/blob/master/src/main/java/com/heng/wechat/WeChatModule.java#L53-L77)

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
