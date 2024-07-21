package com.sample.app.todolist.todo.presentation.list.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sample.app.todolist.databinding.FragmentTaskListBinding
import com.sample.app.todolist.todo.data.model.Task
import com.sample.app.todolist.todo.data.database.CurrentCacheStrategy
import com.sample.app.todolist.todo.data.database.DatabaseStrategy
import com.sample.app.todolist.todo.presentation.home.ui.HomeActivity
import com.sample.app.todolist.todo.presentation.list.TaskListViewModel
import com.sample.app.todolist.todo.presentation.list.ui.adapter.TaskListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TaskListFragment : Fragment(), TaskActionsContract, TaskListContract {

    private lateinit var binding: FragmentTaskListBinding
    private val viewModel: TaskListViewModel by viewModels()

    private val adapter: TaskListAdapter by lazy { TaskListAdapter(this) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentTaskListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initTasksRecyclerView()

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    it.taskList?.let { pagingData ->
                        adapter.submitData(pagingData)
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.updateTask.collect {
                    updatePage()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                fetchTasks()
            }
        }

        binding.create.setOnClickListener {
            navigateToCreateTaskPage()
        }

        binding.refresh.setOnRefreshListener {
            updatePage()
        }

        binding.configurationSwitch.isChecked = CurrentCacheStrategy.strategy == DatabaseStrategy.ROOM

        binding.configuration.setOnClickListener {
            binding.configurationSwitch.isChecked = !binding.configurationSwitch.isChecked
        }

        binding.configurationSwitch.setOnCheckedChangeListener { _, isChecked ->
            changeCacheConfiguration(isChecked)
        }

        binding.taskList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                (binding.taskList.layoutManager as? LinearLayoutManager)?.findFirstCompletelyVisibleItemPosition()?.let {
                    val isFirstTaskCompletelyVisible = it == 0
                    val isListEmpty = recyclerView.adapter?.itemCount == 0
                    showGoToTop(!(isFirstTaskCompletelyVisible || isListEmpty))
                }
            }
        })

        binding.goToTop.setOnClickListener {
            scrollToTop()
        }
    }

    override fun fetchTasks() {
        viewModel.fetchTasks(viewModel.viewModelScope)
    }

    override fun changeCacheConfiguration(isRoom: Boolean) {
        CurrentCacheStrategy.strategy = if (isRoom) DatabaseStrategy.ROOM else DatabaseStrategy.SQLITE
        Handler(Looper.getMainLooper()).postDelayed({
            adapter.refresh()
        }, 200)
    }

    override fun scrollToTop() {
        binding.taskList.smoothScrollToPosition(0)
    }

    override fun navigateToCreateTaskPage() {
        (activity as? HomeActivity)?.navigateToCreateTaskPage()
    }

    override fun showGoToTop(visible: Boolean) {
        if (visible) {
            binding.goToTop.visibility = View.VISIBLE
        } else {
            binding.goToTop.visibility = View.GONE
        }
    }

    override fun initTasksRecyclerView() {
        binding.taskList.layoutManager = LinearLayoutManager(requireContext())
        binding.taskList.adapter = adapter
        binding.taskList.itemAnimator = null
    }

    override fun updatePage() {
        adapter.refresh()
        if (binding.refresh.isRefreshing) binding.refresh.isRefreshing = false
    }

    override fun updateTask(task: Task) {
        viewModel.updateTask(task)
    }

    override fun openTask(id: Int) {
        (activity as? HomeActivity)?.navigateToTaskDetailsPage(id)
    }

    companion object {
        @JvmStatic
        fun newInstance(): TaskListFragment = TaskListFragment()
    }
}