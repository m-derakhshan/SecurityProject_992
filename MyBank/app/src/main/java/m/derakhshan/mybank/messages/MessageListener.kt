package m.derakhshan.mybank.messages

interface MessageListener {
    fun onClickListener(
        accept: Boolean,
        integrity: String?,
        confidentiality: String?,
        req: JoinAccountModel?
    )
}