package nine.sample.webview.ui

import android.content.Context
import android.webkit.JavascriptInterface
import android.widget.Toast
import nine.sample.webview.Global
import nine.sample.webview.util.UIThread

class AndroidBridge(context: Context) {

    private val TAG = this.javaClass.simpleName
    private val mContext = context
    private val mGlobal = Global.getInstance()

    @JavascriptInterface
    fun showToast(msg: String) {
        UIThread.execute {
            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show()
        }
    }

}