package com.diego.rxmovies.utils

import android.content.Context
import com.diego.rxmovies.R

sealed class UIText {
    object NoConnect : UIText()
    object UnknownError : UIText()
    data class MessageException(val errorMessage: String) : UIText()
}

fun Context.getMyUIText(uiText: UIText): String {
    return when (uiText) {
        is UIText.NoConnect -> getString(R.string.no_connect_message)
        is UIText.UnknownError -> getString(R.string.error_result_message_unknown_retry)
        is UIText.MessageException -> String.format(
            getString(R.string.error_result_message_retry),
            uiText.errorMessage
        )
    }
}