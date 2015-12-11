package com.heng.wechat;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by heng on 15/12/10.
 */
public class WeChatModule extends ReactContextBaseJavaModule {

    public static final String REACT_MODULE_NAME = "WeChatAndroid";

    public static IWXAPI wxApi = null;
    public static ReactApplicationContext reactApplicationContext;

    public WeChatModule(ReactApplicationContext reactContext) {
        super(reactContext);
        WeChatModule.reactApplicationContext = reactContext;
    }

    @Override
    public String getName() {
        return REACT_MODULE_NAME;
    }

    @ReactMethod
    public void registerApp(String appId) {
        WeChatModule.wxApi = WXAPIFactory.createWXAPI(getReactApplicationContext(),appId, true);
        WeChatModule.wxApi.registerApp(appId);
    }

    @ReactMethod
    public void isWXAppInstalled(Callback callback) {
        callback.invoke(WeChatModule.wxApi.isWXAppInstalled());
    }

    @ReactMethod
    public void sendAuthRequest(String state) {
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = state;
        WeChatModule.wxApi.sendReq(req);
    }
}
