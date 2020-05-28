package com.alirezaafkar.phuzei.presentation.donate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alirezaafkar.phuzei.R
import com.android.billingclient.api.SkuDetails
import kotlinx.android.synthetic.main.item_donate.view.*

/**
 * Created by Alireza Afkar on 16/9/2018AD.
 */
class DonateAdapter(
    private val items: List<SkuDetails>,
    private val listener: (SkuDetails) -> Unit
) : RecyclerView.Adapter<DonateAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_donate, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], listener)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: SkuDetails, listener: (SkuDetails) -> Unit) {
            itemView.run {
                title.text = item.title
                description.text = item.price
                setOnClickListener { listener(item) }
            }
        }
    }
}
