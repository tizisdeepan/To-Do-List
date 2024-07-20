package com.sample.app.todolist.todo.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.sample.app.todolist.databinding.FragmentTaskListBinding
import com.sample.app.todolist.todo.data.model.Task
import com.sample.app.todolist.todo.ui.home.HomeActivity
import com.sample.app.todolist.todo.ui.list.adapter.TaskListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TaskListFragment : Fragment(), TaskActionsContract {

    private lateinit var binding: FragmentTaskListBinding
    private val viewModel: TaskListViewModel by viewModels()

    private val adapter: TaskListAdapter by lazy { TaskListAdapter(this) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentTaskListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.taskList.layoutManager = LinearLayoutManager(requireContext())
        binding.taskList.adapter = adapter
        binding.taskList.itemAnimator = null

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.uiState.collectLatest {
                    it.taskList?.let { pagingData ->
                        adapter.submitData(pagingData)
                    }

                    it.updatedTaskItem.consume()?.let { updatedTodoItem ->
                        adapter.updateItems { todo ->
                            if (todo.id == updatedTodoItem.id) {
                                updatedTodoItem
                            } else todo
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.fetchTodoItems()
            }
        }

        binding.create.setOnClickListener {
            (activity as? HomeActivity)?.navigateToCreateTaskPage()
        }

        binding.refresh.setOnRefreshListener {
            refreshPage()
        }
    }

    fun refreshPage() {
        adapter.refresh()
        if (binding.refresh.isRefreshing) binding.refresh.isRefreshing = false
    }

    override fun updateTask(task: Task) {
        viewModel.updateTodoItem(task)
    }

    override fun openTask(task: Task) {
        (activity as? HomeActivity)?.navigateToTaskDetailsPage(task.id)
    }

    companion object {
        @JvmStatic
        fun newInstance(): TaskListFragment = TaskListFragment()
    }
}