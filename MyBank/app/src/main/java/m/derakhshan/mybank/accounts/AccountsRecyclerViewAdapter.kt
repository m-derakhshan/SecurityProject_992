package m.derakhshan.mybank.accounts

import android.view.LayoutInflater

import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import m.derakhshan.mybank.Arrange
import m.derakhshan.mybank.R
import m.derakhshan.mybank.databinding.AccountItemModelBinding


class AccountsRecyclerViewAdapter :
    ListAdapter<AccountsModel, AccountsRecyclerViewAdapter.ViewHolder>(object :
        DiffUtil.ItemCallback<AccountsModel>() {
        override fun areItemsTheSame(oldItem: AccountsModel, newItem: AccountsModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: AccountsModel, newItem: AccountsModel): Boolean {
            return (oldItem.accountBalance == newItem.accountBalance &&
                    oldItem.canRead == newItem.canRead)
        }

    }) {

    lateinit var listener: AccountClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<AccountItemModelBinding>(
            inflater,
            R.layout.account_item_model,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    inner class ViewHolder(private val binding: AccountItemModelBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: AccountsModel) {
            binding.root.setOnClickListener {
                listener.onClick(model)
            }
            binding.mainView.alpha = if (model.canRead) 1F else 0.5F
            binding.accountBalance.text = Arrange().persianConverter(model.accountBalance)
            binding.accountNumber.text = Arrange().persianConverter(model.id)
        }

    }
}



