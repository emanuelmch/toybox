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

import androidx.recyclerview.widget.DiffUtil
import bill.toybox.infinity.cats.Cat
import bill.toybox.infinity.cats.CatRepository
import bill.toybox.infra.ObservableActivity
import bill.toybox.infra.PagedListBuilder
import bill.toybox.infra.SingleFetchDataSource

class InfinityPresenter(val view: InfinityView) {

    private val cats = CatRepository()

    fun observe(activity: ObservableActivity) {
        //TODO: This should be doOnCreate
        activity.doOnResume {
            val list = PagedListBuilder<Int, Cat>()
                .setDataSource(SingleFetchDataSource(cats::get))
                .setPageSize(4)
                .build()

            view.showCats(list, CatItemCallback)
        }
    }
}

private object CatItemCallback : DiffUtil.ItemCallback<Cat>() {
    override fun areItemsTheSame(oldItem: Cat, newItem: Cat) = oldItem.url == newItem.url
    override fun areContentsTheSame(oldItem: Cat, newItem: Cat) = oldItem == newItem
}
