package m.derakhshan.mybank.messages

data class JoinAccountModel(
    val id: String,
    val username: String,
    val accountNumber: String,
    val status: Boolean
)