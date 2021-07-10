package m.derakhshan.mybank.accounts.accountDetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import m.derakhshan.mybank.Arrange
import m.derakhshan.mybank.R
import m.derakhshan.mybank.accounts.AccountsModel
import m.derakhshan.mybank.databinding.FragmentAccountDetailsBinding

class AccountDetailsFragment : Fragment() {

    private lateinit var binding: FragmentAccountDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_account_details, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val retrievedData: AccountsModel? = arguments?.getParcelable("info")
        retrievedData?.let { info ->
            binding.accountBalance.text = Arrange().persianConverter(info.accountBalance)
            binding.accountNumber.text = Arrange().persianConverter(info.id)
            val date = info.openDate.split("T")
            binding.accountOpenDate.text = Arrange().persianConverter(date[0])
            binding.accountType.text = getAccountType(info.accountType)

            var owners = ""
            for (i in info.owners) owners = "$owners $i"
            binding.accountOwners.text = owners

            var lastIncome = ""
            for (i in info.lastIncomes)
                lastIncome = if (lastIncome.isEmpty())
                    Arrange().persianConverter(i)
                else
                    "$lastIncome\n${Arrange().persianConverter(i)}"
            binding.lastIncome.text = lastIncome


            var lastOutcome = ""
            for (i in info.lastOutcome)
                lastOutcome = if (lastOutcome.isEmpty())
                    Arrange().persianConverter(i)
                else
                    "$lastOutcome\n${Arrange().persianConverter(i)}"
            binding.lastOutcome.text = lastOutcome

        }
        binding.back.setOnClickListener {
            it.findNavController().navigateUp()
        }

    }


    fun getAccountType(type:String):String{
        return when(type){
            "short-term"->"سپرده پس انداز کوتاه مدت"
            "long-term"->"سپرده پس انداز بلند مدت"
            "current"->"حساب جاری"
            else -> "حساب قرض الحسنه"
        }
    }
}