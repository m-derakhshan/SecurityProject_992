package m.derakhshan.mybank.main.withdraw

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
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import m.derakhshan.mybank.Address
import m.derakhshan.mybank.R
import m.derakhshan.mybank.Utils
import m.derakhshan.mybank.databinding.FragmentWithdrawBinding
import org.json.JSONObject


class WithdrawFragment : Fragment() {

    private lateinit var binding: FragmentWithdrawBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_withdraw, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.back.setOnClickListener {
            it.findNavController().navigateUp()
        }
        binding.withdrawMoney.setOnClickListener {
            withdrawAccount()
        }
    }

    private fun withdrawAccount() {
        binding.withdrawMoney.startAnimation()
        val info = JSONObject()
        info.put("transaction_type", "withdraw")
        info.put("amount", binding.amount.text.toString())
        info.put("from_account", binding.fromAccount.text.toString())
        info.put("to_account", binding.toAccount.text.toString())

        val request = object :
            JsonObjectRequest(
                Method.POST,
                Address().withdrawAccount,
                info,
                {
                    binding.withdrawMoney.revertAnimation()
                    //-------------------------(server response)-----------------------//
                    Utils(requireContext()).showSnackBar(
                        color = ContextCompat.getColor(requireContext(), R.color.black),
                        msg = "عملیات با موفقیت انجام شد",
                        snackView = binding.root
                    ).show()

                },
                {
                    binding.withdrawMoney.revertAnimation()
                    Utils(requireContext()).showSnackBar(
                        color = ContextCompat.getColor(requireContext(), R.color.black),
                        msg = "خطا در انجام عملیات",
                        snackView = binding.root
                    ).show()
                    try {
                        Log.i(
                            "Log",
                            "Error in withdrawAccount ${
                                String(
                                    it.networkResponse.data,
                                    Charsets.UTF_8
                                )
                            }"
                        )
                    } catch (e: Exception) {
                        Log.i("Log", "Error in withdrawAccount $it")
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