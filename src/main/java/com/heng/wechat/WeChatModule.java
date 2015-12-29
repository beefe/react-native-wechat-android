package com.heng.wechat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created by heng on 2015/12/10.
 *
 * Edited by heng on 15/12/18
 * Added share webPage and weChat Pay
 * 
 * Edited by heng on 2015/12/22
 * Add remote image async download
 *
 * Edited by heng on 2015/12/29
 * 1.Removed Handler and Thread
 * 2.Modify options param
 * 3.Added share local
 * 4.Added and remote image(分享远程图片到朋友圈和收藏都会失败,具体原因待查,建议把远程图片下载到本地来分享)
 */
public class WeChatModule extends ReactContextBaseJavaModule {

    public static final String REACT_MODULE_NAME = "WeChatAndroid";

    public static IWXAPI wxApi = null;
    public static String appId;
    public static ReactApplicationContext reactApplicationContext;

    public static final String ACTION_LOGIN = "login";
    public static final String ACTION_SHARE = "share";
    public static final String ACTION_DEFAULT = "default";
    public static String currentAction = ACTION_DEFAULT;


    /*============ WeChat login options key ==============*/
    public static final String OPTIONS_SCOPE = "scope";
    public static final String OPTIONS_STATE = "state";
    /*============ WeChat login options key ==============*/

    /*============ WeChat share options key ==============*/
    public static final String OPTIONS_LINK = "link";
    public static final String OPTIONS_TAG_NAME = "tagName";
    public static final String OPTIONS_TITLE = "title";
    public static final String OPTIONS_DESC = "desc";
    public static final String OPTIONS_THUMB_IMAGE = "thumbImage";
    public static final String OPTIONS_THUMB_SIZE = "thumbSize";
    public static final String OPTIONS_SCENE = "scene";
    public static final String OPTIONS_LOCAL_PATH = "localPath";
    public static final String OPTIONS_REMOTE_URL = "remoteUrl";
    public static final String OPTIONS_IMAGE_SOURCE_TYPE = "imageSourceType";
    /*============ WeChat share options key ==============*/

    /*============ WeChat pay options key ==============*/
    public static final String OPTIONS_APP_ID = "appId";
    public static final String OPTIONS_NONCE_STR = "nonceStr";
    public static final String OPTIONS_PACKAGE_VALUE = "packageValue";
    public static final String OPTIONS_PARTNER_ID = "partnerId";
    public static final String OPTIONS_PREPAY_ID = "prepayId";
    public static final String OPTIONS_TIME_STAMP = "timeStamp";
    public static final String OPTIONS_SIGN = "sign";
    /*============ WeChat pay options key ==============*/


    public static final int SCENE_SESSION = 0;          //分享到聊天界面
    public static final int SCENE_TIMELINE = 1;         //分享到朋友圈
    public static final int SCENE_FAVORITE = 2;         //分享到收藏

    String link = null;          //分享的网页链接
    String tagName = null;       //分享的标签名
    String title = null;         //分享的网页标题
    String desc = null;          //分享的网页描述
    String transaction = null;   //分享的描述
    String thumbImage = null;    //分享的图片的网络地址
    int thumbSize = 150;         //分享的缩略图大小
    int scene = 0;               //分享的方式(0:聊天界面，1:朋友圈，2:收藏)
    int imageSourceType = 0;     //分享的图片来源(0:本地，1:网络)
    String localPath = null;     //分享的图片的本地路径
    String remoteUrl = null;     //分享的图片的本地路径

    public WeChatModule(ReactApplicationContext reactContext) {
        super(reactContext);
        WeChatModule.reactApplicationContext = reactContext;
    }

    @Override
    public String getName() {
        return REACT_MODULE_NAME;
    }

    @ReactMethod
    public void registerApp(String appId, Callback callback) {
        WeChatModule.appId = appId;
        WeChatModule.wxApi = WXAPIFactory.createWXAPI(getReactApplicationContext(), appId, true);
        callback.invoke(WeChatModule.wxApi.registerApp(appId));
    }

    /**
     * Edited by heng on 2015/12/18
     * <p/>
     * add errCallback;
     */
    @ReactMethod
    public void isWXAppInstalled(Callback callback, Callback errCallback) {
        if (WeChatModule.wxApi == null) {
            if (errCallback != null) {
                errCallback.invoke("please registerApp before this !");
            }
            return;
        }
        if (callback != null) {
            callback.invoke(WeChatModule.wxApi.isWXAppInstalled());
        }
    }

