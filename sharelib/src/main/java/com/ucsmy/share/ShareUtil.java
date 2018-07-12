package com.ucsmy.share;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import com.tencent.tauth.Tencent;
import com.ucsmy.share.config.INFO;
import com.ucsmy.share.share.ShareImageObject;
import com.ucsmy.share.share.ShareListener;
import com.ucsmy.share.share.SharePlatform;
import com.ucsmy.share.share.instance.DefaultShareInstance;
import com.ucsmy.share.share.instance.QQShareInstance;
import com.ucsmy.share.share.instance.ShareInstance;
import com.ucsmy.share.share.instance.WeiboShareInstance;
import com.ucsmy.share.share.instance.WxShareInstance;


/**
 * 分享的操作
 * Created by shaohui on 2016/11/18.
 */

public class ShareUtil {

    public static final int TYPE = 798;


    public static ShareListener mShareListener;
    private static ShareInstance mShareInstance;

    public final static int TYPE_IMAGE = 1;
    public final static int TYPE_TEXT = 2;
    public final static int TYPE_MEDIA = 3;

    private static int mType;
    private static int mPlatform;
    private static String mText;
    private static ShareImageObject mShareImageObject;
    private static String mTitle;
    private static String mSummary;
    private static String mTargetUrl;

    /**
     * 主方法
     *
     * @param activity
     */
    static void action(Activity activity) {

        mShareInstance = getShareInstance(activity, mPlatform);

        if (null != mShareListener && null == ShareManager.CONFIG) {
            mShareListener.shareFailure(new Exception(INFO.SHARE_INIT_ERROR));
//            activity.finish();
            return;
        }

        if (null != mShareListener && !mShareInstance.isInstall(activity)) {
            mShareListener.shareFailure(new Exception(INFO.NOT_INSTALL));
//            activity.finish();
            return;
        }


        switch (mType) {
            case TYPE_TEXT:
                mShareInstance.shareText(activity, mPlatform, mText, mShareListener);
                break;
            case TYPE_IMAGE:
                mShareInstance.shareImage(activity, mPlatform, mShareImageObject, mShareListener);
                break;
            case TYPE_MEDIA:
                mShareInstance.shareMedia(activity, mPlatform, mTitle, mTargetUrl, mSummary,
                        mShareImageObject, mShareListener);
                break;
        }
    }

    /**
     * 分享文本
     *
     * @param context
     * @param platform
     * @param text
     * @param listener
     */
    public static void shareText(Context context, @SharePlatform.Platform int platform, String text,
                                 ShareListener listener) {
        mType = TYPE_TEXT;
        mText = text;
        mPlatform = platform;
        mShareListener = buildProxyListener(listener);
        action((Activity) context);

        context.startActivity(ShareAuthActivity.newInstance(context, TYPE));
    }

    /**
     * 分享图片
     *
     * @param context
     * @param platform
     * @param urlOrPath
     * @param listener
     */
    public static void shareImage(Context context, @SharePlatform.Platform final int platform,
                                  final String urlOrPath, ShareListener listener) {
        mType = TYPE_IMAGE;
        mPlatform = platform;
        mShareImageObject = new ShareImageObject(urlOrPath);
        mShareListener = buildProxyListener(listener);

        context.startActivity(ShareAuthActivity.newInstance(context, TYPE));
    }

    /**
     * 分享本地图片
     *
     * @param context
     * @param platform
     * @param bitmap
     * @param listener
     */
    public static void shareImage(Context context, @SharePlatform.Platform final int platform,
                                  final Bitmap bitmap, ShareListener listener) {
        mType = TYPE_IMAGE;
        mPlatform = platform;
        mShareImageObject = new ShareImageObject(bitmap);
        mShareListener = buildProxyListener(listener);
        context.startActivity(ShareAuthActivity.newInstance(context, TYPE));
    }

