package com.ucsmy.share.share.instance;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;

import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzonePublish;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.Tencent;
import com.ucsmy.share.ShareUtil;
import com.ucsmy.share.config.INFO;
import com.ucsmy.share.share.ImageDecoder;
import com.ucsmy.share.share.ShareImageObject;
import com.ucsmy.share.share.ShareListener;
import com.ucsmy.share.share.SharePlatform;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Emitter;
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
 * qq分享
 * Created by Seven on 2016/12/30.
 */

public class QQShareInstance implements ShareInstance {

    private Tencent mTencent;

    private Context mContext;

    public QQShareInstance(Context mContext, String app_id) {
        this.mContext = mContext;
        mTencent = Tencent.createInstance(app_id, mContext.getApplicationContext());
    }

    @Override
    public void shareText(Context mContext, int platform, String text, ShareListener listener) {
        if (platform == SharePlatform.QZONE) {
            shareToQZoneForText((Activity) mContext, text, listener);
        } else {
            listener.shareFailure(new Exception(INFO.QQ_NOT_SUPPORT_SHARE_TXT));
        }
    }

    @Override
    public void shareMedia(final Context mContext, final int platform, final String title, final String targetUrl,
                           final String summary, final ShareImageObject shareImageObject,
                           final ShareListener listener) {
        Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(FlowableEmitter<String> emitter) throws Exception {
                try {
                    emitter.onNext(ImageDecoder.decode(mContext, shareImageObject));
                    emitter.onComplete();
                } catch (Exception e) {
                    emitter.onError(e);
                }
            }

        }, BackpressureStrategy.DROP).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnRequest(new LongConsumer() {
                    @Override
                    public void accept(long t) throws Exception {
                        if (null != listener) {
                            listener.shareRequest();
                        }
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        if (platform == SharePlatform.QZONE) {
                            shareToQZoneForMedia((Activity) mContext, title, targetUrl, summary, s,
                                    listener);
                        } else {
                            shareToQQForMedia((Activity) mContext, title, summary, targetUrl, s, listener);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        listener.shareFailure(new Exception(throwable));
                    }
                });
    }

    @Override
    public void shareImage(final Context mContext, final int platform, final ShareImageObject shareImageObject,
                           final ShareListener listener) {

        Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(FlowableEmitter<String> emitter) throws Exception {
                try {
                    emitter.onNext(ImageDecoder.decode(mContext, shareImageObject));
                    emitter.onComplete();
                } catch (Exception e) {
                    emitter.onError(e);
                }
            }

        }, BackpressureStrategy.DROP).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnRequest(new LongConsumer() {
                    @Override
                    public void accept(long t) throws Exception {
                        if (null != listener) {
                            listener.shareRequest();
                        }
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String localPath) throws Exception {
                        if (platform == SharePlatform.QZONE) {
                            shareToQZoneForImage((Activity) mContext, localPath, listener);
                        } else {
                            shareToQQForImage((Activity) mContext, localPath, listener);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        listener.shareFailure(new Exception(throwable));
                    }
                });
    }

    @Override
    public void handleResult(Intent data) {
        Tencent.handleResultData(data, ShareUtil.mShareListener);
    }

    @Override
    public boolean isInstall(Context context) {
        PackageManager pm = context.getPackageManager();
        if (pm == null) {
            return false;
        }

        List<PackageInfo> packageInfos = pm.getInstalledPackages(0);
        for (PackageInfo info : packageInfos) {
            if (TextUtils.equals(info.packageName.toLowerCase(), "com.tencent.mobileqq")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void recycle() {
        if (mTencent != null) {
            mTencent.releaseResource();
            mTencent = null;
        }

        ImageDecoder.recycle(mContext);
    }

    private void shareToQQForMedia(Activity activity, String title, String summary, String targetUrl, String thumbUrl,
                                   ShareListener listener) {
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, summary);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, targetUrl);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, thumbUrl);
        mTencent.shareToQQ(activity, params, listener);
    }

    private void shareToQQForImage(Activity activity, String localUrl, ShareListener listener) {
        Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, localUrl);
        mTencent.shareToQQ(activity, params, listener);
    }

    private void shareToQZoneForText(Activity activity, String text, ShareListener listener) {
        final Bundle params = new Bundle();
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE,
                QzonePublish.PUBLISH_TO_QZONE_TYPE_PUBLISHMOOD);
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, text);
        mTencent.publishToQzone(activity, params, listener);
    }

    private void shareToQZoneForMedia(Activity activity, String title, String targetUrl, String summary,
                                      String imageUrl, ShareListener listener) {
        final Bundle params = new Bundle();
        final ArrayList<String> image = new ArrayList<>();
        image.add(imageUrl);
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE,
                QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, title);
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, summary);
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, targetUrl);
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, image);
        mTencent.shareToQzone(activity, params, listener);
    }

    private void shareToQZoneForImage(Activity activity, String imagePath, ShareListener listener) {
        final Bundle params = new Bundle();
        final ArrayList<String> image = new ArrayList<>();
        image.add(imagePath);
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzonePublish.PUBLISH_TO_QZONE_TYPE_PUBLISHMOOD);
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, image);
        mTencent.publishToQzone(activity, params, listener);
    }
}
