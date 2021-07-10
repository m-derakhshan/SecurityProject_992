package m.derakhshan.mybank.accounts

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import m.derakhshan.mybank.R
import m.derakhshan.mybank.databinding.FragmentAccountsBinding

class AccountsFragment : Fragment(), AccountClickListener {

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
        binding.refresh.isRefreshing = false
        val accounts = ArrayList<AccountsModel>()
        accounts.add(AccountsModel(
            id = "1",
            accountNumber = "123",
            accountBalance = "25000",
            accountType = "قرض الحسنه",
            lastIncomes = ArrayList<String>().apply {
                this.add("5000")
                this.add("6000")
                this.add("7000")
                this.add("8000")
                this.add("9000")
            },
            lastOutcome = ArrayList<String>().apply {
                this.add("5000")
                this.add("6000")
                this.add("7000")
                this.add("8000")
                this.add("9000")
            },
            openDate = "1400-01-01",
            owners = ArrayList<String>().apply {
                this.add("Mohammad")
            }
        ))
        myAdapter.submitList(accounts)
    }


    override fun onClick(account: AccountsModel) {
        val info = Bundle()
        info.putParcelable("info", account)
        findNavController().navigate(R.id.action_accountsFragment_to_accountDetailsFragment, info)
    }
}