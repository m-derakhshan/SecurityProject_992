package m.derakhshan.mybank


import android.content.Context
import android.graphics.Typeface
import android.view.View
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar

class Utils(private val context: Context) {

    fun showSnackBar(color: Int, msg: String, snackView: View): Snackbar {
        val font = Typeface.createFromAsset(context.assets, "vazir.ttf")
        val snackBar = Snackbar.make(snackView, msg, Snackbar.LENGTH_LONG)
        val view = snackBar.view
        view.setBackgroundColor(color)
        view.layoutDirection = View.LAYOUT_DIRECTION_RTL
        val txt = view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        val action = view.findViewById<TextView>(com.google.android.material.R.id.snackbar_action)
        action.typeface = font
        txt.typeface = font
        return snackBar
    }

    private val share = context.getSharedPreferences("share", Context.MODE_PRIVATE)
    private val editor = share.edit()


    var accessToken: String
        set(value) {
            editor.putString("accessToken", value)
            editor.apply()
        }
        get() = share.getString("accessToken", "") ?: ""


    var wrongPassRetry: Int
        set(value) {
            editor.putInt("wrongPassRetry", value)
            editor.apply()
        }
        get() = share.getInt("wrongPassRetry", 0)

}