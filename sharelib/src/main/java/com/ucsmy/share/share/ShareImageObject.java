package com.ucsmy.share.share;

import android.graphics.Bitmap;

/**
 * 图片对象
 * Created by Seven on 2016/12/30.
 */

public class ShareImageObject {

    private Bitmap mBitmap;

    private String mPathOrUrl;

    public ShareImageObject(Bitmap bitmap) {
        mBitmap = bitmap;
    }

    public ShareImageObject(String pathOrUrl) {
        mPathOrUrl = pathOrUrl;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
    }

    public String getPathOrUrl() {
        return mPathOrUrl;
    }

    public void setPathOrUrl(String pathOrUrl) {
        mPathOrUrl = pathOrUrl;
    }
}
