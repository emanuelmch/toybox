package bill.catbox.navigation

import android.content.Context
import bill.catbox.R
import bill.catbox.settings.SettingsActivity

class Navigator(private val context: Context) {

    fun navigateFromMenu(menuId: Int) {
        if (menuId == R.id.actionSettings) {
            SettingsActivity.startActivity(context)
        } else {
            throw IllegalArgumentException("Illegal menuId requested: $menuId")
        }
    }
}
