package at.nukular.core.ui.rv

/**
 * Updates ViewHolder<br></br>
 * Use in combination with VHUs(ViewHolderUpdates) for items that need a composition of multiple elements to update
 *
 * @param <T>
</T> */
interface UpdateVHInterface<T> {
    fun update(updateObject: T, payloads: List<Any>)
    fun unbindViewHolder() {}
    fun onViewAttached() {}
    fun onViewDetached() {}
}
