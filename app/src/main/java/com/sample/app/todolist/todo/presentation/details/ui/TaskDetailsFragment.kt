package com.sample.app.todolist.todo.presentation.details.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.sample.app.todolist.R
import com.sample.app.todolist.databinding.FragmentTaskDetailsBinding
import com.sample.app.todolist.todo.common.toPrettyDate
import com.sample.app.todolist.todo.data.model.Task
import com.sample.app.todolist.todo.presentation.details.TaskDetailsViewModel
import com.sample.app.todolist.todo.presentation.home.ui.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TaskDetailsFragment : Fragment(), TaskDetailsContract {

    private lateinit var binding: FragmentTaskDetailsBinding
    private val viewModel: TaskDetailsViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentTaskDetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setNavigation()

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.uiState.collectLatest { state ->
                    state.task?.let {
                        renderTask(it)
                    }

                    state.isDeleted.consume()?.let {
                        updateTasksInHomePage()
                        navigateBack()
                    }

                    state.isUpdated.consume()?.let {
                        updateTasksInHomePage()
                    }
                }
            }
        }

        binding.delete.setOnClickListener {
            deleteTask()
        }

        binding.completed.setOnCheckedChangeListener { _, isChecked ->
            updateTask(isChecked)
        }
    }

    override fun renderTask(task: Task) {
        binding.title.text = task.title
        binding.createdOn.text = task.createdOn.toPrettyDate()
        binding.completed.isChecked = task.completed
    }

    override fun updateTasksInHomePage() {
        (activity as? HomeActivity)?.updateTaskList()
    }

    override fun deleteTask() {
        viewModel.deleteTask()
    }

    override fun updateTask(completed: Boolean) {
        with(viewModel.uiState.value) {
            if (task != null) viewModel.updateTask(task, completed)
        }
    }

    override fun navigateBack() {
        (activity as? HomeActivity)?.onBackPressedDispatcher?.onBackPressed()
    }

    override fun setNavigation() {
        binding.toolbar.navigationIcon = ResourcesCompat.getDrawable(resources, R.drawable.baseline_arrow_back_24, requireContext().theme)
        binding.toolbar.setNavigationOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(id: Int): TaskDetailsFragment = TaskDetailsFragment().apply {
            arguments = Bundle().apply {
                putInt("ID", id)
            }
        }
    }
}