    /**
     * 分享多媒体
     *
     * @param context
     * @param platform
     * @param title
     * @param summary
     * @param targetUrl
     * @param thumb
     * @param listener
     */
    public static void shareMedia(Context context, @SharePlatform.Platform int platform,
                                  String title, String summary, String targetUrl, Bitmap thumb, ShareListener listener) {
        mType = TYPE_MEDIA;
        mPlatform = platform;
        mShareImageObject = new ShareImageObject(thumb);
        mSummary = summary;
        mTargetUrl = targetUrl;
        mTitle = title;
        mShareListener = buildProxyListener(listener);

        context.startActivity(ShareAuthActivity.newInstance(context, TYPE));
    }

    /**
     * 分享多媒体
     *
     * @param context
     * @param platform
     * @param title
     * @param summary
     * @param targetUrl
     * @param thumbUrlOrPath
     * @param listener
     */
    public static void shareMedia(Context context, @SharePlatform.Platform int platform,
                                  String title, String summary, String targetUrl, String thumbUrlOrPath,
                                  ShareListener listener) {
        mType = TYPE_MEDIA;
        mPlatform = platform;
        mShareImageObject = new ShareImageObject(thumbUrlOrPath);
        mSummary = summary;
        mTargetUrl = targetUrl;
        mTitle = title;
        mShareListener = buildProxyListener(listener);

        context.startActivity(ShareAuthActivity.newInstance(context, TYPE));
    }

    private static ShareListener buildProxyListener(ShareListener listener) {
        return new ShareListenerProxy(listener);
    }

    public static void handleResult(Intent data) {
        // 微博分享会同时回调onActivityResult和onNewIntent， 而且前者返回的intent为null
        if (mShareInstance != null && data != null) {
            mShareInstance.handleResult(data);
        }
    }

    private static ShareInstance getShareInstance(Context context, @SharePlatform.Platform int platform) {
        switch (platform) {
            case SharePlatform.WX:
            case SharePlatform.WX_TIMELINE:
                return new WxShareInstance(context, ShareManager.CONFIG.getWxId());
            case SharePlatform.QQ:
            case SharePlatform.QZONE:
                return new QQShareInstance(context, ShareManager.CONFIG.getQqId());
            case SharePlatform.WEIBO:
                return new WeiboShareInstance(context, ShareManager.CONFIG.getWeiboId());
            case SharePlatform.DEFAULT:
            default:
                return new DefaultShareInstance();
        }
    }


    public static void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (null != mShareListener) {
            Tencent.onActivityResultData(requestCode, resultCode, data, mShareListener);
        }
    }

    public static void recycle() {
        mTitle = null;
        mSummary = null;
        mShareListener = null;

        if (mShareImageObject != null
                && mShareImageObject.getBitmap() != null
                && !mShareImageObject.getBitmap().isRecycled()) {
            mShareImageObject.getBitmap().recycle();
        }
        mShareImageObject = null;

        if (mShareInstance != null) {
            mShareInstance.recycle();
        }
        mShareInstance = null;
    }

    /**
     * 检查客户端是否安装
     */

    public static boolean isInstalled(@SharePlatform.Platform int platform, Context context) {
        mShareInstance = getShareInstance(context, platform);
        switch (platform) {
            case SharePlatform.QQ:
            case SharePlatform.QZONE:
            case SharePlatform.WEIBO:
            case SharePlatform.WX:
            case SharePlatform.WX_TIMELINE:
                return mShareInstance.isInstall(context);
            case SharePlatform.DEFAULT:
                return true;
            default:
                return false;
        }
    }


    private static class ShareListenerProxy extends ShareListener {

        private final ShareListener mShareListener;

        ShareListenerProxy(ShareListener listener) {
            mShareListener = listener;
        }

        @Override
        public void shareSuccess() {
            ShareUtil.recycle();
            if (null != mShareListener) {
                mShareListener.shareSuccess();
            }
        }

        @Override
        public void shareFailure(Exception e) {
            ShareUtil.recycle();
            if (null != mShareListener) {
                mShareListener.shareFailure(e);
            }
        }

        @Override
        public void shareCancel() {
            ShareUtil.recycle();
            if (null != mShareListener) {
                mShareListener.shareCancel();
            }
        }

        @Override
        public void shareRequest() {
            if (null != mShareListener) {
                mShareListener.shareRequest();
            }
        }
    }
}
