package m.derakhshan.mybank.main.deposit

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.android.volley.AuthFailureError
import com.android.volley.DefaultRetryPolicy
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import m.derakhshan.mybank.Address
import m.derakhshan.mybank.R
import m.derakhshan.mybank.Utils
import m.derakhshan.mybank.accounts.AccountsFragment
import m.derakhshan.mybank.accounts.AccountsModel
import m.derakhshan.mybank.databinding.FragmentDepositBinding
import org.json.JSONObject


class DepositFragment : Fragment() {

    private lateinit var binding: FragmentDepositBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_deposit, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.back.setOnClickListener {
            it.findNavController().navigateUp()
        }
        binding.depositMoney.setOnClickListener {
            depositAccount()
        }

    }


    private fun depositAccount() {
        binding.depositMoney.startAnimation()
        val info = JSONObject()
        info.put("transaction_type", "deposit")
        info.put("amount", binding.amount.text.toString())
        info.put("to_account", binding.toAccount.text.toString())

        val request = object :
            JsonObjectRequest(
                Method.POST,
                Address().depositAccount,
                info,
                {
                    binding.depositMoney.revertAnimation()
                    //-------------------------(server response)-----------------------//
                    Utils(requireContext()).showSnackBar(
                        color = ContextCompat.getColor(requireContext(), R.color.black),
                        msg = "عملیات با موفقیت انجام شد",
                        snackView = binding.root
                    ).show()

                },
                {
                    binding.depositMoney.revertAnimation()
                    Utils(requireContext()).showSnackBar(
                        color = ContextCompat.getColor(requireContext(), R.color.black),
                        msg = "خطا در انجام عملیات",
                        snackView = binding.root
                    ).show()
                    try {
                        Log.i(
                            "Log",
                            "Error in depositAccount ${
                                String(
                                    it.networkResponse.data,
                                    Charsets.UTF_8
                                )
                            }"
                        )
                    } catch (e: Exception) {
                        Log.i("Log", "Error in depositAccount $it")
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