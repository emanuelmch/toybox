/*
 * Copyright (c) 2019 Emanuel Machado da Silva <emanuel.mch@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package bill.catbox.home

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import bill.catbox.R
import bill.catbox.infra.*
import bill.reaktive.Publisher
import bill.reaktive.Publishers
import kotlinx.android.synthetic.main.home_item.view.*
import kotlinx.android.synthetic.main.home_view.view.*
import timber.log.Timber
import kotlin.properties.Delegates.observable

// Not sure how I feel about separating this interface...
interface HomeView {
    val menuSelectedEvent: Publisher<Int>
    val boxChosenEvent: Publisher<Int>

    fun startGame(boxCount: Int)
    fun onEmptyBox(attempts: Int)
    fun onCatFound(attempts: Int)
}

class AndroidHomeView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        FrameLayout(context, attrs, defStyleAttr), HomeView {

    init {
        assert(context is ViewController) { "HomeView is in an invalid context" }
        LayoutInflater.from(context).inflate(R.layout.home_view, this, true)
    }

    private val boxAdapter by lazy { BoxAdapter().apply { boxes.adapter = this } }

    override val menuSelectedEvent = context.optionsItemSelected
    override val boxChosenEvent = Publishers.open<Int>()

    override fun startGame(boxCount: Int) {
        Timber.d("Starting the game with $boxCount boxes")
        boxAdapter.boxCount = boxCount
    }

    override fun onEmptyBox(attempts: Int) {
        snackbar(context.getString(R.string.empty_box, attempts.toOrdinal()))
    }

    override fun onCatFound(attempts: Int) {
        snackbar(context.resources.getQuantityString(R.plurals.cat_found, attempts, attempts))
    }

    private inner class BoxAdapter : RecyclerView.Adapter<BoxViewHolder>() {

        var boxCount: Int by observable(0) { _, _, _ ->
            notifyDataSetChanged()
        }

        override fun getItemCount() = boxCount

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
                BoxViewHolder(parent.inflateChild(R.layout.home_item))

        override fun onBindViewHolder(holder: BoxViewHolder, position: Int) {
            holder.bind(position)
        }
    }

    private inner class BoxViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val boxButton: Button = itemView.buttonBox

        fun bind(position: Int) {
            boxButton.text = context.getString(R.string.box_number, position + 1)
            boxButton.setOnClickListener { boxChosenEvent.onNext(position) }
        }
    }
}
