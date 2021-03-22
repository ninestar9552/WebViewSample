package nine.sample.webview.util

import android.os.Handler
import android.os.Looper

object UIThread {

    /**
     * main thread 이외에서 UI 를 변경하기 위한 핸들러
     */
    private val mHandler: Handler by lazy { Handler(Looper.getMainLooper()) }

    /**
     * main thread 이외의 곳에서 UI 변경.
     *
     * @param r 수행할 일
     */
    fun execute(r: Runnable) {
        mHandler.post(r)
    }

    /**
     * main thread 이외의 곳에서 UI 변경.
     *
     * @param r    수행할 일
     * @param time 지연시간.
     */
    fun execute(r: Runnable, time: Long) {
        mHandler.postDelayed(r, time)
    }

}