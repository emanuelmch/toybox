package bill.catbox.home.counter

import android.util.Log
import android.widget.TextView
import bill.catbox.infra.toInt
import timber.log.Timber
import java.lang.Exception

class AttemptCounterView(private val counter: TextView) {

    var count: Int
        get() = counter.text?.toInt() ?: 0
        set(value) {
            Timber.d(value.toString())
            try {
                counter.text = value.toString()
            } catch (e: Throwable) {
                Timber.e(e, "THE FORK!")
            }
        }

}
