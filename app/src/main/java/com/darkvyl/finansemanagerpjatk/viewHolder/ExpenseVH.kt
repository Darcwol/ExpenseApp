package com.darkvyl.finansemanagerpjatk.viewHolder

import androidx.recyclerview.widget.RecyclerView
import com.darkvyl.finansemanagerpjatk.databinding.ItemExpanseBinding
import com.darkvyl.finansemanagerpjatk.model.Expense
import java.time.format.DateTimeFormatter

class ExpenseVH(private val binding: ItemExpanseBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(expense: Expense) {
        with(binding) {
            where.text = expense.where
            cost.text = String.format("%.2f PLN", expense.cost)
            date.text = expense.date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
            category.text = expense.category
        }
    }
}