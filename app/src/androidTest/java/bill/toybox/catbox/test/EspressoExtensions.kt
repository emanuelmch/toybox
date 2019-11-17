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

package bill.toybox.catbox.test

import android.view.View
import androidx.annotation.StringRes
import androidx.test.espresso.*
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.util.TreeIterables
import androidx.test.platform.app.InstrumentationRegistry
import org.hamcrest.Matcher
import java.lang.Thread.sleep

fun withText(@StringRes resId: Int, vararg formatArgs: Any): Matcher<View> {
    val context = InstrumentationRegistry.getInstrumentation().targetContext
    val text = context.getString(resId, *formatArgs)
    return ViewMatchers.withText(text)
}

fun withQuantityText(@StringRes resId: Int, vararg formatArgs: Int): Matcher<View> {
    val context = InstrumentationRegistry.getInstrumentation().targetContext
    val text = context.resources.getQuantityString(resId, formatArgs[0], *formatArgs.toTypedArray())
    return ViewMatchers.withText(text)
}

fun awaitDisappear(): ViewAction = AwaitDisappearViewAction()

private class AwaitDisappearViewAction : ViewAction {
    override fun getDescription() = "await until disappear"

    override fun getConstraints(): Matcher<View> = ViewMatchers.isDisplayed()

    // FIXME: Should have a timeout
    override fun perform(uiController: UiController, view: View) {
        val rootView = view.rootView
        val isDisplayedMatcher = ViewMatchers.isDisplayed()
        while (view.isDescendantOf(rootView) && isDisplayedMatcher.matches(view)) {
            uiController.loopMainThreadForAtLeast(200)
        }
    }

    private fun View.isDescendantOf(rootView: View): Boolean {
        if (this == rootView) return true

        var view = this
        while (view.parent is View) {
            if (view.parent == rootView) return true
            view = view.parent as View
        }

        return false
    }
}

// FIXME: Should have a timeout
fun waitForView(viewMatcher: Matcher<View>): ViewInteraction {
    while (true) {
        try {
            onView(isRoot()).perform(LookForViewViewAction(viewMatcher))
            return onView(viewMatcher)
        } catch (e: Exception) {
            sleep(250)
        }
    }
}

private class LookForViewViewAction(val viewMatcher: Matcher<View>) : ViewAction {

    override fun getDescription() = "Looking for $viewMatcher in the root view"

    override fun getConstraints(): Matcher<View>? = isRoot()

    override fun perform(uiController: UiController, view: View) {
        if (TreeIterables.breadthFirstViewTraversal(view).none(viewMatcher::matches)) {
            throw NoMatchingViewException.Builder()
                    .withRootView(view)
                    .withViewMatcher(viewMatcher)
                    .build()
        }
    }
}
