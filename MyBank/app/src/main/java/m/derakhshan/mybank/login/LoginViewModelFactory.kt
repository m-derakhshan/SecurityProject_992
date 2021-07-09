package m.derakhshan.mybank.login

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class LoginViewModelFactory(private val showSnackBar: ShowSnackBar,private val application: Application) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(ShowSnackBar::class.java,Application::class.java)
            .newInstance(showSnackBar,application)
    }
}