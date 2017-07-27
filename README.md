# react-native-wechat-android [![npm version](https://img.shields.io/npm/v/react-native-wechat-android.svg?style=flat-square)](https://www.npmjs.com/package/react-native-wechat-android) ![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg)
react-native 的微信SDK辅助包，支持微信登录、微信分享、微信支付(本模块仅支持Android)。

- [iOS模块](https://github.com/beefe/react-native-wechat-ios)地址
- [兼容iOS](https://github.com/beefe/react-native-wechat-android/wiki/%E5%85%BC%E5%AE%B9iOS)示例

## [安装及使用方法](https://github.com/beefe/react-native-wechat-android/wiki)

## 提供以下方法 / Method

* [registerApp](#registerappappidcallback)
* [openWXApp](#openwxappcallback)
* [isWXAppInstalled](#iswxappinstalledcallback)
* [isWXAppSupportAPI](#iswxappsupportapicallback)
* [getWXAppSupportAPI](#iswxappsupportapicallback)
* [sendAuthReq](#sendauthreqscopestatecallback)
* [sendReq](#sendreqoptionscallback)
* [weChatPay](#wechatpayoptionscallback)

### registerApp(appId,callback)

appId : 在微信开放平台申请的AppID

callback : 回调(err,res)

使用示例：

```javascript
WeChatAndroid.registerApp(appId,(err,registerOK) => {
 ...
});
```

### openWXApp(callback)

callback : 回调(err,res)

使用示例：

```javascript
WeChatAndroid.openWXApp((err,res) => {
    ...
});
```

### isWXAppInstalled(callback)

callback : 回调(err,res)

使用示例：

```javascript
WeChatAndroid.isWXAppInstalled(
    (err,isInstalled) => {
        ...
    }
);
```

### isWXAppSupportAPI(callback)

callback : 回调(err,res)

使用示例：

```javascript
WeChatAndroid.isWXAppSupportAPI(
    (err,isSupport) => {
        ...
    }
);
```

### getWXAppSupportAPI(callback)

callback : 回调(err,res)

使用示例：

```javascript
WeChatAndroid.getWXAppSupportAPI(
    (err,supportAPI) => {
        ...
    }
);
```

### sendAuthReq(scope,state,callback)

scope : 微信登录需要的参数(可空)

state : 微信登录需要的参数(可空)

callback : 回调(err,res)

使用示例：

```javascript
WeChatAndroid.sendAuthReq('snsapi_userinfo','SECRET',(err,authReqOK) => {
    ...
});
```
or
```javascript
WeChatAndroid.sendAuthReq(null,null,(err,authReqOK) => {
    ...
});
```
```javascript
// 处理登录回调结果
DeviceEventEmitter.addListener('finishedAuth',function(event){
    if(event.success){
        ToastAndroid.show(
            ' code = ' + JSON.stringify(event.code)
            + ' state = ' + JSON.stringify(event.state),
            ToastAndroid.LONG
        );
    }else{
        ToastAndroid.show('授权失败',ToastAndroid.SHORT);
    }
});
```

### sendReq(options,callback)

options : 分享到微信需要的参数
 * type （必传，用来区分分享的内容）
  * 1:文字
  * 2:图片
  * 3:网页
  * 4:音乐
  * 5:视频
 * title （分享的标题）
 * desc （分享的描述）
 * thumbSize （分享的缩略图大小，不传默认150）
 * tagName
 * transaction
 * scene （分享方式，传错或者不传默认为0）
  * 0:聊天界面／好友
  * 1:朋友圈
  * 2:收藏
 * text （当分享类型为文本时使用）
 * imageUrl （当分享类型为网络照片时使用）
 * imagePath （当分享类型为本地照片时使用）
 * webpageUrl （当分享类型为网页时使用）
 * musicUrl （当分享类型为音乐时使用）
 * musicLowBandUrl （当分享类型为音乐时使用）
 * thumbImage （仅当分享类型为网页、音乐、视频时使用）

callback : 回调(err,res)

使用示例：

```javascript

//分享文本
var textOptions = {
    title: '分享一段内容给你',
    transaction: 'text',
    scene: 0,
    type: 1,
    text: '这里是分享的文本内容',
}

//分享网络图片
var networkImageOptions = {
    title: '分享一张图片给你',
    thumbSize: 150,
    scene: 0,
    type: 2,

    imageUrl: 'https://avatars3.githubusercontent.com/u/3015681?v=3&s=460',
}

//分享本地图片
var localImageOptions = {
    title: '分享一张图片给你',
    thumbSize: 150,
    scene: 0,
    type: 2,

    imagePath: '/mnt/sdcard/temp.png',
}

//分享网页
var webpageOptions = {
    title: '分享这个网页给你',
    desc: '我发现这个网页很有趣，特意分享给你',
    thumbSize: 150,
    scene: 0,
    type: 3,

    webpageUrl: 'https://github.com/beefe/react-native-wechat-android',
    thumbImage: 'http://img1.imgtn.bdimg.com/it/u=3924416677,403957246&fm=21&gp=0.jpg',
}

//分享音乐
var musicOptions = {
    title: '这里是分享的标题',
    desc: '发现一首好听的音乐，分享给你',
    transaction: 'music',
    scene: 1,
    type: 4,

    musicUrl: 'http://staff2.ustc.edu.cn/~wdw/softdown/index.asp/0042515_05.ANDY.mp3',
    thumbImage: 'http://img1.imgtn.bdimg.com/it/u=3924416677,403957246&fm=21&gp=0.jpg',
}

//分享视频
var videoOptions = {
    title: '这里是分享的标题',
    desc: '这个视频好有趣，一起来看看',
    transaction: 'video',
    scene: 1,
    type: 5,

    videoUrl: 'http://www.iqiyi.com/v_19rrnlidhk.html?src=sharemodclk131212',
    thumbImage: 'http://img1.imgtn.bdimg.com/it/u=3924416677,403957246&fm=21&gp=0.jpg',
}

WeChatAndroid.sendReq(videoOptions,(err,sendOK) => {
    ...
});

// 分享回调
DeviceEventEmitter.addListener('finishedShare',function(event){
    if(event.success){
        ToastAndroid.show('分享成功',ToastAndroid.SHORT);
    }else{
        ToastAndroid.show('分享失败',ToastAndroid.SHORT);
    }
});
```

#### WechatAPI.shareToTimeline(data)
分享到朋友圈

```javascript
// 分享文字
{	
	type: 'text', 
	text: 文字内容,
}
```

```javascript
// 分享图片
{	
	type: 'image',
	imageUrl: 图片地址,
	title : 标题,
	description : 描述,
}
```

```javascript
// 分享网页
{	
	type: 'news',
	title : 标题,
	description : 描述,	
	webpageUrl : 链接地址,
	imageUrl: 缩略图地址,
}
```

### weChatPay(options,callback)

options : [微信支付需要的参数](https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=9_12&index=2)

callback : 回调(err,res)

使用示例：

```javascript

var payOptions = {
    appId: 'wx8888888888888888',         
    nonceStr: '5K8264ILTKCH16CQ2502SI8ZNMTM67VS',            
    packageValue: 'Sign=WXPay',
    partnerId: '1900000109',
    prepayId: 'WX1217752501201407033233368018',
    timeStamp: '1412000000',
    sign: 'C380BEC2BFD727A4B6845133519F3AD6',
};

WeChatAndroid.weChatPay(payOptions,(err,sendReqOK) => {
    ...
});

//  处理支付回调结果
DeviceEventEmitter.addListener('finishedPay',function(event){
    if(event.success){
        // 在此发起网络请求由服务器验证是否真正支付成功，然后做出相应的处理
    }else{
        ToastAndroid.show('支付失败',ToastAndroid.SHORT);
    }
});
```

## Notes
* 打包apk请参考[Generating Signed APK](http://facebook.github.io/react-native/docs/signed-apk-android.html#content)
* 如需要混淆，需要在混淆文件里加上以下代码：
```text
-keep class com.tencent.mm.sdk.** {
   *;
}
```
