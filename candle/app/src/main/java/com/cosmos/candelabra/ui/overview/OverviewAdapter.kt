package com.cosmos.candelabra.ui.overview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import coil.transform.BlurTransformation
import com.cosmos.candelabra.R
import com.cosmos.candelabra.data.model.db.Quote
import com.cosmos.candelabra.databinding.ItemQuoteOverviewBinding
import com.cosmos.candelabra.util.CurrencyUtil
import com.cosmos.candelabra.util.extension.formatChange
import com.cosmos.candelabra.util.extension.formatChangePercent
import com.robinhood.ticker.TickerUtils

class OverviewAdapter(
    private val onClick: (View, Quote) -> Unit
) : ListAdapter<Quote, OverviewAdapter.ViewHolder>(QUOTE_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemQuoteOverviewBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            holder.update(getItem(position))
        }
    }

    fun updateList(quotes: List<Quote>) {
        val newList = mutableListOf(*currentList.toTypedArray())

        quotes.forEach { newQuote ->
            val oldQuoteIndex = newList.indexOfFirst { it.symbol == newQuote.symbol }
            if (oldQuoteIndex != -1) {
                newList[oldQuoteIndex] = newQuote
            }
        }

        submitList(newList)
    }

    inner class ViewHolder(
        private val binding: ItemQuoteOverviewBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.price.setCharacterLists(TickerUtils.provideNumberList())
            binding.change.setCharacterLists(TickerUtils.provideNumberList())
            binding.changePercent.setCharacterLists(TickerUtils.provideNumberList())
        }

        fun bind(quote: Quote) {
            binding.quote = quote

            binding.root.setOnClickListener { onClick.invoke(binding.layoutRoot, quote) }

            binding.background.load(R.drawable.quote_card_background) {
                scale(Scale.FILL)
                transformations(BlurTransformation(binding.background.context, 25F, 4F))
            }

            update(quote)
        }

        fun update(quote: Quote) {
            binding.price.text = CurrencyUtil.formatNumber(quote.price, quote.currency)
            binding.change.formatChange(quote.change)
            binding.changePercent.formatChangePercent(quote.changePercent)
        }
    }

    companion object {
        private val QUOTE_COMPARATOR = object : DiffUtil.ItemCallback<Quote>() {
            override fun areItemsTheSame(oldItem: Quote, newItem: Quote): Boolean {
                return oldItem.symbol == newItem.symbol
            }

            override fun areContentsTheSame(oldItem: Quote, newItem: Quote): Boolean {
                return oldItem == newItem
            }

            override fun getChangePayload(oldItem: Quote, newItem: Quote): Any {
                return newItem
            }
        }
    }
}
