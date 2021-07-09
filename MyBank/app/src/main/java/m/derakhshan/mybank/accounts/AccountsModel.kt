package m.derakhshan.mybank.accounts

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AccountsModel(
    val id: String,
    val accountNumber: String,
    val accountBalance: String,
    val accountType: String,
    val lastIncomes: ArrayList<String>,
    val lastOutcome: ArrayList<String>,
    val openDate: String,
    val owners: ArrayList<String>
) : Parcelable