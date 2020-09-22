package com.rittmann.widgets.dialog

import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView

fun TextView?.simpleLinkClick(text: String = "", init: Int, finish: Int, click: () -> Unit) {
    if (this == null) return

    val textToUse = if (text.isEmpty()) this.text else text

    val spannableString = SpannableString(textToUse)

    val clickableSpan: ClickableSpan = object : ClickableSpan() {
        override fun onClick(widget: View) {
            click()
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.isUnderlineText = false
        }
    }

    spannableString.setSpan(
        clickableSpan,
        init,
        finish,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    this.text = spannableString
    movementMethod = LinkMovementMethod.getInstance()
    highlightColor = Color.RED // to see the difference
}