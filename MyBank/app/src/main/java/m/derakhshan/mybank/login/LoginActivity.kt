package m.derakhshan.mybank.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import m.derakhshan.mybank.MainActivity
import m.derakhshan.mybank.R
import m.derakhshan.mybank.Utils
import m.derakhshan.mybank.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity(), ShowSnackBar {
    lateinit var binding: ActivityLoginBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        val factory = LoginViewModelFactory(this, application)
        val viewModel = ViewModelProvider(this, factory).get(LoginViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.loginToAccount.setOnClickListener {
            binding.mainLayout.transitionToStart()
        }

        viewModel.gotoMainActivity.observe(this, {
            it?.let { gotoMain ->
                if (gotoMain) {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
        })


        viewModel.startLoginAnimation.observe(this, {
            it?.let { start ->
                if (start) {
                    binding.login.startAnimation()
                } else
                    binding.login.revertAnimation()
            }
        })


        viewModel.startRegisterAnimation.observe(this, {
            it?.let { start ->
                if (start) {
                    binding.register.startAnimation()
                } else
                    binding.register.revertAnimation()
            }
        })


    }


    override fun snackBar(msg: String) {
        Utils(this).showSnackBar(
            color = ContextCompat.getColor(this, R.color.dark_gray),
            msg = msg,
            snackView = binding.root
        ).show()
    }
}