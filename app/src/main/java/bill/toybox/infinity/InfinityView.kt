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
import bill.toybox.infinity.cats.Cat
import bill.toybox.infra.inflateChild
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.infinity_item.view.*

class InfinityView(val cats: RecyclerView) {

    fun showCats(pagedList: PagedList<Cat>, diffCallback: DiffUtil.ItemCallback<Cat>) {
        val adapter = InfiniteCatsAdapter(diffCallback)

        cats.adapter = adapter
        adapter.submitList(pagedList)
    }
}


private class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val catImage: ImageView = itemView.catImage
}

private class InfiniteCatsAdapter(diffCallback: DiffUtil.ItemCallback<Cat>) :
    PagedListAdapter<Cat, ViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(parent.inflateChild(R.layout.infinity_item))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Picasso.get()
            .load(getItem(position)!!.url)
            .fit()
            .centerCrop()
            .into(holder.catImage)
    }
}
