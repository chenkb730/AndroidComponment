package com.ucsmy.share.share.instance;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Pair;

import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.ucsmy.share.config.ShareConfig;
import com.ucsmy.share.share.ImageDecoder;
import com.ucsmy.share.share.ShareImageObject;
import com.ucsmy.share.share.ShareListener;
import com.ucsmy.share.share.SharePlatform;

import java.util.UUID;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.LongConsumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 微博分享
 * Created by Seven on 2016/12/30.
 */

public class WeiboShareInstance implements ShareInstance, WbShareCallback {
    /**
     * 微博分享限制thumb image必须小于2097152，否则点击分享会没有反应
     */

    private static final int TARGET_SIZE = 1024;

    private static final int TARGET_LENGTH = 2097152;

    private WbShareHandler wbShareHandler;

    private ShareListener listener;

    public WeiboShareInstance(Context context, String appId) {
//        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(context, appId);
//        mWeiboShareAPI.registerApp();
        WbSdk.install(context, new AuthInfo(context, appId, ShareConfig.instance().getWeiboRedirectUrl(), ShareConfig.instance().getWeiboScope()));

        wbShareHandler = new WbShareHandler((Activity) context);
        wbShareHandler.registerApp();
    }

    @Override
    public void shareText(Context mContext, int platform, String text, ShareListener listener) {
        WeiboMultiMessage message = new WeiboMultiMessage();
        message.textObject = getTextObj(text);

        sendRequest((Activity) mContext, message);
    }

    @Override
    public void shareMedia(Context mContext, int platform, final String title, final String targetUrl, String summary,
                           ShareImageObject shareImageObject,
                           final ShareListener listener) {
        String content = String.format("%s %s", title, targetUrl);
        shareTextOrImage((Activity) mContext, shareImageObject, content, listener);
    }

    @Override
    public void shareImage(Context mContext, int platform, ShareImageObject shareImageObject,
                           ShareListener listener) {
        shareTextOrImage((Activity) mContext, shareImageObject, null, listener);
    }

    @Override
    public void handleResult(Intent intent) {
//        SendMessageToWeiboResponse baseResponse =
//                new SendMessageToWeiboResponse(intent.getExtras());
//
//        switch (baseResponse.errCode) {
//            case WBConstants.ErrorCode.ERR_OK:
//                ShareUtil.mShareListener.shareSuccess();
//                break;
//            case WBConstants.ErrorCode.ERR_FAIL:
//                ShareUtil.mShareListener.shareFailure(new Exception(baseResponse.errMsg));
//                break;
//            case WBConstants.ErrorCode.ERR_CANCEL:
//                ShareUtil.mShareListener.shareCancel();
//                break;
//            default:
//                ShareUtil.mShareListener.shareFailure(new Exception(baseResponse.errMsg));
//        }

        if (null != wbShareHandler) {
            wbShareHandler.doResultIntent(intent, this);
        }
    }

    @Override
    public boolean isInstall(Context context) {
        if (null != wbShareHandler) {
            return wbShareHandler.isWbAppInstalled();
        }
        return false;
    }

    @Override
    public void recycle() {
        if (null != wbShareHandler) {
            wbShareHandler = null;
        }
    }

    private void shareTextOrImage(final Activity activity, final ShareImageObject shareImageObject, final String text,
                                  final ShareListener listener) {
        this.listener = listener;


        Flowable.create(new FlowableOnSubscribe<Pair<String, byte[]>>() {
            @Override
            public void subscribe(FlowableEmitter<Pair<String, byte[]>> emitter) throws Exception {
                try {
                    String path = ImageDecoder.decode(activity, shareImageObject);
                    emitter.onNext(Pair.create(path,
                            ImageDecoder.compress2Byte(path, TARGET_SIZE, TARGET_LENGTH)));
                    emitter.onComplete();
                } catch (Exception e) {
                    emitter.onError(e);
                }
            }

        }, BackpressureStrategy.BUFFER).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnRequest(new LongConsumer() {
                    @Override
                    public void accept(long t) throws Exception {
                        if (null != listener) {
                            listener.shareRequest();
                        }
                    }
                })
                .subscribe(new Consumer<Pair<String, byte[]>>() {
                    @Override
                    public void accept(Pair<String, byte[]> pair) throws Exception {
                        ImageObject imageObject = new ImageObject();
                        imageObject.imageData = pair.second;
                        imageObject.imagePath = pair.first;

                        WeiboMultiMessage message = new WeiboMultiMessage();
                        message.imageObject = imageObject;
                        if (!TextUtils.isEmpty(text)) {
                            TextObject textObject = new TextObject();
                            textObject.text = text;
                            message.textObject = textObject;
                        }

                        sendRequest(activity, message);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        listener.shareFailure(new Exception(throwable));
                    }
                });
    }

    private void sendRequest(Activity activity, WeiboMultiMessage message) {
        wbShareHandler.shareMessage(message, false);
    }

    /**
     * 创建文本消息对象。
     *
     * @return 文本消息对象。
     */
    private TextObject getTextObj(String text) {
        TextObject textObject = new TextObject();
        textObject.text = text;
        return textObject;
    }

    /**
     * 创建图片消息对象。
     *
     * @return 图片消息对象。
     */
    private ImageObject getImageObj(ShareImageObject shareImageObject) {
        if (null == shareImageObject || null == shareImageObject.getBitmap()) {
            return null;
        }
        ImageObject imageObject = new ImageObject();
        imageObject.setImageObject(shareImageObject.getBitmap());
        return imageObject;
    }

    /**
     * 创建多媒体（网页）消息对象。
     *
     * @return 多媒体（网页）消息对象。
     */
    private WebpageObject getWebpageObj(final String title, final String targetUrl, String summary, ShareImageObject shareImageObject) {
        WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = UUID.randomUUID().toString().replace("-", "");
        mediaObject.title = title;
        mediaObject.description = summary;
        // 设置 Bitmap 类型的图片到视频对象里         设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
        if (null != shareImageObject && null != shareImageObject.getBitmap()) {
            mediaObject.setThumbImage(shareImageObject.getBitmap());
        }
        mediaObject.actionUrl = targetUrl;
        mediaObject.defaultText = "";
        return mediaObject;
    }

    @Override
    public void onWbShareSuccess() {
        if (null != listener) {
            listener.shareSuccess();
        }
    }

    @Override
    public void onWbShareCancel() {
        if (null != listener) {
            listener.shareCancel();
        }
    }

    @Override
    public void onWbShareFail() {
        if (null != listener) {
            listener.shareFailure(new Exception("onWbShareFail"));
        }
    }
}
