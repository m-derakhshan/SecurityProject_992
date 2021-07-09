package m.derakhshan.mybank.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import m.derakhshan.mybank.R
import m.derakhshan.mybank.databinding.FragmentMainBinding


class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //-------------------------(adding new account)-----------------------//
        binding.addAccount.setOnClickListener {
            it.findNavController().navigate(R.id.action_mainFragment_to_addAccount)
        }
        //-------------------------(join to an account)-----------------------//
        binding.joinAccount.setOnClickListener {
            it.findNavController().navigate(R.id.action_mainFragment_to_joinAccountFragment)
        }
        //-------------------------(withdraw money)-----------------------//
        binding.depositMoney.setOnClickListener {
            it.findNavController().navigate(R.id.action_mainFragment_to_depositFragment)
        }
        //-------------------------(get money)-----------------------//
        binding.getMoney.setOnClickListener {
            it.findNavController().navigate(R.id.action_mainFragment_to_withdrawFragment)
        }

    }

}