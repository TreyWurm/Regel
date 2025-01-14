package at.nukular.core.extensions

import android.graphics.Rect

/**
 * Transforms a Rect(left,top,right,bottom) to a size 4 [IntArray]
 */
fun Rect.toArray(): IntArray {
    return intArrayOf(left, top, right, bottom)
}

/**
 * Transforms a size 4 [IntArray] to a [Rect] (left,top,right,bottom)
 * In case the array wasn't size 4 a new Rect(0,0,0,0) will be returned
 */
fun IntArray.toRect(): Rect {
    if (size != 4) {
        return Rect()
    }

    return Rect(this[0], this[1], this[2], this[3])
}