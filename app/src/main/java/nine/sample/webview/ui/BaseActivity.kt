package nine.sample.webview.ui

import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import nine.sample.webview.Global
import nine.sample.webview.R

open class BaseActivity: AppCompatActivity() {

    protected open var TAG = javaClass.simpleName

    val mGlobal by lazy { Global.getInstance() }

    protected fun getTopVisibleFragment(): Fragment? {
        for (i in supportFragmentManager.fragments.indices.reversed()) {
            if (supportFragmentManager.fragments[i].isVisible) {
                return supportFragmentManager.fragments[i]
            }
        }
        return null
    }

    protected fun showFinishAlert() {
        AlertDialog.Builder(this)
        .setMessage(R.string.msg_finish_app)
        .setPositiveButton(R.string.btn_yes) { dialog, which ->
            finish()
        }
        .setNegativeButton(R.string.btn_no, null)
        .show()
    }
}