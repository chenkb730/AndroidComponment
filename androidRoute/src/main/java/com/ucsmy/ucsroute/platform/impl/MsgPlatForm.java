package com.ucsmy.ucsroute.platform.impl;

import android.widget.Toast;

import com.ucsmy.ucsroute.UcsRoute;
import com.ucsmy.ucsroute.platform.IUcsPlatForm;

import java.net.URLDecoder;

/**
 * 项目
 * Created by Seven on 2017/5/10.
 */

final class MsgPlatForm implements IUcsPlatForm {

    @Override
    public void go(UcsRoute.Builder builder) {
        if(null == builder) {
            return;
        }
        if(null == builder.uri) {
            return;
        }

        Toast.makeText(builder.mContext, URLDecoder.decode(builder.uri.host()), Toast.LENGTH_LONG).show();
    }
}
