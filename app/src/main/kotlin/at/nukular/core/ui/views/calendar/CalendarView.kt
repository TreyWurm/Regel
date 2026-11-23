package at.nukular.core.ui.views.calendar

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import at.nukular.core.extensions.AppConfig
import at.nukular.core.extensions.isSameMonth
import at.nukular.core.ui.rv.SnapPagerScrollListener
import at.nukular.core.ui.theming.Theme
import at.nukular.core.ui.views.UiComponent
import at.nukular.regel.rv.ItemMonth
import at.nukular.regel.rv.VHUMonth
import dagger.hilt.android.AndroidEntryPoint
import eu.davidea.flexibleadapter.FlexibleAdapter
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject


@AndroidEntryPoint
class CalendarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : RecyclerView(context, attrs),
    UiComponent,
    SnapPagerScrollListener.OnChangeListener {

    // ================================================================================
    // region UiComponent

    @Inject
    override lateinit var theme: Theme

    // Custom View initialization, so we see a preview inside layout editor
    override val isViewInEditMode: Boolean = isInEditMode

    override var applyRipple: Boolean = true

    init {
        if (isInEditMode) {
            AppConfig.onConfigChanged(context, null)
        }
    }
    // endregion {

    var scrollListener: CalendarListener? = null
    private var selectedDays: MutableSet<LocalDate> = mutableSetOf()
    private var estimatedDays: Set<LocalDate> = emptySet()
    private val layoutManagera: LinearLayoutManager = LinearLayoutManager(context, HORIZONTAL, false)
    private val adaptera = FlexibleAdapter<ItemMonth>(
        listOf(
            ItemMonth(VHUMonth(YearMonth.now().minusMonths(2), emptySet())),
            ItemMonth(VHUMonth(YearMonth.now().minusMonths(1), emptySet())),
            ItemMonth(VHUMonth(YearMonth.now(), emptySet())),
            ItemMonth(VHUMonth(YearMonth.now().plusMonths(1), emptySet())),
            ItemMonth(VHUMonth(YearMonth.now().plusMonths(2), emptySet())),
        )
    )

    init {
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(this)
        layoutManager = layoutManagera
        adapter = adaptera
        layoutManager?.scrollToPosition(2)

        addOnScrollListener(SnapPagerScrollListener(snapHelper = snapHelper, type = SnapPagerScrollListener.Type.ON_SETTLED, listener = this, notifyOnInit = true))
        addOnScrollListener(SnapPagerScrollListener(snapHelper = snapHelper, type = SnapPagerScrollListener.Type.ON_SCROLL, listener = this))
    }

    override fun onSnapped(position: Int, type: SnapPagerScrollListener.Type) {
        if (type == SnapPagerScrollListener.Type.ON_SCROLL) {
            val item = adaptera.getItem(position) ?: return
            scrollListener?.monthScrolled(item.vhu.month)
            return
        }


        if (position <= 2) {
            val item = adaptera.getItem(0) ?: return
            val newMonth = item.vhu.month.minusMonths(1)
            post { adaptera.addItem(0, createMonthItem(newMonth)) }
        }

        if (position >= adaptera.itemCount - 2) {
            val item = adaptera.getItem(adaptera.itemCount - 1) ?: return
            val newMonth = item.vhu.month.plusMonths(1)
            post { adaptera.addItem(createMonthItem(newMonth)) }
        }
    }


    /**
     * Replaces all selected and estimated days and rebinds every month whose days changed
     */
    fun setDays(selected: Set<LocalDate>, estimated: Set<LocalDate>) {
        selectedDays = selected.toMutableSet()
        estimatedDays = estimated

        val groupedSelected = selected.groupBy { YearMonth.from(it) }
        val groupedEstimated = estimated.groupBy { YearMonth.from(it) }
        adaptera.currentItems.toList().forEach {
            val newVhu = it.vhu.copy(
                selectedDays = groupedSelected[it.vhu.month]?.toSet() ?: emptySet(),
                estimatedDays = groupedEstimated[it.vhu.month]?.toSet() ?: emptySet(),
            )
            if (newVhu == it.vhu) return@forEach
            adaptera.updateItem(ItemMonth(newVhu))
        }
    }

    private fun createMonthItem(month: YearMonth): ItemMonth = ItemMonth(
        VHUMonth(
            month = month,
            selectedDays = selectedDays.filter { it.isSameMonth(month) }.toSet(),
            estimatedDays = estimatedDays.filter { it.isSameMonth(month) }.toSet(),
        )
    )

    fun addSelectedDay(day: LocalDate) {
        selectedDays.add(day)

        adaptera.currentItems.forEach {
            if (day.isSameMonth(it.vhu.month)) {
                val newItem = ItemMonth(it.vhu.copy(selectedDays = it.vhu.selectedDays + day))
                adaptera.updateItem(newItem)
            }
        }
    }

    fun addSelectedDays(days: Set<LocalDate>) {
        selectedDays.addAll(days)

        val groupedDays = days.groupBy { YearMonth.from(it) }
        adaptera.currentItems.forEach {
            val daysForMonth = groupedDays[it.vhu.month] ?: return@forEach
            val newItem = ItemMonth(it.vhu.copy(selectedDays = it.vhu.selectedDays + daysForMonth))
            adaptera.updateItem(newItem)
        }
    }

    fun clearSelectedDays() {
        selectedDays.clear()

        adaptera.currentItems.forEach {
            val newItem = ItemMonth(it.vhu.copy(selectedDays = emptySet()))
            adaptera.updateItem(newItem)
        }
    }

    fun removeSelectedDay(day: LocalDate) {
        selectedDays.remove(day)

        adaptera.currentItems.forEach {
            if (day.isSameMonth(it.vhu.month)) {
                val newItem = ItemMonth(it.vhu.copy(selectedDays = it.vhu.selectedDays - day))
                adaptera.updateItem(newItem)
            }
        }
    }

    fun removeSelectedDays(days: List<LocalDate>) {
        selectedDays.removeAll(days)
    }

    fun scrollToToday() {
        val today = adaptera.currentItems.indexOfFirst { it.vhu.month == YearMonth.now() }
        if (today == -1) return
        layoutManager?.scrollToPosition(today)
    }
}