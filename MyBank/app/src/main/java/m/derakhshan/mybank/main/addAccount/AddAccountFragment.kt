package m.derakhshan.mybank.main.addAccount

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.android.volley.AuthFailureError
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import m.derakhshan.mybank.Address
import m.derakhshan.mybank.R
import m.derakhshan.mybank.Utils
import m.derakhshan.mybank.databinding.FragmentAddAccountBinding
import org.json.JSONObject


class AddAccount : Fragment() {

    private lateinit var binding: FragmentAddAccountBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_account, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //-------------------------(set back function)-----------------------//
        binding.back.setOnClickListener {
            it.findNavController().navigateUp()
        }

        //-------------------------(setting up account type)-----------------------//
        val accountTypeItems = ArrayList<String>()
        accountTypeItems.add("سپرده پس انداز کوتاه مدت")
        accountTypeItems.add("سپرده پس انداز بلند مدت")
        accountTypeItems.add("حساب جاری")
        accountTypeItems.add("حساب قرض الحسنه")
        val accountTypeOptions =
            ArrayAdapter(
                requireContext(),
                R.layout.support_simple_spinner_dropdown_item,
                accountTypeItems
            )
        binding.accountType.adapter = accountTypeOptions

        //-------------------------(setting up the confidentiality type)-----------------------//
        val confidentialityTypeItems = ArrayList<String>()
        confidentialityTypeItems.add("Unclassified")
        confidentialityTypeItems.add("Confidential")
        confidentialityTypeItems.add("Secret")
        confidentialityTypeItems.add("TopSecret")
        val confidentialityTypeOptions =
            ArrayAdapter(
                requireContext(),
                R.layout.support_simple_spinner_dropdown_item,
                confidentialityTypeItems
            )
        binding.confidentialityType.adapter = confidentialityTypeOptions

        //-------------------------(setting up the integrity type)-----------------------//
        val integrityTypeItems = ArrayList<String>()
        integrityTypeItems.add("Untrusted")
        integrityTypeItems.add("SlightlyTrusted")
        integrityTypeItems.add("Trusted")
        integrityTypeItems.add("VeryTrusted")
        val integrityTypeOptions =
            ArrayAdapter(
                requireContext(),
                R.layout.support_simple_spinner_dropdown_item,
                integrityTypeItems
            )
        binding.integrityType.adapter = integrityTypeOptions

        //-------------------------(adding listener for creating new account btn)-----------------------//
        binding.addAccount.setOnClickListener {
            createNewAccount()
        }
    }


    private fun getAccountTypeID(id: Long): String {
        return when (id) {
            1L -> "short-term"
            2L -> "long-term"
            3L -> "current"
            else -> "gharz"
        }
    }


    private fun createNewAccount() {

        val info = JSONObject()
        info.put("account_type", getAccountTypeID(binding.accountType.selectedItemId + 1))
        info.put("conf_label", binding.confidentialityType.selectedItemId + 1)
        info.put("integrity_label", binding.integrityType.selectedItemId + 1)
        info.put("amount", binding.balance.text.toString())
        info.put("user", "")

        binding.addAccount.startAnimation()
        Log.i("Log", "info for creating account is $info")
        val request = object :
            JsonObjectRequest(
                Method.POST,
                Address().createAccount,
                info,
                {
                    //-------------------------(server response)-----------------------//
                    Log.i("Log", "response is $it")
                    binding.addAccount.revertAnimation()
                    Utils(requireContext()).showSnackBar(
                        color = ContextCompat.getColor(
                            requireContext(),
                            R.color.black
                        ),
                        msg = "حساب با موفقیت ایجاد شد",
                        snackView = binding.root
                    ).show()
                },
                {
                    binding.addAccount.revertAnimation()
                    try {
                        Utils(requireContext()).showSnackBar(
                            color = ContextCompat.getColor(
                                requireContext(),
                                R.color.black
                            ),
                            msg = "خطا در ایجاد حساب جدید",
                            snackView = binding.root
                        ).show()

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
                }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["Authorization"] = "Bearer ${Utils(context = requireContext()).accessToken}"

                return params
            }
        }
        request.retryPolicy = DefaultRetryPolicy(
            30000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        Volley.newRequestQueue(requireContext()).add(request)
    }
}