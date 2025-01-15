package at.nukular.core.ui.drawables

import android.graphics.drawable.Drawable
import android.graphics.drawable.shapes.Shape

sealed interface ColoredShapeDrawable {
    val color: Int
    val customShape: Shape
    val drawable: Drawable
}