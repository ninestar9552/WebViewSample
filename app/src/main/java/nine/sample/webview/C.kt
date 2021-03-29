package nine.sample.webview

class C {
    object Push {
        const val FCM_KEY = "fcm_key"
    }

    object Extra {
        const val METHOD_KEY = "method_key"
        const val TARGET_URL = "target_url"
        const val BRIDGE_TYPE = "bridge_type"
    }

    object METHOD_KEYS {
        const val BRIDGE = "method_key_bridge"
        const val GO_URL = "method_key_go_url"
    }

    object BRIDGE {
        const val APP_BRIDGE_NAME = "AndroidBridge"

        const val TYPE_AUTO_LOGIN = "auto_login"
        const val TYPE_LOGOUT = "logout"
    }
}