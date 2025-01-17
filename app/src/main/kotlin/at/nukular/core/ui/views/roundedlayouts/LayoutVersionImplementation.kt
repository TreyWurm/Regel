package at.nukular.core.ui.views.roundedlayouts

import android.graphics.Canvas

interface LayoutVersionImplementation {
    fun initBackground()
    fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int)
    fun dispatchDraw(canvas: Canvas)
}