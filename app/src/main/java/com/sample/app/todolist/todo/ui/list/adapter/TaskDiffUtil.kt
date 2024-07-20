package com.sample.app.todolist.todo.ui.list.adapter

import androidx.recyclerview.widget.DiffUtil
import com.sample.app.todolist.todo.data.model.Task

class TaskDiffUtil : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem == newItem
    }
}