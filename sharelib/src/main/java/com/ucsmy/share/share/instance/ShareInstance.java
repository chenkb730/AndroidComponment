package com.ucsmy.share.share.instance;

import android.content.Context;
import android.content.Intent;

import com.ucsmy.share.share.ShareImageObject;
import com.ucsmy.share.share.ShareListener;

/**
 * 分享接口
 * Created by Seven on 2016/12/30.
 */

public interface ShareInstance {

    void shareText(Context mContext, int platform, String text, ShareListener listener);

    void shareMedia(Context mContext, int platform, String title, String targetUrl, String summary,
                    ShareImageObject shareImageObject, ShareListener listener);

    void shareImage(Context mContext, int platform, ShareImageObject shareImageObject,
                    ShareListener listener);

    void handleResult(Intent data);

    boolean isInstall(Context context);

    void recycle();
}
