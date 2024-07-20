package com.sample.app.todolist.todo.ui.list.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.sample.app.todolist.databinding.ItemTaskBinding
import com.sample.app.todolist.todo.data.model.Task
import com.sample.app.todolist.todo.ui.list.TaskActionsContract

class TaskListAdapter(private val taskActionsContract: TaskActionsContract) : PagingDataAdapter<Task, TaskViewHolder>(TaskDiffUtil()) {

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

    @SuppressLint("NotifyDataSetChanged")
    fun updateItems(mapper: (Task) -> Task?) {
        snapshot().map { data ->
            if (data != null) mapper(data)
            else null
        }
        notifyDataSetChanged()
    }
}