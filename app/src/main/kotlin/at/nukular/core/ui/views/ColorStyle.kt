package at.nukular.core.ui.views

const val COLOR_STYLE_ON_PRIMARY = 0x10
const val COLOR_STYLE_ON_SURFACE = 0x20
const val COLOR_STYLE_PRIMARY = 0x30
const val COLOR_STYLE_SURFACE = 0x40
const val COLOR_STYLE_TEXT_INTENSE = 0x50
const val COLOR_STYLE_TEXT_LIGHT = 0x51
const val COLOR_STYLE_DISABLED = 0x60
const val COLOR_STYLE_ERROR = 0x70
const val COLOR_STYLE_SUCCESS = 0x80


enum class ColorStyle(val style: Int) {
    DISABLED(COLOR_STYLE_DISABLED),
    ERROR(COLOR_STYLE_ERROR),
    ON_PRIMARY(COLOR_STYLE_ON_PRIMARY),
    ON_SURFACE(COLOR_STYLE_ON_SURFACE),
    PRIMARY(COLOR_STYLE_PRIMARY),
    SUCCESS(COLOR_STYLE_SUCCESS),
    SURFACE(COLOR_STYLE_SURFACE),
    TEXT_INTENSE(COLOR_STYLE_TEXT_INTENSE),
    TEXT_LIGHT(COLOR_STYLE_TEXT_LIGHT),
    ;

    companion object {
        private val map = entries.associateBy(ColorStyle::style)
        fun fromInt(type: Int) = map[type]
    }
}