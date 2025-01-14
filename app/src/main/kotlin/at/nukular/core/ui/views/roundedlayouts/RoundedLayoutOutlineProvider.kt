package at.nukular.core.ui.views.roundedlayouts

import android.graphics.Outline
import android.graphics.Path
import android.os.Build
import android.view.View
import android.view.ViewOutlineProvider
import at.nukular.core.extensions.with

class RoundedLayoutOutlineProvider : ViewOutlineProvider() {

    var width: Int = 0
    var height: Int = 0
    var cornerRadii: IntArray = IntArray(8) { 0 }

    override fun getOutline(view: View, outline: Outline) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val path = Path().with {
                it.addRoundRect(0f, 0f, width.toFloat(), height.toFloat(), cornerRadii.map { radius -> radius.toFloat() }.toFloatArray(), Path.Direction.CW)
            }
            outline.setPath(path)
        } else {
            // Prior to API 30 we can only have same corner radii for clipping
            // As setting Outline.setConvexPath() would lead to Outline.canClip() return false
            if (cornerRadii.distinct().size > 1) {
                outline.setRect(0, 0, width, height)
            } else {
                outline.setRoundRect(0, 0, width, height, cornerRadii[0].toFloat())
            }
        }
    }
}