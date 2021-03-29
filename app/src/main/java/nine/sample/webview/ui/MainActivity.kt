package nine.sample.webview.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
        intent.getStringExtra(C.Extra.METHOD_KEY)?.let { methodKey ->
            when(methodKey) {
                C.METHOD_KEYS.GO_URL -> {
                    intent.getStringExtra(C.Extra.TARGET_URL)?.let { url ->
                        Log.d(TAG, "TARGET_URL: $url")
                        goWebView(targetUrl = url)
                    }
                }
                else -> {

                }
            }
        }
    }

    private fun goWebView(targetUrl: String) {
        WebViewFragment.getInstance().getWebView()?.let { webView ->
            WebViewFragment.getInstance().goUrl(webView, targetUrl)
        } ?: run {
            val webViewFragment = WebViewFragment.getInstance()
            val bundle = Bundle()
            bundle.putString(C.Extra.TARGET_URL, targetUrl)
            webViewFragment.arguments = bundle
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, webViewFragment)
                    .commit()
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