    /**
     * Edited by heng on 2015/12/18
     * <p/>
     * update params and add errCallback;
     * <p/>
     * save current action
     */
    @ReactMethod
    public void sendAuthReq(ReadableMap options, Callback errCallback) {
        if (WeChatModule.wxApi == null) {
            if (errCallback != null) {
                errCallback.invoke("please registerApp before this !");
            }
            return;
        }
        String scope = "snsapi_userinfo";
        String state = "SECRET";
        if (options != null) {
            if (options.hasKey(OPTIONS_SCOPE)) {
                scope = options.getString(OPTIONS_SCOPE);
            }
            if (options.hasKey(OPTIONS_STATE)) {
                state = options.getString(OPTIONS_STATE);
            }
        }
        SendAuth.Req req = new SendAuth.Req();
        req.scope = scope;
        req.state = state;
        WeChatModule.currentAction = ACTION_LOGIN;
        WeChatModule.wxApi.sendReq(req);
    }


    /**
     * Added by heng on 2015/12/18
     * <p/>
     * callback return true ? is support : not support
     * if not register WeChat appId , errCallback return error
     */
    @ReactMethod
    public void isWXAppSupportAPI(Callback callback, Callback errCallback) {
        if (WeChatModule.wxApi == null) {
            if (errCallback != null) {
                errCallback.invoke("please registerApp before this !");
            }
            return;
        }
        if (callback != null) {
            callback.invoke(WeChatModule.wxApi.isWXAppSupportAPI());
        }
    }

    /**
     * Added by heng on 2015/12/18
     * Edited by heng one 2015/12/29
     *
     * this method is used to share webPage to WeChat
     *
     * errCallback return error
     */
    @ReactMethod
    public void sendLinkURL(ReadableMap options, Callback errCallback) {
        if (WeChatModule.wxApi == null) {
            if (errCallback != null) {
                errCallback.invoke("please registerApp before this !");
            }
            return;
        }

        if (options != null) {
            if (options.hasKey(OPTIONS_LINK)) {
                link = options.getString(OPTIONS_LINK);
            }else{
                errCallback.invoke("please setting share link !");
                return;
            }
            if (options.hasKey(OPTIONS_TAG_NAME)) {
                tagName = options.getString(OPTIONS_TAG_NAME);
            }
            if (options.hasKey(OPTIONS_THUMB_SIZE)) {
                thumbSize = options.getInt(OPTIONS_THUMB_SIZE);
            }
            if (options.hasKey(OPTIONS_TITLE)) {
                title = options.getString(OPTIONS_TITLE);
            }else{
                errCallback.invoke("please setting share title !");
                return;
            }
            if (options.hasKey(OPTIONS_DESC)) {
                desc = options.getString(OPTIONS_DESC);
            }
            if (options.hasKey(OPTIONS_THUMB_IMAGE)) {
                thumbImage = options.getString(OPTIONS_THUMB_IMAGE);
            }
            if (options.hasKey(OPTIONS_SCENE)) {
                scene = options.getInt(OPTIONS_SCENE);
            }
            shareWebPage(thumbImage);
        } else {
            if (errCallback != null) {
                errCallback.invoke("please setting options !");
            }
        }
    }

    /**
     * Added by buhe on 2015/12/29
     *
     * Edited by heng on 2015/12/29
     *
     * this method is used to share image to WeChat
     * errCallback return error
     */
    @ReactMethod
    public void sendImage(ReadableMap options, Callback errCallback) {
        if (WeChatModule.wxApi == null) {
            if (errCallback != null) {
                errCallback.invoke("please registerApp before this !");
            }
            return;
        }

        if (options != null) {
            if(options.hasKey(OPTIONS_IMAGE_SOURCE_TYPE)){
                imageSourceType = options.getInt(OPTIONS_IMAGE_SOURCE_TYPE);
            }
            if (options.hasKey(OPTIONS_SCENE)) {
                scene = options.getInt(OPTIONS_SCENE);
            }
            if (options.hasKey(OPTIONS_THUMB_SIZE)) {
                thumbSize = options.getInt(OPTIONS_THUMB_SIZE);
            }
            if(imageSourceType == 0){
                if (options.hasKey(OPTIONS_LOCAL_PATH)) {
                    localPath = options.getString(OPTIONS_LOCAL_PATH);
                    File file = new File(localPath);
                    if(file.exists()){
                        shareLocalImage();
                    } else {
                        if (errCallback != null) {
                            errCallback.invoke("the local path is not found file !");
                        }
                    }
                }else{
                    if (errCallback != null) {
                        errCallback.invoke("please setting image local path !");
                    }
                }
            }else{
                if (options.hasKey(OPTIONS_REMOTE_URL)) {
                    remoteUrl = options.getString(OPTIONS_REMOTE_URL);
                    shareRemoteImage();
                }else{
                    if (errCallback != null) {
                        errCallback.invoke("please setting image remote path !");
                    }
                }
            }
        } else {
            if (errCallback != null) {
                errCallback.invoke("please setting options !");
            }
        }
    }

