package com.sample.app.todolist.todo.presentation.list.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.sample.app.todolist.todo.presentation.list.ui.TaskUI

class TaskDiffUtil : DiffUtil.ItemCallback<TaskUI>() {
    override fun areItemsTheSame(oldItem: TaskUI, newItem: TaskUI): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TaskUI, newItem: TaskUI): Boolean {
        return oldItem == newItem
    }
}