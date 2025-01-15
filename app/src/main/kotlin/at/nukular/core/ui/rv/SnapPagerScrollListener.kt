package at.nukular.core.ui.rv

import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView

class SnapPagerScrollListener(
    private val notifyOnInit: Boolean = false,
    private val snapHelper: PagerSnapHelper,
    private val type: Type,
    private var enabled: Boolean = true,
    var listener: OnChangeListener?,
) : RecyclerView.OnScrollListener() {

    private var snapPosition: Int = RecyclerView.NO_POSITION

    enum class Type {
        ON_SCROLL,
        ON_SETTLED
    }

    interface OnChangeListener {
        fun onSnapped(position: Int, type: Type)
    }


    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if ((type == Type.ON_SCROLL) || !hasItemPosition()) {
            notifyListenerIfNeeded(getSnapPosition(recyclerView))
        }

        if (type == Type.ON_SETTLED && dx == 0 && dy == 0) {
            notifyListenerIfNeeded(getSnapPosition(recyclerView))
        }
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        if (type == Type.ON_SETTLED && newState == RecyclerView.SCROLL_STATE_IDLE) {
            notifyListenerIfNeeded(getSnapPosition(recyclerView))
        }
    }

    fun getSnapPosition(recyclerView: RecyclerView): Int {
        val layoutManager = recyclerView.layoutManager
        if (layoutManager == null) {
            return RecyclerView.NO_POSITION
        }

        val snapView = snapHelper.findSnapView(layoutManager)
        if (snapView == null) {
            return RecyclerView.NO_POSITION
        }

        return layoutManager.getPosition(snapView)
    }

    private fun notifyListenerIfNeeded(newSnapPosition: Int) {
        if (enabled && snapPosition != newSnapPosition) {
            if (notifyOnInit && !hasItemPosition()) {
                listener?.onSnapped(newSnapPosition, type)
            } else if (hasItemPosition()) {
                listener?.onSnapped(newSnapPosition, type)
            }

            snapPosition = newSnapPosition
        }
    }

    private fun hasItemPosition(): Boolean {
        return snapPosition != RecyclerView.NO_POSITION
    }

    fun setEnabled(enabled: Boolean) {
        this.enabled = enabled
    }

    fun isEnabled(): Boolean {
        return enabled
    }
}