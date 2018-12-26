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

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import bill.catbox.R
import bill.catbox.infra.*
import bill.reaktive.Publishers
import kotlinx.android.synthetic.main.home_item.view.*
import kotlinx.android.synthetic.main.home_view.view.*
import timber.log.Timber
import kotlin.properties.Delegates.observable

class HomeView(private val rootView: ViewGroup) {

    private val context = rootView.context
    private val boxAdapter = BoxAdapter().also { rootView.boxes.adapter = it }

    val boxChosenEvent = boxAdapter.boxClickedEvent

    fun startGame(boxCount: Int) {
        Timber.d("Starting the game with $boxCount boxes")
        boxAdapter.boxCount = boxCount
    }

    fun onEmptyBox(attempts: Int) {
        rootView.snackbar(context.getString(R.string.empty_box, attempts.toOrdinal()))
    }

    fun onCatFound(attempts: Int) {
        rootView.snackbar(context.resources.getQuantityString(R.plurals.cat_found, attempts, attempts))
    }
}

private class BoxAdapter : RecyclerView.Adapter<BoxViewHolder>() {

    val boxClickedEvent = Publishers.open<Int>()

    var boxCount: Int by observable(0) { _, _, _ ->
        notifyDataSetChanged()
    }

    override fun getItemCount() = boxCount

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            BoxViewHolder(parent.inflateChild(R.layout.home_item), boxClickedEvent::onNext)

    override fun onBindViewHolder(holder: BoxViewHolder, position: Int) =
            holder.bind(position)
}

private class BoxViewHolder(itemView: View,
                            private val boxChosenEvent: (Int) -> Unit) : RecyclerView.ViewHolder(itemView) {

    fun bind(position: Int) {
        itemView.boxButton.run {
            text = itemView.context.getString(R.string.box_number, position + 1)
            setOnClickListener { boxChosenEvent(position) }
        }
    }
}
