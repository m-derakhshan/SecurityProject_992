package m.derakhshan.mybank.main.joinAccount

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
import m.derakhshan.mybank.databinding.FragmentJoinAccountBinding
import org.json.JSONObject


class JoinAccountFragment : Fragment() {

    private lateinit var binding: FragmentJoinAccountBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_join_account, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //-------------------------(on back click listener)-----------------------//
        binding.back.setOnClickListener {
            it.findNavController().navigateUp()
        }
        binding.joinAccount.setOnClickListener {
            joinAccount()
        }
    }


    private fun joinAccount() {

        val info = JSONObject()
        info.put("requested_account", binding.accountNumber.text.toString())
        binding.joinAccount.startAnimation()

        val request = object :
            JsonObjectRequest(
                Method.POST,
                Address().joinAccount,
                info,
                {
                    //-------------------------(server response)-----------------------//
                    Log.i("Log", "response is $it")
                    binding.joinAccount.revertAnimation()
                    Utils(requireContext()).showSnackBar(
                        color = ContextCompat.getColor(
                            requireContext(),
                            R.color.black
                        ),
                        msg = "درخواست شما با موفقیت ایجاد شد.",
                        snackView = binding.root
                    ).show()
                },
                {
                    binding.joinAccount.revertAnimation()
                    try {
                        Utils(requireContext()).showSnackBar(
                            color = ContextCompat.getColor(
                                requireContext(),
                                R.color.black
                            ),
                            msg = "خطا در اشتراک به حساب",
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