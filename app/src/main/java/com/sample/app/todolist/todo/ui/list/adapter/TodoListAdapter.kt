package com.sample.app.todolist.todo.ui.list.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.sample.app.todolist.databinding.ItemTodoBinding
import com.sample.app.todolist.todo.data.model.Todo

class TodoListAdapter : PagingDataAdapter<Todo, TodoViewHolder>(TodoDiffUtil()) {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        context = parent.context
        return TodoViewHolder(ItemTodoBinding.inflate(LayoutInflater.from(context)))
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.setData(getItem(position)!!)
    }
}