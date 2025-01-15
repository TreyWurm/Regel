package at.nukular.regel.rv

import at.nukular.regel.model.VHUEntry
import eu.davidea.flexibleadapter.FlexibleAdapter
import java.time.LocalDate

class EntryAdapter(
    items: List<ItemEntry> = emptyList(),
    listeners: Any? = null,
    stableIds: Boolean = false,
) : FlexibleAdapter<ItemEntry>(items, listeners, stableIds) {

    fun setEntries(vhus: List<LocalDate>) {
        updateDataSet(createItems(vhus.map { VHUEntry(it) }))
    }

    fun addEntries(vhus: List<VHUEntry>) {
        addItems(itemCount, createItems(vhus))
    }

    private fun createItems(vhus: List<VHUEntry>): List<ItemEntry> = vhus.map { createItem(it) }
    private fun createItem(vhu: VHUEntry): ItemEntry = ItemEntry(vhu)
}