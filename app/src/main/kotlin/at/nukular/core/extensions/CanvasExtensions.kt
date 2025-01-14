package at.nukular.core.extensions

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.text.StaticLayout
import androidx.core.graphics.withTranslation


fun Bitmap.drawTranslated(canvas: Canvas, translationX: Float, translationY: Float, srcRect: Rect? = null, dstRect: Rect, paintIcon: Paint? = null) {
    canvas.withTranslation(translationX, translationY) {
        drawBitmap(this@drawTranslated, srcRect, dstRect, paintIcon)
    }
}

fun Drawable.drawTranslated(canvas: Canvas, translationX: Float, translationY: Float) {
    canvas.withTranslation(translationX, translationY) {
        draw(this)
    }
}

fun StaticLayout.drawTranslated(canvas: Canvas, translationX: Float = 0f, translationY: Float = 0f) {
    canvas.withTranslation(translationX, translationY) {
        draw(this)
    }
}