package com.cosmos.candelabra.ui.quotedetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cosmos.candelabra.data.model.QuoteDetail
import com.cosmos.candelabra.databinding.ItemQuoteDetailsBinding

class QuoteDetailsAdapter : ListAdapter<QuoteDetail, QuoteDetailsAdapter.ViewHolder>(
    DETAIL_COMPARATOR
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemQuoteDetailsBinding.inflate(inflater, parent, false))
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

    inner class ViewHolder(
        private val binding: ItemQuoteDetailsBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(quoteDetail: QuoteDetail) {
            binding.quoteDetail = quoteDetail
        }

        fun update(quoteDetail: QuoteDetail) {
            binding.data.text = quoteDetail.data
        }
    }

    companion object {
        private val DETAIL_COMPARATOR = object : DiffUtil.ItemCallback<QuoteDetail>() {
            override fun areItemsTheSame(oldItem: QuoteDetail, newItem: QuoteDetail): Boolean {
                return oldItem.title == newItem.title
            }

            override fun areContentsTheSame(oldItem: QuoteDetail, newItem: QuoteDetail): Boolean {
                return oldItem.data == newItem.data
            }

            override fun getChangePayload(oldItem: QuoteDetail, newItem: QuoteDetail): Any? {
                return newItem
            }
        }
    }
}
