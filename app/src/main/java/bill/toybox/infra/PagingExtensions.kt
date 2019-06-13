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

package bill.toybox.infra

import android.os.Handler
import android.os.Looper
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class PagedListBuilder<Key, Value>() {

    private var dataSource: DataSource<Key, Value>? = null
    private var pageSize = -1

    fun setDataSource(dataSource: DataSource<Key, Value>): PagedListBuilder<Key, Value> {
        this.dataSource = dataSource
        return this
    }

    fun setPageSize(pageSize: Int): PagedListBuilder<Key, Value> {
        this.pageSize = pageSize
        return this
    }

    fun build(): PagedList<Value> {
        val dataSource = dataSource ?: throw IllegalStateException("dataSource can't be null")

        val config = PagedList.Config.Builder()
            .setPageSize(pageSize)
            .build()

        val fetchExecutor = Executors.newSingleThreadExecutor()

        val handler = Handler(Looper.getMainLooper())
        val notifyExecutor = Executor { handler.post(it) }

        return PagedList(dataSource, config, notifyExecutor, fetchExecutor)
    }
}

// TODO: This really should be its own DataSource, not a specialization of PageKeyedDataSource
class SingleFetchDataSource<T>(private val fetch: (Int) -> T) : PageKeyedDataSource<Int, T>() {
    private val io = Executors.newSingleThreadExecutor()

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, T>) {
        //TODO: We shouldn't have to be using io here
        io.submit {
            val items = loadRange(0, params.requestedLoadSize)
            callback.onResult(items, null, items.size)
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, T>) {
        //TODO: We shouldn't have to be using io here
        io.submit {
            val start = params.key
            val items = loadRange(start, params.requestedLoadSize)
            callback.onResult(items, start + items.size)
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, T>) {
        throw IllegalStateException("Should never reach here")
    }

    private fun loadRange(start: Int, count: Int) =
        (start until start + count).map(fetch)
}
