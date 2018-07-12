package com.ucsmy.share.share.instance;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.ucsmy.share.R;
import com.ucsmy.share.share.ImageDecoder;
import com.ucsmy.share.share.ShareImageObject;
import com.ucsmy.share.share.ShareListener;

import java.io.File;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.LongConsumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 系统分享
 * Created by Seven on 2016/12/30.
 */

public class DefaultShareInstance implements ShareInstance {

    @Override
    public void shareText(Context mContext, int platform, String text, ShareListener listener) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");
        mContext.startActivity(Intent.createChooser(sendIntent,
                mContext.getResources().getString(R.string.app_name)));
    }

    @Override
    public void shareMedia(Context mContext, int platform, String title, String targetUrl, String summary,
                           ShareImageObject shareImageObject, ShareListener listener) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, String.format("%s %s", title, targetUrl));
        sendIntent.setType("text/plain");
        mContext.startActivity(Intent.createChooser(sendIntent,
                mContext.getResources().getString(R.string.app_name)));
    }

    @Override
    public void shareImage(final Context mContext, int platform, final ShareImageObject shareImageObject,
                           final ShareListener listener) {
        Flowable.create(new FlowableOnSubscribe<Uri>() {
            @Override
            public void subscribe(FlowableEmitter<Uri> emitter) throws Exception {
                try {
                    Uri uri =
                            Uri.fromFile(new File(ImageDecoder.decode(mContext, shareImageObject)));
                    emitter.onNext(uri);
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
                        listener.shareRequest();
                    }
                }).subscribe(new Consumer<Uri>() {
            @Override
            public void accept(Uri uri) throws Exception {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                shareIntent.setType("image/jpeg");
                mContext.startActivity(Intent.createChooser(shareIntent,
                        mContext.getResources().getText(R.string.app_name)));
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
        // Default share, do nothing
    }

    @Override
    public boolean isInstall(Context context) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        return context.getPackageManager()
                .resolveActivity(shareIntent, PackageManager.MATCH_DEFAULT_ONLY) != null;
    }

    @Override
    public void recycle() {

    }
}
