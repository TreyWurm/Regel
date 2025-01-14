package at.nukular.core.ui.views

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Parcelable
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.children
import androidx.core.view.setPadding
import at.nukular.core.extensions.AppConfig
import at.nukular.core.extensions.dpAsPx
import at.nukular.core.extensions.dpAsPxFloat
import at.nukular.core.extensions.toColorStateList
import at.nukular.core.ui.theming.Theme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.parcelize.Parcelize
import javax.inject.Inject


private const val MIN_HEIGHT = 16
private const val CORNER_RADIUS = 12


/**
 * LinearLayout mocking iOS's UISegmentedControl
 */
@AndroidEntryPoint
class SegmentedControlView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : LinearLayout(context, attrs), UiComponent {

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


    interface SelectionListener {
        fun onItemSelected(item: View) {}
        fun onItemUnSelected(item: View) {}
        fun onItemReselected(item: View) {}
    }

    val listeners: MutableSet<SelectionListener> = mutableSetOf()


    var selectedId = 0
        private set

    private lateinit var childBackgrounds: List<Drawable>

    init {
        gravity = Gravity.CENTER_VERTICAL
        minimumHeight = MIN_HEIGHT.dpAsPx
        clipChildren = false
        clipToPadding = false
        setPadding(2.dpAsPx)

        val drawable = GradientDrawable().also {
            it.color = Theme.lighten(Color.LTGRAY, .05f).toColorStateList()
            it.cornerRadius = CORNER_RADIUS.dpAsPxFloat
        }
        background = drawable
    }


    override fun onFinishInflate() {
        super.onFinishInflate()
        if (children.any { child -> child !is TextView && child !is ImageView }) {
            throw IllegalStateException("Children of " + SegmentedControlView::class.java.name + " must be of type TextView || ImageView")
        }


        val backgrounds = mutableListOf<Drawable>()
        children.forEach { child ->
            backgrounds.add(
                createRippleWith(
                    background = GradientDrawable().apply {
                        this.color = colorSurface.toColorStateList()
                        cornerRadius = (CORNER_RADIUS - 1).dpAsPxFloat
                    },
                )
            )

            child.elevation = 2.dpAsPxFloat
            child.setOnClickListener { selectChild(it) }
            child.minimumHeight = MIN_HEIGHT.dpAsPx
            if (child is TextView) {
                child.gravity = Gravity.CENTER
            }
        }

        childBackgrounds = backgrounds
        if (childCount > 0) {
            selectChild(children.first())
        }
    }

    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()
        return SavedState(superState, selectedId)
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        val savedState = state as SavedState
        super.onRestoreInstanceState(savedState.superState)
        selectedId = savedState.selectedId
        updateChildrenViews(selectedId)
    }


    fun selectChild(targetView: View) {
        val priorSelectedId = selectedId

        updateChildrenViews(targetView.id)
        selectedId = targetView.id

        if (priorSelectedId == targetView.id) {
            listeners.forEach { it.onItemReselected(targetView) }
        } else {
            listeners.forEach { it.onItemSelected(targetView) }
        }
    }

    private fun updateChildrenViews(targetViewId: Int) {
        children.forEachIndexed { index, child ->
            if (child.id == targetViewId) {
                child.background = childBackgrounds[index]
            } else {
                child.setBackgroundResource(0)
            }
        }
    }


    fun addListener(listener: SelectionListener) {
        listeners.add(listener)
    }

    fun removeListener(listener: SelectionListener) {
        listeners.remove(listener)
    }

    /**
     * SaveState class
     */
    @Parcelize
    private class SavedState(val source: Parcelable?, val selectedId: Int) : BaseSavedState(source)
}