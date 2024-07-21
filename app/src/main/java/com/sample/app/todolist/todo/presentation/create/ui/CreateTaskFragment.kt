package com.sample.app.todolist.todo.presentation.create.ui

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
import com.sample.app.todolist.databinding.FragmentTaskCreateBinding
import com.sample.app.todolist.todo.presentation.create.CreateTaskViewModel
import com.sample.app.todolist.todo.presentation.home.ui.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreateTaskFragment : Fragment(), CreateTaskContract {

    private lateinit var binding: FragmentTaskCreateBinding
    private val viewModel: CreateTaskViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentTaskCreateBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setNavigation()

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest {
                    if (it.isCreated) {
                        updateTasksInHomePage()
                    }
                }
            }
        }

        binding.create.setOnClickListener {
            createTask()
        }
    }

    override fun updateTasksInHomePage() {
        (activity as? HomeActivity)?.updateTaskList()
        (activity as? HomeActivity)?.onBackPressedDispatcher?.onBackPressed()
    }

    override fun createTask() {
        val title = binding.title.text?.toString()
        if (!title.isNullOrEmpty()) {
            viewModel.createTask(title)
        }
    }

    override fun setNavigation() {
        binding.toolbar.navigationIcon = ResourcesCompat.getDrawable(resources, R.drawable.baseline_arrow_back_24, requireContext().theme)
        binding.toolbar.setNavigationOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(): CreateTaskFragment = CreateTaskFragment()
    }
}