package com.sample.app.todolist.todo.ui.create

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.sample.app.todolist.R
import com.sample.app.todolist.databinding.FragmentTodoCreateBinding
import com.sample.app.todolist.todo.ui.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreateTodoFragment : Fragment() {

    private lateinit var binding: FragmentTodoCreateBinding
    private val viewModel: CreateTodoViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentTodoCreateBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.uiState.collectLatest {
                    if (it.isCreated) {
                        (activity as? HomeActivity)?.onBackPressedDispatcher?.onBackPressed()
                    }
                }
            }
        }

        binding.create.setOnClickListener {
            val title = binding.title.text?.toString()
            if (!title.isNullOrEmpty()) {
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.createTodoItem(title).collectLatest {
                        if (it.isCreated) {
                            (activity as? HomeActivity)?.onBackPressedDispatcher?.onBackPressed()
                        }
                    }
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(): CreateTodoFragment = CreateTodoFragment()
    }
}