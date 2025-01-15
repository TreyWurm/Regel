package at.nukular.core.ui.drawables

import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.graphics.drawable.shapes.Shape

class ColoredCircleDrawable(override val color: Int) : ShapeDrawable(), ColoredShapeDrawable {

    override val customShape: Shape = OvalShape()
    override val drawable: Drawable = this

    init {
        paint.color = color
        shape = customShape
    }

//    override fun onDraw(shape: Shape, canvas: Canvas, paint: Paint) {
//        super.onDraw(shape, canvas, paint)
//    }
//    override fun draw(canvas: Canvas, paint: Paint) {
//        paint.color = this.color
//        super.draw(canvas, paint)
//    }
}