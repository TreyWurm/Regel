package at.nukular.core.ui.listeners.toolbar

import androidx.appcompat.app.AppCompatActivity

class DefaultActivityToolbarListener(val activity: AppCompatActivity) : ToolbarListener {
    override fun onNavigationBackClicked() {
        activity.onBackPressedDispatcher.onBackPressed()
    }

    override fun onActionClicked(id: Int) {
    }
}