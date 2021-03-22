package nine.sample.webview

import android.content.Context
import androidx.preference.PreferenceManager

class Global private constructor() {
    private lateinit var context: Context
    private val TAG by lazy { context.applicationInfo.name }

    init {
        instance = this
    }

    companion object {
        private var instance: Global? = null

        fun getInstance(): Global {
            return instance ?: Global()
        }

        private const val IS_RELEASE = false
        const val LOGIN_PATH = "/login.html"
        const val MAIN_PATH = "/main.html"
    }

    fun setContext(context: Context) {
        this.context = context
    }

    fun getBaseUrl(): String {
        return if (IS_RELEASE) {
            // 운영서버 base url
            "https://m.naver.com"
        } else {
            // 개발서버 base url
            "https://www.google.co.kr/"
        }
    }

    fun putString(key: String?, value: String?) {
        val prefsEditor = PreferenceManager.getDefaultSharedPreferences(context).edit()
        prefsEditor.putString(key, value)
        prefsEditor.commit()
    }

    fun getString(key: String?): String? {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(key, null)
    }

    fun putBoolean(key: String?, value: Boolean) {
        val prefsEditor = PreferenceManager.getDefaultSharedPreferences(context).edit()
        prefsEditor.putBoolean(key, value)
        prefsEditor.commit()
    }

    fun getBoolean(key: String?, def: Boolean): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(context)
            .getBoolean(key, def)
    }
}