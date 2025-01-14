package at.nukular.core.ui.rv

import android.view.View
import at.nukular.core.ui.theming.Theme
import at.nukular.core.ui.theming.ThemeInterface
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.viewholders.FlexibleViewHolder
import javax.inject.Inject

abstract class ThemedViewHolder<T> : FlexibleViewHolder,
    VHInterface<T>,
    ThemeInterface {

    @Inject
    override lateinit var appTheme: Theme


    constructor(view: View, adapter: FlexibleAdapter<*>) : super(view, adapter) {
        bindView(view)
        initListener()
    }

    constructor(view: View, adapter: FlexibleAdapter<*>, stickyHeader: Boolean) : super(
        view,
        adapter,
        stickyHeader
    ) {
        bindView(view)
        initListener()
    }

    abstract fun bindView(view: View)
    open fun initListener() {}
}
