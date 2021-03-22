package nine.sample.webview.ui

import android.content.Intent
import android.os.Bundle
import nine.sample.webview.C
import nine.sample.webview.R
import nine.sample.webview.util.UIThread

class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        UIThread.execute({
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(C.Web.METHOD_KEY, C.Web.METHOD_GO_URL)
            intent.putExtra(C.Web.TARGET_URL, mGlobal.getBaseUrl())
            startActivity(intent)
            finish()
        }, 1000)
    }
}