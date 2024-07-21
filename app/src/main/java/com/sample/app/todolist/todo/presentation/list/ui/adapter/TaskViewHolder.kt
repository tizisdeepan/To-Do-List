package com.sample.app.todolist.todo.presentation.list.ui.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.sample.app.todolist.databinding.ItemTaskBinding
import com.sample.app.todolist.todo.presentation.list.ui.TaskActionsContract
import com.sample.app.todolist.todo.presentation.list.ui.TaskUI
import com.sample.app.todolist.todo.presentation.list.ui.toTask

class TaskViewHolder(private val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {

    fun setData(task: TaskUI, taskActionsContract: TaskActionsContract) {
        binding.parent.visibility = View.VISIBLE
        binding.completed.setOnCheckedChangeListener(null)
        binding.title.text = task.title
        binding.createdOn.text = task.displayDate
        binding.completed.isChecked = task.completed

        binding.completed.setOnCheckedChangeListener { _, isChecked ->
            taskActionsContract.updateTask(task.copy(completed = isChecked).toTask())
        }

        binding.parent.setOnClickListener {
            taskActionsContract.openTask(task.id)
        }
    }
}