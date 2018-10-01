package bill.reactive.test

import bill.reactive.HotNotifier

internal class MockNotifier : HotNotifier({}) {

    var cancelled: Boolean = false
        private set

    override fun onCancel() {
        this.cancelled = true
        super.onCancel()
    }
}
