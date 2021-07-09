package m.derakhshan.mybank.messages

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import m.derakhshan.mybank.R
import m.derakhshan.mybank.databinding.JoinRequestItemModelBinding


class MessageRecyclerViewAdapter :
    ListAdapter<JoinAccountModel, MessageRecyclerViewAdapter.ViewHolder>(object :
        DiffUtil.ItemCallback<JoinAccountModel>() {
        override fun areItemsTheSame(
            oldItem: JoinAccountModel,
            newItem: JoinAccountModel
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: JoinAccountModel,
            newItem: JoinAccountModel
        ): Boolean {
            return oldItem.accountNumber == newItem.accountNumber &&
                    oldItem.username == newItem.username
        }

    }) {

    lateinit var listener: MessageListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<JoinRequestItemModelBinding>(
            inflater,
            R.layout.join_request_item_model, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position))


    inner class ViewHolder(val view: JoinRequestItemModelBinding) :
        RecyclerView.ViewHolder(view.root) {
        fun bind(model: JoinAccountModel) {
            view.applicant.text = model.username
            view.joinAccountNumber.text = model.accountNumber

            //-------------------------(setting up the confidentiality type)-----------------------//
            val confidentialityTypeItems = ArrayList<String>()
            confidentialityTypeItems.add("TopSecret")
            confidentialityTypeItems.add("Secret")
            confidentialityTypeItems.add("Confidential")
            confidentialityTypeItems.add("Unclassified")
            val confidentialityTypeOptions =
                ArrayAdapter(
                    itemView.context,
                    R.layout.support_simple_spinner_dropdown_item,
                    confidentialityTypeItems
                )
            view.confidentialityType.adapter = confidentialityTypeOptions

            //-------------------------(setting up the integrity type)-----------------------//
            val integrityTypeItems = ArrayList<String>()
            integrityTypeItems.add("VeryTrusted")
            integrityTypeItems.add("Trusted")
            integrityTypeItems.add("SlightlyTrusted")
            integrityTypeItems.add("Untrusted")
            val integrityTypeOptions =
                ArrayAdapter(
                    itemView.context,
                    R.layout.support_simple_spinner_dropdown_item,
                    integrityTypeItems
                )
            view.integrityType.adapter = integrityTypeOptions


            //-------------------------(adding accept listener)-----------------------//
            view.accept.setOnClickListener {
                listener.onClickListener(
                    accept = true,
                    integrity = view.integrityType.selectedItem.toString(),
                    confidentiality = view.confidentialityType.selectedItem.toString(),
                    req = model
                )
            }
            //-------------------------(adding reject listener)-----------------------//
            view.reject.setOnClickListener {
                listener.onClickListener(
                    accept = false,
                    integrity = null,
                    confidentiality = null,
                    req = null
                )
            }

        }
    }
}