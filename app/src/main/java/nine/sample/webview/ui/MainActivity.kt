package nine.sample.webview.ui

import android.content.Intent
import android.os.Bundle
import nine.sample.webview.R
import nine.sample.webview.C

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        intent?.let { goIntent(it) }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let { goIntent(it) }
    }

    private fun goIntent(intent: Intent) {
        val methodKey = intent.getStringExtra(C.Web.METHOD_KEY)
        methodKey?.let { method ->
            when(method) {
                C.Web.METHOD_GO_URL -> {
                    val targetUrl = intent.getStringExtra(C.Web.TARGET_URL)
                    targetUrl?.let { url ->
                        val webViewFragment = WebViewFragment.getInstance()
                        val bundle = Bundle()
                        bundle.putString(C.Web.TARGET_URL, url)
                        webViewFragment.arguments = bundle
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.container, webViewFragment)
                            .commit()
                    }
                }
                else -> {

                }
            }
        }
    }

    private fun webViewBack(fragment: WebViewFragment) {
        fragment.getWebView()?.let { webView ->
            if (webView.canGoBack()) {
                webView.goBack()
            } else {
                showFinishAlert()
            }
        } ?: run {
            super.onBackPressed()
        }
    }

    override fun onBackPressed() {
        getTopVisibleFragment()?.let { fragment ->
            when (fragment) {
                is WebViewFragment -> {
                    webViewBack(fragment)
                }
                else -> {
                    super.onBackPressed()
                }
            }
        } ?: run {
            super.onBackPressed()
        }
    }
}