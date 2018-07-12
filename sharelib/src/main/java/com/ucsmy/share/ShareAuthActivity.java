package com.ucsmy.share;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * 回调的页面
 * Created by Seven on 2017/1/18.
 */

public class ShareAuthActivity extends Activity {

    private int mType;
    private boolean isNew;
    private static final String TYPE = "share_activity_type";

    public static Intent newInstance(Context context, int type) {
        Intent intent = new Intent(context, ShareAuthActivity.class);
        if (context instanceof Application) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.putExtra(TYPE, type);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isNew = true;

        mType = getIntent().getIntExtra(TYPE, 0);
        if (mType == ShareUtil.TYPE) {
            // 分享
            ShareUtil.action(this);
        } else {
            // 微信回调
            ShareUtil.handleResult(getIntent());
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isNew) {
            isNew = false;
        } else {
            finish();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ShareUtil.handleResult(intent);
//        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ShareUtil.handleResult(data);
//        finish();
    }

}
