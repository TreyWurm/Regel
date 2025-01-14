package at.nukular.core.ui.theming

interface ThemeInterface {
    val appTheme: Theme

    /**
     * Interface method to theme implementing item
     *
     *   * [com.citiesapps.cities.activities.ABaseThemedActivity] calls this in [com.citiesapps.cities.__app.ui.screens.ABaseActivity.onPostCreate()]
     *   * [com.citiesapps.cities.adapter.AbstractThemedImageLoaderViewHolder] calls this in the ViewHolder constructor
     *   * [com.citiesapps.cities.fragments.ABaseDialogFragment] calls this in [androidx.appcompat.app.AppCompatDialogFragment.onViewCreated]
     *   * [com.citiesapps.cities.fragments.ABaseFragment] calls this in [androidx.fragment.app.Fragment.onViewCreated]
     */
    fun applyTheme() {}
}