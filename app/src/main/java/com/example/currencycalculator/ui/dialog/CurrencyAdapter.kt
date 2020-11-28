package com.example.currencycalculator.ui.dialog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.currencycalculator.R
import com.example.currencycalculator.data.model.CurrencySymbol
import com.example.currencycalculator.util.loadImage
import kotlinx.android.synthetic.main.currency_item.view.*

class CurrencyAdapter(private val viewModel: CurrencyViewModel) :
    ListAdapter<CurrencySymbol, CurrencyAdapter.ViewHolder>(CurrencyDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.currency_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(viewModel, getItem(position))
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(viewModel: CurrencyViewModel, symbol: CurrencySymbol) {
            with(itemView) {
                currency.text = "${symbol.currency} (${symbol.name})"
                currencyImg.loadImage(symbol.currency)
                setOnClickListener { viewModel.selectCurrency(symbol.currency) }
            }
        }
    }
}

class CurrencyDiffCallback : DiffUtil.ItemCallback<CurrencySymbol>() {
    override fun areItemsTheSame(oldItem: CurrencySymbol, newItem: CurrencySymbol): Boolean {
        return oldItem.currency == newItem.currency
    }

    override fun areContentsTheSame(oldItem: CurrencySymbol, newItem: CurrencySymbol): Boolean {
        return oldItem == newItem
    }
}