package com.darkvyl.finansemanagerpjatk.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.darkvyl.finansemanagerpjatk.Shared
import com.darkvyl.finansemanagerpjatk.databinding.ItemExpanseBinding
import com.darkvyl.finansemanagerpjatk.viewHolder.ExpenseVH

class ExpenseAdapter : RecyclerView.Adapter<ExpenseVH>() {
    private var clickListener: ((Int) -> Unit)? = null
    private var longClickListener: ((View, Int) -> Boolean)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseVH {
        val binding = ItemExpanseBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ExpenseVH(binding).also { holder ->
            binding.root.setOnLongClickListener {
                if (this.longClickListener != null)
                    this.longClickListener?.let {it1 -> it1(it, holder.layoutPosition)}
                false
            }

            binding.root.setOnClickListener {
                this.clickListener?.let { it1 -> it1(holder.layoutPosition) }
            }
        }
    }

    override fun onBindViewHolder(holder: ExpenseVH, position: Int) {
        holder.bind(Shared.expanseList[position])
    }

    override fun getItemCount(): Int = Shared.expanseList.size

    fun refresh() {
        notifyDataSetChanged()
    }

    fun setOnClickListener(listener: (Int) -> Unit) {
        this.clickListener = listener
    }

    fun setOnLongClickListener(listener: (View, Int) -> Boolean) {
        this.longClickListener = listener
    }
}