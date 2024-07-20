package com.sample.app.todolist.todo.ui.details

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
import com.sample.app.todolist.todo.ui.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TaskDetailsFragment : Fragment() {

    private lateinit var binding: FragmentTaskDetailsBinding
    private val viewModel: TaskDetailsViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentTaskDetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setNavigation()

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.uiState.collectLatest { state ->
                    if (state.task != null) {
                        binding.title.text = state.task.title
                        binding.createdOn.text = state.task.createdOn.toPrettyDate()
                        binding.completed.isChecked = state.task.completed
                    }

                    state.isDeleted.consume()?.let {
                        (activity as? HomeActivity)?.updateTaskList()
                        (activity as? HomeActivity)?.onBackPressedDispatcher?.onBackPressed()
                    }

                    state.isUpdated.consume()?.let {
                        (activity as? HomeActivity)?.updateTaskList()
                    }
                }
            }
        }

        binding.delete.setOnClickListener {
            viewModel.deleteTodoItem()
        }

        binding.completed.setOnCheckedChangeListener { _, isChecked ->
            viewModel.updateTask(isChecked)
        }
    }

    private fun setNavigation() {
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