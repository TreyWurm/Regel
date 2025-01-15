package at.nukular.core.ui.views.calendar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import at.nukular.core.extensions.AppConfig
import at.nukular.core.extensions.CURRENT_LOCALE
import at.nukular.core.extensions.StaticLayout
import at.nukular.core.extensions.dpAsPx
import at.nukular.core.extensions.dpAsPxFloat
import at.nukular.core.extensions.drawTranslated
import at.nukular.core.extensions.isSameMonth
import at.nukular.core.extensions.measureDimension
import at.nukular.core.extensions.spAsPxFloat
import at.nukular.core.extensions.weekOfMonth
import at.nukular.core.ui.theming.Theme
import at.nukular.core.ui.views.UiComponent
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import javax.inject.Inject
import kotlin.math.max
import kotlin.math.min


@AndroidEntryPoint
class MonthView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : View(context, attrs),
    UiComponent {

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

    private val paintWeekDays = TextPaint().apply {
        flags = Paint.ANTI_ALIAS_FLAG
        textSize = 16.spAsPxFloat
        typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
        color = colorTextIntense
    }
    private val paintDays = TextPaint().apply {
        flags = Paint.ANTI_ALIAS_FLAG
        textSize = 16.spAsPxFloat
        color = colorTextIntense
    }

    private val paintDivider = Paint().apply {
        flags = Paint.ANTI_ALIAS_FLAG
        color = colorDivider
    }

    private val paintToday = Paint().apply {
        flags = Paint.ANTI_ALIAS_FLAG
        color = Theme.alpha(Color.BLUE, 0x50)
    }

    private val paintPeriod = Paint().apply {
        flags = Paint.ANTI_ALIAS_FLAG
        color = Theme.alpha(Color.RED, 0x50)
    }

    private val cellsPerRow = 7
    private var cellWidth = 0
    private var cellHeight = 0
    private var horizontalOffsetsWeekDays = FloatArray(cellsPerRow)
    private var horizontalOffsetsDays = FloatArray(31)
    private var staticLayoutsWeekDays = Array(cellsPerRow) {
        val text = DayOfWeek.of(it + 1).getDisplayName(TextStyle.SHORT_STANDALONE, CURRENT_LOCALE)
        val textWidth = paintWeekDays.measureText(text)
        StaticLayout(text, paintWeekDays, textWidth.toInt())
    }
    private var staticLayoutsDays = Array(31) {
        val text = "${it + 1}"
        val textWidth = paintDays.measureText(text)
        StaticLayout(text, paintDays, textWidth.toInt())
    }
    private val spacingSelectedDay = 8.dpAsPx

    private var firstDayOfWeekOfMonth: Int = 1
    var month: YearMonth? = YearMonth.now()
        set(value) {
            field = value
            updateOffsetsDay()
        }

    private var selectedDays: MutableSet<LocalDate> = mutableSetOf()

    fun addSelectedDay(day: LocalDate) {
        selectedDays.add(day)
        invalidate()
    }

    fun addSelectedDays(days: Set<LocalDate>) {
        selectedDays.addAll(days)
        invalidate()
    }

    fun clearSelectedDays() {
        selectedDays.clear()
        invalidate()
    }

    fun removeSelectedDay(day: LocalDate) {
        selectedDays.remove(day)
        invalidate()
    }

    fun removeSelectedDays(days: List<LocalDate>) {
        selectedDays.removeAll(days)
        invalidate()
    }

    fun updateOffsetsDay() {
        horizontalOffsetsDays = FloatArray(31) {
            firstDayOfWeekOfMonth = month?.atDay(1)?.dayOfWeek?.value ?: 1
            (it - 1 + firstDayOfWeekOfMonth) % 7 * cellWidth.toFloat() + ((cellWidth - staticLayoutsDays[it].width) / 2f)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        cellWidth = w / cellsPerRow
        cellHeight = (h - staticLayoutsWeekDays[0].height - 16.dpAsPx) / 6
        horizontalOffsetsWeekDays = FloatArray(cellsPerRow) {
            it * cellWidth.toFloat() + ((cellWidth - staticLayoutsWeekDays[it].width) / 2f)
        }

        if (cellWidth < staticLayoutsWeekDays.maxOf { it.width }) {
            Timber.w("onSizeChanged: CellWidth not sufficient for StaticLayout")
        }
        updateOffsetsDay()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val specMode = MeasureSpec.getMode(widthMeasureSpec)
        val specSize = MeasureSpec.getSize(widthMeasureSpec)

        val desiredWidth: Int = max(staticLayoutsWeekDays.sumOf { it.width }, staticLayoutsDays.takeLast(7).sumOf { it.width })
        val width = measureDimension(desiredWidth, widthMeasureSpec)

        val textHeightWeekDay = staticLayoutsWeekDays.maxOf { it.height }
        val textHeightDay = staticLayoutsDays.maxOf { it.height }
        val desiredHeight = textHeightWeekDay + (textHeightDay + 2 * spacingSelectedDay) * 6 + 16.dpAsPx
        val height = measureDimension(desiredHeight, heightMeasureSpec)

        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        staticLayoutsWeekDays.forEachIndexed { index, it ->
            it.drawTranslated(canvas, translationX = horizontalOffsetsWeekDays[index], translationY = 8.dpAsPxFloat)

            if (index == 0) return@forEachIndexed
            canvas.drawLine(index * cellWidth.toFloat(), 0f, index * cellWidth.toFloat(), height.toFloat(), paintDivider)
        }
//        canvas.drawLine(0f, staticLayoutsWeekDays[0].height + 16.dpAsPxFloat, width.toFloat(), staticLayoutsWeekDays[0].height + 16.dpAsPxFloat, paintDivider)


        canvas.translate(0f, staticLayoutsWeekDays[0].height + 16.dpAsPxFloat)
        val month = month ?: return
        staticLayoutsDays.take(month.lengthOfMonth()).forEachIndexed { index, it ->
            it.drawTranslated(
                canvas,
                translationX = horizontalOffsetsDays[index],
                translationY = (index - 1 + firstDayOfWeekOfMonth) / 7 * cellHeight +
                        (cellHeight.toFloat() - staticLayoutsDays[0].height) / 2
            )
        }

        for (i in 0..5) {
            canvas.drawLine(0f, cellHeight * i.toFloat(), width.toFloat(), cellHeight * i.toFloat(), paintDivider)
        }


        val today = LocalDate.now()
        if (today.isSameMonth(month)) {
            val minSide = min(cellWidth, cellHeight)
            val radius = (minSide - 8.dpAsPx) / 2f
            canvas.drawCircle(
                (today.dayOfWeek.value - 1) % 7f * cellWidth + cellWidth / 2f,
                (today.weekOfMonth() - 1) * cellHeight + cellHeight / 2.toFloat(),
                radius,
                paintToday
            )
        }

        selectedDays.forEach {
            val today = it
            if (today.isSameMonth(month)) {
                val minSide = min(cellWidth, cellHeight)
                val radius = (minSide - 8.dpAsPx) / 2f
                canvas.drawCircle(
                    (today.dayOfWeek.value - 1) % 7f * cellWidth + cellWidth / 2f,
                    (today.weekOfMonth() - 1) * cellHeight + cellHeight / 2.toFloat(),
                    radius,
                    paintPeriod
                )
            }
        }
    }
}