package com.cosmos.candelabra.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cosmos.candelabra.data.model.SearchItem
import com.cosmos.candelabra.databinding.ItemSearchQuoteBinding

class SearchAdapter(
    private val onClick: (View, SearchItem.Quote) -> Unit
) : ListAdapter<SearchItem, RecyclerView.ViewHolder>(SEARCH_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return QuoteViewHolder(ItemSearchQuoteBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as QuoteViewHolder).bind(getItem(position) as SearchItem.Quote)
    }

    inner class QuoteViewHolder(
        private val binding: ItemSearchQuoteBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(quote: SearchItem.Quote) {
            binding.quote = quote

            binding.root.setOnClickListener { onClick.invoke(binding.layoutRoot, quote) }
        }
    }

    companion object {
        private val SEARCH_COMPARATOR = object : DiffUtil.ItemCallback<SearchItem>() {

            override fun areItemsTheSame(oldItem: SearchItem, newItem: SearchItem): Boolean {
                return (oldItem as? SearchItem.Quote)?.symbol ==
                        (newItem as? SearchItem.Quote)?.symbol
            }

            override fun areContentsTheSame(oldItem: SearchItem, newItem: SearchItem): Boolean {
                return (oldItem as? SearchItem.Quote) == (newItem as? SearchItem.Quote)
            }
        }
    }
}
