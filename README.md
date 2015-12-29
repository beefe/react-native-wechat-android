# react-native-wechat-android
react-native 的微信SDK辅助包，目前提供调用微信支付的方法[weChatPay](https://github.com/beefe/react-native-wechat-android/blob/master/src/main/java/com/heng/wechat/WeChatModule.java#L341-L395)，返回处理可以参考[微信开放平台微信支付功能](https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=open1419317784&token=ac2c2797aab719b69c622e37880298e1fc7638b0&lang=zh_CN)自行完善，需要在wxapi包中添加WXPayEntryActivity.java类来处理。

The WeChat sdk help library for react-native , currently provides call wechat payment method [weChatPay](https://github.com/beefe/react-native-wechat-android/blob/master/src/main/java/com/heng/wechat/WeChatModule.java#L341-L395), returns to the process can refer to [WeChat open api](https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=open1419317784&token=ac2c2797aab719b69c622e37880298e1fc7638b0&lang = zh_CN) self- improvement , need to add in wxapi package WXPayEntryActivity.java class to handle .

## 提供以下方法 / Method

* registerApp(appId,callback)
 
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
