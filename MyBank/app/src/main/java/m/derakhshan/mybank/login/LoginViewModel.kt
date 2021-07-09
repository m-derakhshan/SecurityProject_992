package m.derakhshan.mybank.login


import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import m.derakhshan.mybank.Address
import m.derakhshan.mybank.Arrange
import m.derakhshan.mybank.Utils
import org.json.JSONObject


class LoginViewModel(private val showSnackBar: ShowSnackBar, application: Application) :
    AndroidViewModel(application) {

    val username = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val confirmPassword = MutableLiveData<String>()
    val gotoMainActivity = MutableLiveData<Boolean>()
    val startLoginAnimation = MutableLiveData<Boolean>()
    val startRegisterAnimation = MutableLiveData<Boolean>()


    fun login() {
        val info = JSONObject()
        info.put("username", username.value.toString())
        info.put("password", password.value.toString())
        startLoginAnimation.value = true
        val request =
            JsonObjectRequest(
                Request.Method.POST,
                Address().loginAPI,
                info,
                {
                    //-------------------------(server response)-----------------------//
                    Utils(getApplication()).wrongPassRetry = 0
                    Utils(getApplication()).accessToken = it.getString("access_token")
                    gotoMainActivity.value = true
                    startLoginAnimation.value = false
                },
                {
                    startLoginAnimation.value = false
                    showSnackBar.snackBar(msg = "خطا در ورود به حساب کاربری")
                    Utils(getApplication()).wrongPassRetry++
                    if (Utils(getApplication()).wrongPassRetry > 3) {
                        gotoMainActivity.value = true
                        Utils(getApplication()).wrongPassRetry = 0
                    }
                    try {
                        Log.i(
                            "Log",
                            "Error in LoginViewModel_login ${
                                String(
                                    it.networkResponse.data,
                                    Charsets.UTF_8
                                )
                            }"
                        )
                    } catch (e: Exception) {
                        Log.i("Log", "Error in LoginViewModel_Login $it")
                    }
                })
        request.retryPolicy = DefaultRetryPolicy(
            30000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        Volley.newRequestQueue(getApplication()).add(request)
    }


    fun register() {
        val passwordChecker = isPasswordStrong(password = password.value.toString())
        when {
            password.value.toString() != confirmPassword.value.toString() -> showSnackBar.snackBar(
                msg = "کلمه ی عبور با تکرار آن انطباق ندارد."
            )
            passwordChecker == PasswordProblem.NoProblem -> {
                //-------------------------(every thing is ok so request to server)-----------------------//
                startRegisterAnimation.value = true
                val info = JSONObject()
                info.put("username", username.value.toString())
                info.put("password1", password.value.toString())
                info.put("password2", confirmPassword.value.toString())
                Log.i(
                    "Log", "username is ${info["username"]} " +
                            "pass: ${info["password1"]} pass2:${info["password2"]}"
                )
                val requestRegister =
                    JsonObjectRequest(
                        Request.Method.POST,
                        Address().registerAPI,
                        info,
                        {
                            //-------------------------(server response)-----------------------//
                            startRegisterAnimation.value = false
                            Utils(getApplication()).accessToken = it.getString("access_token")
                            gotoMainActivity.value = true
                        },
                        {
                            startRegisterAnimation.value = false
                            showSnackBar.snackBar(msg = "خطا در ایجاد حساب کاربری")
                            try {
                                Log.i(
                                    "Log",
                                    "Error in Register ViewModel_login ${
                                        String(
                                            it.networkResponse.data,
                                            Charsets.UTF_8
                                        )
                                    }"
                                )
                            } catch (e: Exception) {
                                Log.i("Log", "Error in Register ViewModel_Login $it")
                            }
                        })
                requestRegister.retryPolicy = DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
                Volley.newRequestQueue(getApplication()).add(requestRegister)

            }
            else -> showSnackBar.snackBar(
                msg = Arrange().persianConverter(
                    "کلمه عبور باید ترکیبی از حروف بزرگ، حروف کوچک و عدد با طول حداقل 8 باشد."
                )
            )
        }

    }


    private fun isPasswordStrong(password: String): PasswordProblem {

        var hasDigit = false
        var hasUpperCase = false
        var hasLowerCase = false

        for (char in password) {
            when {
                char.isDigit() -> hasDigit = true
                char.isUpperCase() -> hasUpperCase = true
                char.isLowerCase() -> hasLowerCase = true
            }
        }
        return when {
            !hasDigit -> PasswordProblem.NoDigit
            !hasLowerCase -> PasswordProblem.NoLowerCase
            !hasUpperCase -> PasswordProblem.NoUpperCase
            password.length < 8 -> PasswordProblem.LengthError
            else -> PasswordProblem.NoProblem
        }

    }


    enum class PasswordProblem {
        LengthError,
        NoDigit,
        NoUpperCase,
        NoLowerCase,
        NoProblem
    }

}