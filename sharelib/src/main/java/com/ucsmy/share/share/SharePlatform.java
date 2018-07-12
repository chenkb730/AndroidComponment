package com.ucsmy.share.share;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 分享对象
 * Created by Seven on 2016/12/30.
 */

public class SharePlatform {

    @IntDef({DEFAULT, QQ, QZONE, WEIBO, WX, WX_TIMELINE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Platform {
    }

    public static final int DEFAULT = 0x000000;
    public static final int QQ = 0x000001;
    public static final int QZONE = 0x000002;
    public static final int WX = 0x000003;
    public static final int WX_TIMELINE = 0x000004;
    public static final int WEIBO = 0x000005;


}
