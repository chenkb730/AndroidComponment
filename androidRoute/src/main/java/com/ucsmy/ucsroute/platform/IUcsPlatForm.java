package com.ucsmy.ucsroute.platform;

import com.ucsmy.ucsroute.UcsRoute;

/**
 * Created by Seven on 2017/5/10.
 */

public interface IUcsPlatForm<T extends UcsRoute.Builder> {

    void go(T builder);
}
