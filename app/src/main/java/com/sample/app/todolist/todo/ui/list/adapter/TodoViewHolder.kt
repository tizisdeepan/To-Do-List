package com.sample.app.todolist.todo.ui.list.adapter

import androidx.recyclerview.widget.RecyclerView
import com.sample.app.todolist.databinding.ItemTodoBinding
import com.sample.app.todolist.todo.data.model.Todo
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TodoViewHolder(private val binding: ItemTodoBinding) : RecyclerView.ViewHolder(binding.root) {

    fun setData(todo: Todo) {
        binding.title.text = todo.title
        binding.createdOn.text = todo.createdOn.toPrettyDate()
        binding.completed.isChecked = todo.completed
    }

    private fun Long.toPrettyDate(): String {
        return Date().run {
            time = this@toPrettyDate
            SimpleDateFormat("MMM dd, yyyy, hh:mm a", Locale.getDefault()).format(this)
        }
    }
}