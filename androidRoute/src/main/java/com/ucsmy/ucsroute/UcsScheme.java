package com.ucsmy.ucsroute;

/**
 * 自定义协议
 * Created by Seven on 2017/3/6.
 */

public enum UcsScheme {
    COMMON("#"),
    HTTP("http"),
    HTTPS("https"),
    IMAGES("image"),
    MSG("msg"),
    UCS("ucs");//ucs://xxx?key={}

    private String key;

    UcsScheme(String key) {
        this.key = key;
    }


    @Override
    public String toString() {
        return key;
    }
}
