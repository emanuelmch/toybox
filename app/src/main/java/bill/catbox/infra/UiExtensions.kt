package bill.catbox.infra

import android.content.Context
import android.widget.Toast

// TODO: KTX 0.3 will include a similar function, so replace it when it's released
fun Context.toast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT)
            .apply { show() }
}
