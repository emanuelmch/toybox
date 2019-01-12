package bill.catbox.test

fun <T> deferring(function: ((() -> Unit) -> Unit) -> T): T {
    val actions = mutableListOf<() -> Unit>()

    try {
        return function(actions::plusAssign)
    } finally {
        actions.reversed().forEach(Function0<Unit>::invoke)
    }
}
