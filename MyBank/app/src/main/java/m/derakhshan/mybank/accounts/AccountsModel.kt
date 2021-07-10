package m.derakhshan.mybank.accounts

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class AccountsModel(
    val id: String,
    val accountType: String,
    val accountBalance: String,
    val owners: ArrayList<String>,
    val canRead: Boolean,
    val canWrite: Boolean,
    val openDate: String,
    val lastIncomes: ArrayList<String>,
    val lastOutcome: ArrayList<String>
) : Parcelable
