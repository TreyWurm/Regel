package at.nukular.regel.rv

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import at.nukular.core.extensions.with
import at.nukular.core.ui.rv.ThemedViewHolder
import at.nukular.regel.R
import at.nukular.regel.databinding.ItemMonthBinding
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.davidea.flexibleadapter.items.IFlexible

class ItemMonth(
    val vhu: VHUMonth
) : AbstractFlexibleItem<ItemMonth.VH>() {


    override fun getLayoutRes(): Int {
        return R.layout.item_month
    }

    override fun createViewHolder(view: View, adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>): VH {
        return VH(view, adapter)
    }

    override fun bindViewHolder(adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>, holder: VH, position: Int, payloads: MutableList<Any>) {
        holder.update(vhu, payloads)
    }

    override fun unbindViewHolder(adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>, holder: VH, position: Int) {
        holder.unbindViewHolder()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ItemMonth) return false
        if (vhu.month != other.vhu.month) return false

        return true
    }

    override fun hashCode(): Int {
        return vhu.month.hashCode()
    }

    override fun shouldNotifyChange(newItem: IFlexible<*>): Boolean {
        if (newItem is ItemMonth) {
            return vhu.selectedDays != newItem.vhu.selectedDays
        }
        return super.shouldNotifyChange(newItem)
    }


    /**
     * ViewHolder
     */
    class VH(
        view: View,
        adapter: FlexibleAdapter<*>
    ) : ThemedViewHolder<VHUMonth>(view, adapter) {

        private lateinit var binding: ItemMonthBinding
        private lateinit var vhu: VHUMonth


        override fun applyTheme() {
        }

        override fun bindView(view: View) {
            binding = ItemMonthBinding.bind(view)
        }

        override fun initListener() {

        }


        override fun update(updateObject: VHUMonth, payloads: List<Any>) {
            vhu = updateObject

            if (payloads.isEmpty()) {
                binding.root.with {
                    it.month = vhu.month
                    it.addSelectedDays(vhu.selectedDays)
                }

            } else {
                payloads.forEach {
                    when (it) {
                    }
                }
            }
        }

    }
}