package com.sample.app.todolist.todo.ui.list.adapter

import androidx.recyclerview.widget.RecyclerView
import com.sample.app.todolist.databinding.ItemTodoBinding
import com.sample.app.todolist.todo.common.toPrettyDate
import com.sample.app.todolist.todo.data.model.Todo
import com.sample.app.todolist.todo.ui.list.TodoActionsContract
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TodoViewHolder(private val binding: ItemTodoBinding) : RecyclerView.ViewHolder(binding.root) {

    fun setData(todo: Todo, todoActionsContract: TodoActionsContract) {
        binding.title.text = todo.title
        binding.createdOn.text = todo.createdOn.toPrettyDate()
        binding.completed.isChecked = todo.completed

        binding.completed.setOnCheckedChangeListener { _, isChecked ->
            todoActionsContract.updateTodoItem(todo.copy(completed = isChecked))
        }

        binding.parent.setOnClickListener {
            todoActionsContract.openTodoItem(todo)
        }
    }
}