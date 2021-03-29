package nine.sample.webview.ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.net.http.SslError
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import nine.sample.webview.C
import nine.sample.webview.Global
import nine.sample.webview.R
import nine.sample.webview.util.defaultSetting

class WebViewFragment : BaseFragment() {

    init {
        instance = this
    }

    companion object {
        private var instance: WebViewFragment? = null

        fun getInstance(): WebViewFragment {
            return instance ?: WebViewFragment()
        }
    }

    private var mWebView: WebView? = null
    private var targetUrl: String = ""

    fun getWebView(): WebView? {
        return mWebView ?: view?.findViewById(R.id.webview)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { bundle ->
            targetUrl = bundle.getString(C.Extra.TARGET_URL) ?: ""
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_web_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mWebView = view.findViewById(R.id.webview)

        mWebView?.let { webView ->
            WebView.setWebContentsDebuggingEnabled(true)  // webview debug 설정 (chrome://inspect)

            webView.defaultSetting()
            webView.webViewClient = BaseWebViewClient()
            webView.webChromeClient = BaseWebChromeClient()
            webView.addJavascriptInterface(AndroidBridge(requireContext()), C.BRIDGE.APP_BRIDGE_NAME)
            refreshWebView(webView, targetUrl)
        }

        loadJsTest()
    }

    fun refreshWebView(webView: WebView, url: String) {
        webView.clearCache(true)
        webView.loadUrl(url)
    }

    fun goUrl(webView: WebView, url: String) {
        webView.loadUrl(url)
    }

    private fun loadJsTest() {
        mWebView?.let { webView ->
            webView.loadUrl("javascript:alert('loadJs Test!?')")
        }
    }



    open inner class BaseWebViewClient: WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
            Log.d(TAG, "[shouldOverrideUrlLoading]url:: ${request.url}")
            Log.d(TAG, "[shouldOverrideUrlLoading]scheme:: ${request.url.scheme}")
            Log.d(TAG, "[shouldOverrideUrlLoading]host:: ${request.url.host}")
            Log.d(TAG, "[shouldOverrideUrlLoading]path:: ${request.url.path}")
            Log.d(TAG, "[shouldOverrideUrlLoading]method:: ${request.method}")
            Log.d(TAG, "[shouldOverrideUrlLoading]query:: ${request.url.query}")

            request.url.scheme?.let { scheme ->
                if (scheme == "tel" || scheme == "mms" || scheme == "sms") {
                    val intent = Intent(Intent.ACTION_VIEW, request.url)
                    view.context.startActivity(intent)
                    return true
                }
            }
            return super.shouldOverrideUrlLoading(view, request)
        }

        override fun onPageFinished(view: WebView, url: String) {
            val uri = Uri.parse(url)

            Log.d(TAG, "[onPageFinished]url:: $url")
            Log.d(TAG, "[onPageFinished]scheme:: ${uri.scheme}")
            Log.d(TAG, "[onPageFinished]host:: ${uri.host}")
            Log.d(TAG, "[onPageFinished]path:: ${uri.path}")
            Log.d(TAG, "[onPageFinished]query:: ${uri.query}")

            uri.path?.let { path ->
                // 특정 페이지 진입 시 back history를 clear (필요시)
                if (path!!.contains(Global.MAIN_PATH)) {
                    view.clearHistory()
                }
            }
            super.onPageFinished(view, url)
        }

        override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
            AlertDialog.Builder(view.context)
                .setTitle(R.string.title_ssl_error)
                .setMessage(R.string.msg_ssl_error)
                .setPositiveButton(R.string.btn_ssl_proceed) { dialog, which ->
                    handler.proceed()
                }
                .setNegativeButton(R.string.btn_ssl_cancel) { dialog, which ->
                    handler.cancel()
                }
                .show()
        }
    }

    open inner class BaseWebChromeClient: WebChromeClient() {
        override fun onJsAlert(view: WebView, url: String?, message: String?, result: JsResult): Boolean {
            // web system alert dialog custom
            AlertDialog.Builder(view.context)
                .setMessage(message)
                .setPositiveButton(R.string.btn_confirm) { dialog, which ->
                    result?.confirm()
                }
                .setCancelable(false) // JsResult를 제대로 반환하지 않으면 webview가 먹통이 됨
                .show()
            return true // 이벤트 소비여부, false로 설정 시 super 메서드가 실행됨
        }

        override fun onJsConfirm(view: WebView, url: String?, message: String?, result: JsResult): Boolean {
            // web system confirm dialog custom
            AlertDialog.Builder(view.context)
                .setMessage(message)
                .setPositiveButton(R.string.btn_yes) { dialog, which ->
                    result?.confirm()
                }
                .setNegativeButton(R.string.btn_no) { dialog, which ->
                    result?.cancel()
                }
                .setCancelable(false) // JsResult를 제대로 반환하지 않으면 webview가 먹통이 됨
                .show()
            return true // 이벤트 소비여부, false로 설정 시 super 메서드가 실행됨
        }

        override fun onCreateWindow(view: WebView, isDialog: Boolean, isUserGesture: Boolean, resultMsg: Message): Boolean {
            // web에서 새창(window.open()) 호출 시 webview.setSupportMultipleWindows 값이 true 일 때에만 호출됨
            val dialog = Dialog(view.context, android.R.style.Theme_Translucent_NoTitleBar) // 다이얼로그에 전체화면 테마 세팅
            val newWebView = WebView(view.context)

            newWebView.defaultSetting()
            newWebView.webViewClient = BaseWebViewClient()
            newWebView.webChromeClient = object : BaseWebChromeClient() {
                override fun onCloseWindow(window: WebView) {
                    // window.close() 가 호출되었을 때
                    dialog.dismiss()
                }
            }

            val webViewTransport = resultMsg.obj as WebView.WebViewTransport
            webViewTransport.webView = newWebView
            resultMsg.sendToTarget()

            dialog.setContentView(webViewTransport.webView!!)
            dialog.show()

            return true
        }
    }
}