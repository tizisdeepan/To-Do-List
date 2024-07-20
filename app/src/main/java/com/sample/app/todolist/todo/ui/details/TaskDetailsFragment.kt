package com.sample.app.todolist.todo.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
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

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.uiState.collectLatest { state ->
                    if (state.task != null) {
                        binding.title.text = state.task.title
                        binding.createdOn.text = state.task.createdOn.toPrettyDate()
                        binding.completed.isChecked = state.task.completed
                    }

                    if (state.isDeleted.consume() == true) {
                        (activity as? HomeActivity)?.onBackPressedDispatcher?.onBackPressed()
                    }
                }
            }
        }

        binding.delete.setOnClickListener {
            viewModel.deleteTodoItem()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(id: Long): TaskDetailsFragment = TaskDetailsFragment().apply {
            arguments = Bundle().apply {
                putLong("ID", id)
            }
        }
    }
}