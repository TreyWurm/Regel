package at.nukular.core.extensions

import android.graphics.*

@JvmOverloads
fun Bitmap.createBitmapFrom(
    width: Int = this.width,
    height: Int = this.height,
    color: Int? = null,
): Bitmap {
    val bitmap = Bitmap.createScaledBitmap(this, width, height, false)
    if (color == null) {
        return bitmap
    }

    val c = Canvas(bitmap)
    val paint = Paint()
    paint.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
    c.drawBitmap(bitmap, 0f, 0f, paint)
    return bitmap
}