package at.nukular.core.ui.drawables

import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.graphics.drawable.shapes.Shape

class ColoredRectDrawable(override val color: Int) : ShapeDrawable(), ColoredShapeDrawable {
    override var customShape: Shape = RectShape()
    override val drawable: Drawable = this

    init {
        paint.color = color
        shape = customShape
    }

    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)
        customShape = RectShape()
        shape = customShape
    }
}