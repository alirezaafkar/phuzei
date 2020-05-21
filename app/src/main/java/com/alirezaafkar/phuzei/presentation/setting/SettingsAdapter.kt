package com.alirezaafkar.phuzei.presentation.setting

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alirezaafkar.phuzei.R
import com.google.android.material.radiobutton.MaterialRadioButton

/**
 * Created by Alireza Afkar on 16/9/2018AD.
 */
class SettingsAdapter(
    private val listener: (String) -> Unit
) : RecyclerView.Adapter<SettingsAdapter.ViewHolder>() {

    private var category = ""
    private val items = listOf(
        "All",
        "Favorites",
        "Landscapes",
        "Receipts",
        "Cityscapes",
        "Landmarks",
        "Selfies",
        "People",
        "Pets",
        "Weddings",
        "Birthdays",
        "Documents",
        "Travel",
        "Animals",
        "Food",
        "Sport",
        "Night",
        "Performances",
        "Whiteboards",
        "Screenshots",
        "Utility",
        "Arts",
        "Crafts",
        "Fashion",
        "Houses",
        "Gardens",
        "Flowers",
        "Holidays"
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(items[position]) {
            holder.bind(this, this == category, listener)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: String, selected: Boolean, listener: (String) -> Unit) {
            (itemView as MaterialRadioButton).run {
                text = item
                isChecked = selected
                setOnClickListener { listener(item) }
            }
        }
    }

    fun setCategory(category: String) {
        this.category = if (category.isEmpty()) {
            items.first()
        } else {
            category
        }
        notifyDataSetChanged()
    }
}
