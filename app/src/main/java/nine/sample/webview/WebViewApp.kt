package nine.sample.webview

import android.app.Application

class WebViewApp: Application() {

    override fun onCreate() {
        super.onCreate()

        Global.getInstance().setContext(this)
    }
}