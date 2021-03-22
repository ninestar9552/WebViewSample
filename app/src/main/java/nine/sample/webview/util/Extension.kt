package nine.sample.webview.util

import android.util.Xml
import android.webkit.WebSettings
import android.webkit.WebView

fun WebView.defaultSetting() {
    this.settings.javaScriptEnabled = true // 자바스크립트 사용 허용
    this.settings.useWideViewPort = true // wide viewport를 사용하도록 설정
    this.settings.loadWithOverviewMode = true // 웹 컨텐츠가 웹뷰보다 클 경우 스크린 크기에 맞게 조정
    this.settings.domStorageEnabled = true // web에서 local storage를 사용할 경우 필요한 옵션
    this.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW // HTTPS HTTP의 연동, 서로 호출 가능하도록
    this.settings.defaultTextEncodingName = Xml.Encoding.UTF_8.name // 한글이 깨질 경우 대비 UTF8로 인코딩 하도록 Default 설정

    // setSupportMultipleWindows(true) -> webChromeClient의 onCreateWindow가 호출됨
    // setSupportMultipleWindows(false) -> window.open()으로 새창 호출 시 window.close()가 동작하지 않음 [Scripts may close only the windows that were opened by them.]
    this.settings.setSupportMultipleWindows(true) // 여러개의 윈도우를 사용할 수 있도록 설정
    this.settings.javaScriptCanOpenWindowsAutomatically = true // 웹뷰내의 JS의 window.open()을 허용할 것인지에 대한 여부
}