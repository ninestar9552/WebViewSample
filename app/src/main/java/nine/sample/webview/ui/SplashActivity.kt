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
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    .putExtra(C.Extra.METHOD_KEY, C.METHOD_KEYS.GO_URL)
                    .putExtra(C.Extra.TARGET_URL, mGlobal.getBaseUrl())
            startActivity(intent)
            finish()
        }, 1000)
    }
}