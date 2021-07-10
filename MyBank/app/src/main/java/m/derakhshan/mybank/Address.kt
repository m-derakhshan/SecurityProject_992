package m.derakhshan.mybank

class Address {
    private val base = "https://banker.iran.liara.run"
    val loginAPI = "$base/api/rest-auth/login/"
    val registerAPI = "$base/api/rest-auth/registration/"
    val createAccount = "$base/api/v1/accounts/"
    val getAccounts = "$base/api/v1/accounts/"
    val joinAccount = "$base/api/v1/accounts/requests/"
    fun setStatusJoinAccount(id:String) = "$base/api/v1/accounts/requests/$id/"

}