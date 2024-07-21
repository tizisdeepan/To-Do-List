package com.sample.app.todolist.todo.presentation.list.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.sample.app.todolist.databinding.ItemTaskBinding
import com.sample.app.todolist.todo.presentation.list.ui.TaskActionsContract
import com.sample.app.todolist.todo.presentation.list.ui.TaskUI

class TaskListAdapter(private val taskActionsContract: TaskActionsContract) : PagingDataAdapter<TaskUI, TaskViewHolder>(TaskDiffUtil()) {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        context = parent.context
        return TaskViewHolder(ItemTaskBinding.inflate(LayoutInflater.from(context)))
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        getItem(position)?.let {
            holder.setData(it, taskActionsContract)
        }
    }
}