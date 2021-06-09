package com.darkvyl.finansemanagerpjatk

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.darkvyl.finansemanagerpjatk.databinding.ActivityAddEditBinding
import com.darkvyl.finansemanagerpjatk.model.Expense
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class AddEditActivity : AppCompatActivity() {
    private val binding by lazy { ActivityAddEditBinding.inflate(layoutInflater) }
    private val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
    private val editItemId by lazy { intent.extras?.getInt("edit_item_pos") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val editItemId = this.editItemId
        if (editItemId != null) {
            val editItem = Shared.expanseList[editItemId]
            binding.editWhere.setText(editItem.where)
            binding.editCost.setText(editItem.cost.toString())
            binding.editCategory.setText(editItem.category)
            binding.editDate.setText(editItem.date.format(dateTimeFormatter))
        }
    }

    fun save(view: View) {
        if (!validate())
            return
        val expense = Expense(
            binding.editWhere.text.toString(),
            binding.editCost.text.toString().toDouble(),
            LocalDateTime.parse(binding.editDate.text.toString(), dateTimeFormatter),
            binding.editCategory.text.toString()
        )
        val editItemId = this.editItemId
        if (editItemId != null) {
            Shared.expanseList.removeAt(editItemId)
            Shared.expanseList.add(editItemId, expense)
        }
        else
            Shared.expanseList.add(expense)
        Shared.sum = Shared.expanseList.sumByDouble {
            it.cost
        }
        setResult(Activity.RESULT_OK)
        finish()
    }
    fun setDateTime(view: View) {
        val c: Calendar = Calendar.getInstance()
        val mYear = c.get(Calendar.YEAR)
        val mMonth = c.get(Calendar.MONTH)
        val mDay = c.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, year, monthOfYear, dayOfMonth ->
            val dayOfMonthStr = when {
                dayOfMonth < 10 -> {
                    "0$dayOfMonth"
                }
                else -> {
                    dayOfMonth.toString()
                }
            }
            val monthOfYearStr = when {
                monthOfYear + 1 < 10 -> {
                    "0${monthOfYear + 1}"
                }
                else -> {
                    (monthOfYear + 1).toString()
                }
            }
            val mHour = c[Calendar.HOUR_OF_DAY]
            val mMinute = c[Calendar.MINUTE]

            val timePickerDialog = TimePickerDialog(this, { _, hourOfDay, minute ->
                val hourOfDayStr = when {
                    hourOfDay < 10 -> {
                        "0$hourOfDay"
                    }
                    else -> {
                        hourOfDay.toString()
                    }
                }
                val minuteStr = when {
                    minute < 10 -> {
                        "0$minute"
                    }
                    else -> {
                        minute.toString()
                    }
                }
                val date = "$dayOfMonthStr/$monthOfYearStr/$year $hourOfDayStr:$minuteStr"
                binding.editDate.setText(date) }, mHour, mMinute, true)
            timePickerDialog.show()
        }, mYear, mMonth, mDay)
        datePickerDialog.show()
    }

    private fun validate(): Boolean {
        try {
            LocalDateTime.parse(binding.editDate.text.toString(), dateTimeFormatter)
        } catch (e: Exception) {
            Toast.makeText(this, "Enter date!", Toast.LENGTH_SHORT).show()
            binding.editDate.invalidate()
            return false
        }
        if (binding.editWhere.text.toString().isEmpty()) {
            Toast.makeText(this, "Enter where was the expense!", Toast.LENGTH_SHORT).show()
            return false
        }
        if (binding.editCost.text.toString().isEmpty()) {
            Toast.makeText(this, "Enter cost of the expense!", Toast.LENGTH_SHORT).show()
            return false
        }
        if (binding.editCategory.text.toString().isEmpty()) {
            Toast.makeText(this, "Enter category of the expense!", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

}