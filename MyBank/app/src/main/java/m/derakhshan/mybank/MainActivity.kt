package m.derakhshan.mybank

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import m.derakhshan.mybank.databinding.ActivityMainBinding
import java.security.SecureRandom

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val iv = ByteArray(16)

    init {
        SecureRandom().nextBytes(iv)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        if (savedInstanceState == null)
            setNavigation()

        val cryptography = Cryptography()
        val encrypt = cryptography.encrypt(
            plaintext = "Mohammad Derakhshan Talkhouncheh taghdim mikonad man midonam".toByteArray(),
            key = cryptography.getSecretKey("1234567812345678"),
            IV = iv
        )

        val decrypt = cryptography.decrypt(
            cipherText = encrypt,
            key = cryptography.getSecretKey("1234567813245678"),
            IV = iv
        )



    }


    private fun setNavigation() {
        val graphID = listOf(
            R.navigation.home,
            R.navigation.messages,
            R.navigation.accounts
        )
        binding.bottomMenu.setupWithNavController(
            navGraphIds = graphID,
            fragmentManager = supportFragmentManager,
            containerId = binding.container.id, intent = intent
        )
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        setNavigation()
    }
}