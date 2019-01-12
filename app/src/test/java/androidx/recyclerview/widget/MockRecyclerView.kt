package androidx.recyclerview.widget

import bill.catbox.test.forceSet
import io.mockk.every
import io.mockk.mockk

class MockRecyclerView {

    private class MockAdapterDataObservable : RecyclerView.AdapterDataObservable() {
        override fun notifyChanged() = Unit
    }

    companion object {
        fun create(): RecyclerView {
            val view: RecyclerView = mockk(relaxed = true)
            var adapter: RecyclerView.Adapter<out RecyclerView.ViewHolder>? = null

            every { view.adapter } answers { adapter }

            every {
                view.adapter = any()
            } propertyType RecyclerView.Adapter::class answers {
                value.forceSet("mObservable", MockAdapterDataObservable())
                adapter = value
            }

            return view
        }
    }
}
