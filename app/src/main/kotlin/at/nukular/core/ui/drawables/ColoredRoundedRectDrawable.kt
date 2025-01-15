package at.nukular.core.ui.drawables

import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.graphics.drawable.shapes.Shape

class ColoredRoundedRectDrawable(
    override val color: Int,
    val radii: FloatArray? = null,
) : ShapeDrawable(),
    ColoredShapeDrawable {
    override var customShape: Shape = RoundRectShape(null, null, null)
    override val drawable: Drawable = this

    init {
        paint.color = color
        shape = customShape
    }

    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)
        customShape = RoundRectShape(FloatArray(8) { minOf(bounds.width(), bounds.height()).toFloat() }, null, null)
        shape = customShape
    }
}