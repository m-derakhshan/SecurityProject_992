package m.derakhshan.mybank.accounts

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.AuthFailureError
import com.android.volley.DefaultRetryPolicy
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import m.derakhshan.mybank.Address
import m.derakhshan.mybank.R
import m.derakhshan.mybank.Utils
import m.derakhshan.mybank.databinding.FragmentAccountsBinding

class AccountsFragment : Fragment(), AccountClickListener {

    companion object {
        var showLoading = true
    }

    private lateinit var binding: FragmentAccountsBinding
    private val myAdapter = AccountsRecyclerViewAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_accounts, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myAdapter.listener = this

        binding.recyclerView.apply {
            this.layoutManager = LinearLayoutManager(requireContext())
            this.adapter = myAdapter
        }
        binding.refresh.setOnRefreshListener {
            getAccounts()
        }
        getAccounts()
    }

    private fun getAccounts() {
        if (showLoading) {
            binding.refresh.isRefreshing = true
            showLoading = false
        }
        val accounts = ArrayList<AccountsModel>()


        val request = object :
            JsonArrayRequest(
                Method.GET,
                Address().getAccounts,
                null,
                {
                    binding.refresh.isRefreshing = false
                    //-------------------------(server response)-----------------------//
                    for (i in 0 until it.length()) {
                        val info = it.getJSONObject(i)
                        accounts.add(
                            AccountsModel(
                                id = info.getString("id"),
                                accountType = info.getString("account_type"),
                                accountBalance = info.getString("amount"),
                                owners = ArrayList<String>().apply {
                                    this.add(info.getString("username"))
                                },
                                canRead = info.getBoolean("has_read_access"),
                                canWrite = info.getBoolean("has_write_access"),
                                openDate = info.getString("created_at"),
                                lastIncomes = ArrayList<String>().apply {
                                    val tran = info.getJSONArray("transactions_as_reciever")
                                    for(j in 0 until tran.length()){
                                        val tranNumber = tran.getJSONObject(j)
                                        this.add(tranNumber.getString("amount"))
                                    }
                                },
                                lastOutcome = ArrayList<String>().apply {
                                    val tran = info.getJSONArray("transactions_as_sender")
                                    for(j in 0 until tran.length()){
                                        val tranNumber = tran.getJSONObject(j)
                                        this.add(tranNumber.getString("amount"))
                                    }
                                }
                            )
                        )
                    }
                    myAdapter.submitList(accounts)
                },
                {
                    binding.refresh.isRefreshing = false
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


    override fun onClick(account: AccountsModel) {
        if (!account.canRead) {
            Utils(requireContext()).showSnackBar(
                color = ContextCompat.getColor(requireContext(), R.color.black),
                msg = "شما دسترسی خواندن اطلاعات را ندارید",
                snackView = binding.root
            )
            return
        }
        val info = Bundle()
        info.putParcelable("info", account)
        findNavController().navigate(R.id.action_accountsFragment_to_accountDetailsFragment, info)
    }
}