    /**
     * Added by heng on 2015/12/18
     * <p/>
     * WeChat pay method
     * errCallback return error
     */
    @ReactMethod
    public void weChatPay(ReadableMap options, Callback errCallback) {
        if (WeChatModule.wxApi == null) {
            if (errCallback != null) {
                errCallback.invoke("please registerApp before this !");
            }
            return;
        }
        String appId = WeChatModule.appId;
        String nonceStr = "";
        String packageValue = "";
        String partnerId = "";
        String prepayId = "";
        String timeStamp = "";
        String sign = "";
        if (options != null) {
            if (options.hasKey(OPTIONS_APP_ID)) {
                appId = options.getString(OPTIONS_APP_ID);
            }
            if (options.hasKey(OPTIONS_NONCE_STR)) {
                nonceStr = options.getString(OPTIONS_NONCE_STR);
            }
            if (options.hasKey(OPTIONS_PACKAGE_VALUE)) {
                packageValue = options.getString(OPTIONS_PACKAGE_VALUE);
            }
            if (options.hasKey(OPTIONS_PARTNER_ID)) {
                partnerId = options.getString(OPTIONS_PARTNER_ID);
            }
            if (options.hasKey(OPTIONS_PREPAY_ID)) {
                prepayId = options.getString(OPTIONS_PREPAY_ID);
            }
            if (options.hasKey(OPTIONS_TIME_STAMP)) {
                timeStamp = options.getString(OPTIONS_TIME_STAMP);
            }
            if (options.hasKey(OPTIONS_SIGN)) {
                sign = options.getString(OPTIONS_SIGN);
            }
        }

        PayReq request = new PayReq();
        request.appId = appId;
        request.nonceStr = nonceStr;
        request.packageValue = packageValue;
        request.partnerId = partnerId;
        request.prepayId = prepayId;
        request.timeStamp = timeStamp;
        request.sign = sign;
        WeChatModule.wxApi.sendReq(request);
    }

    /**
     * Added by heng on 2015/12/29
     *
     * share web page
     */
    void shareWebPage(String thumbImage){
        WXWebpageObject webPage = new WXWebpageObject();
        webPage.webpageUrl = link;
        try {
            WXMediaMessage msg = new WXMediaMessage(webPage);
            Bitmap bitmap = BitmapFactory.decodeStream(new URL(thumbImage).openStream());
            weChatShare(msg, bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Added by heng on 2015/12/29
     *
     * share local image
     */
    void shareLocalImage(){
        WXImageObject wxImageObject = new WXImageObject();
        wxImageObject.setImagePath(localPath);

        Bitmap bitmap = BitmapFactory.decodeFile(localPath);
        WXMediaMessage msg = new WXMediaMessage(wxImageObject);
        weChatShare(msg, bitmap);
    }

    /**
     * Added by heng on 2015/12/29
     *
     * share remote image
     */
    void shareRemoteImage(){
        WXImageObject wxImageObject = new WXImageObject();
        wxImageObject.imageUrl = remoteUrl;
        try {
            WXMediaMessage msg = new WXMediaMessage(wxImageObject);
            Bitmap bitmap = BitmapFactory.decodeStream(new URL(remoteUrl).openStream());
            weChatShare(msg, bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Added by heng on 2015/12/29
     *
     * weChat share
     */
    void weChatShare(WXMediaMessage msg,Bitmap bitmap) {
        if(!TextUtils.isEmpty(tagName)){
            msg.mediaTagName = tagName;
        }
        if(!TextUtils.isEmpty(title)){
            msg.title = title;
        }
        if(!TextUtils.isEmpty(desc)){
            msg.description = desc;
        }
        if (bitmap != null) {
            Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, thumbSize, thumbSize, true);
            bitmap.recycle();
            msg.thumbData = bmpToByteArray(thumbBmp, true);
        }
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        if(!TextUtils.isEmpty(transaction)){
            req.transaction = transaction;
        }else{
            req.transaction = String.valueOf(System.currentTimeMillis());
        }
        req.message = msg;
        switch (scene) {
            case SCENE_SESSION:
                req.scene = SendMessageToWX.Req.WXSceneSession;
                break;
            case SCENE_TIMELINE:
                req.scene = SendMessageToWX.Req.WXSceneTimeline;
                break;
            case SCENE_FAVORITE:
                req.scene = SendMessageToWX.Req.WXSceneFavorite;
                break;
        }
        WeChatModule.currentAction = ACTION_SHARE;
        WeChatModule.wxApi.sendReq(req);
    }

    /**
     * Added by heng on 2015/12/29
     *
     * Bitmap to byte array
     */
    byte[] bmpToByteArray(Bitmap bmp, boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }
        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
