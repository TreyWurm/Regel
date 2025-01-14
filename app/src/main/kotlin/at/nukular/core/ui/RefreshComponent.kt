package at.nukular.core.ui

interface RefreshComponent {
    /**
     * Flag indicating if the implementing component should do a reload of its data
     */
    var doRefreshData: Boolean

    /**
     * Function that gets called, when the data of the component should be refreshed in the background
     *
     * Called during
     * * [BaseActivityImpl.onResume] when entering onResume every time except for the 1st time
     */
    fun refreshData() {}
}