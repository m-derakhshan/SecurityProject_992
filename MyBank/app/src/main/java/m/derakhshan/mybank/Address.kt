package m.derakhshan.mybank

class Address {
    private val base = "https://banker.iran.liara.run"
    val loginAPI = "$base/api/rest-auth/login/"
    val registerAPI = "$base/api/rest-auth/registration/"
    val createAccount = "$base/api/v1/accounts/"
    val getAccounts = "$base/api/v1/accounts/"
    fun getAccountDetail(id:String) = "$base/api/v1/accounts/$id/"
    val joinAccount = "$base/api/v1/accounts/requests/"
    val depositAccount = "$base/api/v1/accounts/transactions/"
    val withdrawAccount = "$base/api/v1/accounts/transactions/"
    fun setStatusJoinAccount(id:String) = "$base/api/v1/accounts/requests/$id/"

}