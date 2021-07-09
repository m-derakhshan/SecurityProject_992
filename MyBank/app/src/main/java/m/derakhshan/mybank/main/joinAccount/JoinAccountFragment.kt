package m.derakhshan.mybank.main.joinAccount

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import m.derakhshan.mybank.R
import m.derakhshan.mybank.databinding.FragmentJoinAccountBinding


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
    }

}