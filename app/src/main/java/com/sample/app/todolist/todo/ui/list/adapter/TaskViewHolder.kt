package com.sample.app.todolist.todo.ui.list.adapter

import androidx.recyclerview.widget.RecyclerView
import com.sample.app.todolist.databinding.ItemTaskBinding
import com.sample.app.todolist.todo.common.toPrettyDate
import com.sample.app.todolist.todo.data.model.Task
import com.sample.app.todolist.todo.ui.list.TaskActionsContract

class TaskViewHolder(private val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {

    fun setData(task: Task, taskActionsContract: TaskActionsContract) {
        binding.title.text = task.title
        binding.createdOn.text = task.createdOn.toPrettyDate()
        binding.completed.isChecked = task.completed

        binding.completed.setOnCheckedChangeListener { _, isChecked ->
            taskActionsContract.updateTask(task.copy(completed = isChecked))
        }

        binding.parent.setOnClickListener {
            taskActionsContract.openTask(task)
        }
    }
}