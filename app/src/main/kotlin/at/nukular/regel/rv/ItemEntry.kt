package at.nukular.regel.rv

import android.view.View
import at.nukular.core.ui.rv.UpdateVHInterface
import at.nukular.regel.Constants
import at.nukular.regel.R
import at.nukular.regel.databinding.ItemEntryBinding
import at.nukular.regel.model.VHUEntry
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.viewholders.FlexibleViewHolder

class ItemEntry(
    val vhu: VHUEntry,
) : AbstractFlexibleItem<ItemEntry.VHEntry>() {

    override fun getLayoutRes(): Int {
        return R.layout.item_entry
    }

    override fun createViewHolder(view: View, adapter: FlexibleAdapter<IFlexible<*>>): VHEntry {
        return VHEntry(view, adapter)
    }

    override fun bindViewHolder(adapter: FlexibleAdapter<IFlexible<*>>, holder: VHEntry, position: Int, payloads: List<Any>) {
        holder.update(vhu, payloads)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as ItemEntry
        return vhu == other.vhu
    }

    override fun hashCode(): Int {
        return vhu.hashCode()
    }


    /**
     * ViewHolder class
     */
    class VHEntry(
        view: View,
        adapter: FlexibleAdapter<*>,
    ) : FlexibleViewHolder(view, adapter), UpdateVHInterface<VHUEntry> {

        private lateinit var item: VHUEntry
        private lateinit var binding: ItemEntryBinding

        init {
            binding = ItemEntryBinding.bind(view)
        }


        override fun update(updateObject: VHUEntry, payloads: List<Any>) {
            item = updateObject

            if (payloads.isEmpty()) {
                binding.root.text = Constants.dayOfMonthMonthAbbrYear.format(item.date)
            } else {
                for (payload in payloads) {
                }
            }
        }
    }
}