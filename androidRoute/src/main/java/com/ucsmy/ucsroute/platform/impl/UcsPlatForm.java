package com.ucsmy.ucsroute.platform.impl;

import com.ucsmy.ucsroute.ActivityBuilder;
import com.ucsmy.ucsroute.platform.IUcsPlatForm;

/**
 * 自己的协议
 * Created by Seven on 2017/5/10.
 */

final class UcsPlatForm implements IUcsPlatForm<ActivityBuilder> {

    @Override
    public void go(ActivityBuilder builder) {
        builder.startActivity();
    }
}
