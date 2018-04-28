package bill.catbox.infra

import android.content.Context
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast

// TODO: KTX 0.3 will include a similar function, so replace it when it's released
fun Context.toast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT)
            .apply { show() }
}

// TODO: Should we ask for Context and get text from resources?
fun Int.toOrdinal(): String {
    val suffix = if ((this % 100) in 4..19) {
        "th"
    } else {
        when (this % 10) {
            1 -> "st"
            2 -> "nd"
            3 -> "rd"
            else -> "th"
        }
    }

    return this.toString() + suffix
}

fun ViewGroup.inflateChild(@LayoutRes resource: Int) =
        LayoutInflater.from(context).inflate(resource, this, false)
