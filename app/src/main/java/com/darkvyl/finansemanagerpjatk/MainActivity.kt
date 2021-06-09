package com.darkvyl.finansemanagerpjatk

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.darkvyl.finansemanagerpjatk.adapter.ExpenseAdapter
import com.darkvyl.finansemanagerpjatk.databinding.ActivityMainBinding
import com.darkvyl.finansemanagerpjatk.model.Expense
import com.google.android.material.snackbar.Snackbar
import java.time.LocalDateTime

private const val EXPENSE_ADD = 1
private const val EXPENSE_EDIT = 2

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val expenseAdapter by lazy { ExpenseAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        seed()
        setContentView(binding.root)
        Shared.sum = Shared.expanseList.sumByDouble {
            it.cost
        }
        binding.summary.text = String.format("Summary: %.2f PLN", Shared.sum)

        binding.expanseList.apply {
            adapter = expenseAdapter.also {
                it.setOnClickListener  { i ->
                    val editActivity = Intent(this@MainActivity, AddEditActivity::class.java)
                    editActivity.putExtra("edit_item_pos", i)
                    startActivityForResult(editActivity, EXPENSE_EDIT)
                }
                it.setOnLongClickListener { view, i ->
                    val builder = AlertDialog.Builder(this@MainActivity).also { it1 ->
                        it1.setTitle("Confirm")
                        it1.setMessage("Are you sure?")
                    }
                    builder.setPositiveButton("YES") { dialog, _ ->
                        val removedElement = Shared.expanseList.removeAt(i)
                        expenseAdapter.notifyItemRemoved(i)
                        Shared.sum = Shared.expanseList.sumByDouble { it1 ->
                            it1.cost
                        }
                        binding.summary.text = String.format("Summary: %.2f PLN", Shared.sum)

                        val snackbar = Snackbar.make(
                            this@MainActivity,
                            view,
                            "${removedElement.where} is removed",
                            Snackbar.LENGTH_LONG
                        )
                        snackbar.setAction("Undo") {
                            Shared.expanseList.add(i, removedElement)
                            expenseAdapter.notifyItemInserted(i)
                            Shared.sum = Shared.expanseList.sumByDouble { it1 ->
                                it1.cost
                            }
                            binding.summary.text = String.format("Summary: %.2f PLN", Shared.sum)
                        }
                        dialog.dismiss()
                        snackbar.show()
                    }
                    builder.setNegativeButton("NO") { dialog, _ ->
                        dialog.cancel()
                    }
                    val alert: AlertDialog = builder.create()
                    alert.show()
                    true
                }
            }
            layoutManager = LinearLayoutManager(context)
        }
    }

    fun add(view: View) {
        startActivityForResult(
            Intent(this, AddEditActivity::class.java),
            EXPENSE_ADD
        )
    }
    fun goToChart(view: View) {
        startActivity(
            Intent(this, ChartActivity::class.java),
        )
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if ((requestCode == EXPENSE_ADD || requestCode == EXPENSE_EDIT ) && resultCode == Activity.RESULT_OK) {
            expenseAdapter.refresh()
            binding.summary.text = String.format("Summary: %.2f PLN", Shared.sum)
        } else super.onActivityResult(requestCode, resultCode, data)
    }

    private fun seed() {
        Shared.expanseList.add(Expense("Biedronka", -45.78, LocalDateTime.now(), "Products"))
        Shared.expanseList.add(Expense("Work", 500.0, LocalDateTime.now(), "Salary"))
        Shared.expanseList.add(Expense("PJATK", -5.99, LocalDateTime.now().plusDays(2), "Cafe"))
        Shared.expanseList.add(Expense("Multikino", -15.0, LocalDateTime.now().minusDays(2), "Kino"))
    }

}