package com.sample.app.todolist.todo.ui.list.adapter

import androidx.recyclerview.widget.DiffUtil
import com.sample.app.todolist.todo.data.model.Todo

class TodoDiffUtil : DiffUtil.ItemCallback<Todo>() {
    override fun areItemsTheSame(oldItem: Todo, newItem: Todo): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Todo, newItem: Todo): Boolean {
        return oldItem == newItem
    }
}