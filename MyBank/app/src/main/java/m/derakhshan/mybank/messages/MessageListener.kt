package m.derakhshan.mybank.messages

import java.text.FieldPosition

interface MessageListener {
    fun onClickListener(
        position: Int,
        accept: Boolean,
        integrity: String?,
        confidentiality: String?,
        req: JoinAccountModel
    )
}