package nine.sample.webview.ui

import androidx.fragment.app.Fragment
import nine.sample.webview.Global

open class BaseFragment: Fragment() {

    protected var TAG = javaClass.simpleName

    protected val mGlobal = Global.getInstance()

    fun getParentActivity(): BaseActivity? {
        val mParentActivity = requireActivity()
        return if (mParentActivity is BaseActivity) {
            mParentActivity
        } else {
            null
        }
    }
}