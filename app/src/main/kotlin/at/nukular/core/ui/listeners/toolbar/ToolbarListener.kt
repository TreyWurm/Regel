package at.nukular.core.ui.listeners.toolbar

interface ToolbarListener {
    fun onNavigationBackClicked()
    fun onActionClicked(id: Int)
}