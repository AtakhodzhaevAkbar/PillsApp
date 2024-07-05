package com.example.pillsapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pillsapp.R
import com.example.pillsapp.data.Item

class ItemAdapter(private var items: MutableList<Item>) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    private var listener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = items[position]
        holder.bind(currentItem)
        holder.setOnItemClickListener(listener)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateItem(updatedItem: Item) {
        val index = items.indexOfFirst { it.id == updatedItem.id }
        if (index != -1) {
            items[index] = updatedItem
            notifyItemChanged(index)
        }
    }

    fun addItem(newItem: Item) {
        items.add(newItem)
        notifyItemInserted(items.size - 1)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTV)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTV)
        private val timeTextView: TextView = itemView.findViewById(R.id.timeTV)
        private val dateTextView: TextView = itemView.findViewById(R.id.dateTV)

        fun bind(item: Item) {
            titleTextView.text = item.title
            descriptionTextView.text = item.description
            timeTextView.text = item.time
            dateTextView.text = item.date
        }

        fun setOnItemClickListener(listener: OnItemClickListener?) {
            itemView.setOnClickListener {
                listener?.onItemClick(adapterPosition)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}
