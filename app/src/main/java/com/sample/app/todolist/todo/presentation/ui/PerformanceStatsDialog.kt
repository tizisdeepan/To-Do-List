package com.sample.app.todolist.todo.presentation.ui

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import com.sample.app.todolist.R
import com.sample.app.todolist.databinding.DialogPerformanceStatsBinding
import com.sample.app.todolist.todo.data.model.DatabasePerformance

class PerformanceStatsDialog(ctx: Context, private val databasePerformance: DatabasePerformance) : Dialog(ctx) {

    private lateinit var binding: DialogPerformanceStatsBinding

    override fun onStart() {
        super.onStart()
        window?.setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogPerformanceStatsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.gotIt.setOnClickListener {
            dismiss()
        }

        binding.roomWrites.text = context.resources.getString(R.string.writes, databasePerformance.room.averageWrites.toString())
        binding.roomReads.text = context.resources.getString(R.string.reads, databasePerformance.room.averageReads.toString())
        binding.roomDeletes.text = context.resources.getString(R.string.deletes, databasePerformance.room.averageDeletes.toString())

        binding.sqliteWrites.text = context.resources.getString(R.string.writes, databasePerformance.sqlite.averageWrites.toString())
        binding.sqliteReads.text = context.resources.getString(R.string.reads, databasePerformance.sqlite.averageReads.toString())
        binding.sqliteDeletes.text = context.resources.getString(R.string.deletes, databasePerformance.sqlite.averageDeletes.toString())

        setCancelable(false)
        setCanceledOnTouchOutside(false)
    }
}