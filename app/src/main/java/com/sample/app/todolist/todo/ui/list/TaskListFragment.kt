package com.sample.app.todolist.todo.ui.list

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
import com.sample.app.todolist.todo.data.repository.CurrentCacheStrategy
import com.sample.app.todolist.todo.data.repository.DatabaseStrategy
import com.sample.app.todolist.todo.ui.home.HomeActivity
import com.sample.app.todolist.todo.ui.list.adapter.TaskListAdapter
import dagger.hilt.android.AndroidEntryPoint
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
                viewModel.fetchTasks(viewModel.viewModelScope)
            }
        }

        binding.create.setOnClickListener {
            (activity as? HomeActivity)?.navigateToCreateTaskPage()
        }

        binding.refresh.setOnRefreshListener {
            updatePage()
        }

        binding.configurationSwitch.isChecked = CurrentCacheStrategy.strategy == DatabaseStrategy.ROOM

        binding.configuration.setOnClickListener {
            binding.configurationSwitch.isChecked = !binding.configurationSwitch.isChecked
        }

        binding.configurationSwitch.setOnCheckedChangeListener { _, isChecked ->
            CurrentCacheStrategy.strategy = if (isChecked) DatabaseStrategy.ROOM else DatabaseStrategy.SQLITE
            Handler(Looper.getMainLooper()).postDelayed({
                adapter.refresh()
            }, 200)
        }

        binding.taskList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                (binding.taskList.layoutManager as? LinearLayoutManager)?.findFirstVisibleItemPosition()?.let {
                    if (it != 0) {
                        binding.goToTop.visibility = View.VISIBLE
                    } else {
                        binding.goToTop.visibility = View.GONE
                    }
                    if (recyclerView.adapter?.itemCount == 0) binding.goToTop.visibility = View.GONE
                }
            }
        })

        binding.goToTop.setOnClickListener {
            binding.taskList.smoothScrollToPosition(0)
        }
    }

    fun updatePage() {
        adapter.refresh()
        if (binding.refresh.isRefreshing) binding.refresh.isRefreshing = false
    }

    override fun updateTask(task: Task) {
        viewModel.updateTask(task)
    }

    override fun openTask(task: Task) {
        (activity as? HomeActivity)?.navigateToTaskDetailsPage(task.id)
    }

    companion object {
        @JvmStatic
        fun newInstance(): TaskListFragment = TaskListFragment()
    }
}