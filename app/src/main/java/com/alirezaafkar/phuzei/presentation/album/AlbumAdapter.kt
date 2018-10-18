package com.alirezaafkar.phuzei.presentation.album

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.alirezaafkar.phuzei.R
import com.alirezaafkar.phuzei.data.model.Album
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_album.view.*

/**
 * Created by Alireza Afkar on 16/9/2018AD.
 */
class AlbumAdapter(private val album: String?, private val listener: (Album) -> Unit) :
    androidx.recyclerview.widget.RecyclerView.Adapter<AlbumAdapter.ViewHolder>() {

    private var items = mutableListOf<Album>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_album, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(items[position]) {
            holder.bind(this, this.id == album, listener)
        }
    }

    class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        fun bind(item: Album, selected: Boolean, listener: (Album) -> Unit) = with(itemView) {
            name.text = item.title
            count.text = this.context.getString(R.string.items_count, item.itemsCount)
            tick.isVisible = selected
            Glide.with(this).load(item.coverPhotoUrl).into(image)

            setOnClickListener { listener(item) }
        }
    }

    fun addItems(items: List<Album>) {
        val oldSize = this.items.size
        this.items.addAll(items)
        notifyItemRangeInserted(oldSize, items.size)
    }

    fun clearItems() {
        items.clear()
        notifyDataSetChanged()
    }
}
