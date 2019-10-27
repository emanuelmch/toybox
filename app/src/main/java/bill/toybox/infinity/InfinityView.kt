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

package bill.toybox.infinity

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import bill.toybox.R
import bill.toybox.infinity.cats.CatPromise
import bill.toybox.infra.inflateChild
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.infinity_item.view.catImage
import timber.log.Timber

class InfinityView(private val cats: RecyclerView) {

    fun showCats(pagedList: PagedList<CatPromise>, diffCallback: DiffUtil.ItemCallback<CatPromise>) {
        val adapter = InfiniteCatsAdapter(diffCallback)

        cats.adapter = adapter
        adapter.submitList(pagedList)
    }
}

private class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val catImage: ImageView = itemView.catImage
}

private class InfiniteCatsAdapter(diffCallback: DiffUtil.ItemCallback<CatPromise>) :
    PagedListAdapter<CatPromise, ViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(parent.inflateChild(R.layout.infinity_item))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Timber.d("Binding cat")
        val item = getItem(position)
        Timber.d("Item is not null, right? ${item != null}")
        item!!.cat
            .signalOnForeground()
            .doOnNext { cat ->
                Glide.with(holder.catImage)
                    .load(cat.url)
                    .centerCrop()
                    .into(holder.catImage)
            }
            .doOnCancel { Timber.d("GOT CANCELLED") }
            .subscribe()
    }
}
