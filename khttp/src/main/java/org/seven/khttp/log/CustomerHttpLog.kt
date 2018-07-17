package org.seven.khttp.log

import android.util.Log
import okhttp3.ResponseBody
import org.seven.khttp.config.KHttpConfig

/**
 * Created by Seven on 2018/7/16.
 */
internal class CustomerHttpLog : KHttpLogInterface {
    override fun log(response: ResponseBody?) {
        val responseStr = response?.string()
        when (KHttpConfig.getInstance().logTag) {
            KHttpLogInterface.LOGTAG.VERBOSE -> {
                Log.v("kHttp", "response is $responseStr")
            }
            KHttpLogInterface.LOGTAG.ERROR -> {
                Log.e("kHttp", "response is $responseStr")
            }
            KHttpLogInterface.LOGTAG.INFO -> {
                Log.i("kHttp", "response is $responseStr")
            }
            KHttpLogInterface.LOGTAG.WARNING -> {
                Log.w("kHttp", "response is $responseStr")
            }
            KHttpLogInterface.LOGTAG.DEBUG -> {
                Log.d("kHttp", "response is $responseStr")
            }
            else -> Log.wtf("kHttp", "response is $responseStr")


        }

    }
}