package at.nukular.core.ui.rv

/**
 * Updates ViewHolder
 *
 * Use in combination with VHUs(ViewHolderUpdates) for items that need a composition of multiple elements to update
 */
interface VHInterface<T> {
    fun update(updateObject: T, payloads: List<Any>)
    fun unbindViewHolder() {}
    fun onViewAttached() {}
    fun onViewDetached() {}
}
