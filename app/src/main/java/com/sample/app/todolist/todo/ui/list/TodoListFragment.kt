package com.sample.app.todolist.todo.ui.list

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.sample.app.todolist.R
import com.sample.app.todolist.databinding.FragmentTodoListBinding
import com.sample.app.todolist.todo.ui.home.HomeActivity
import com.sample.app.todolist.todo.ui.list.adapter.TodoListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TodoListFragment : Fragment() {

    private lateinit var binding: FragmentTodoListBinding
    private val viewModel: TodoListViewModel by viewModels()

    private val adapter: TodoListAdapter by lazy { TodoListAdapter() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentTodoListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.todoList.layoutManager = LinearLayoutManager(requireContext())
        binding.todoList.adapter = adapter
        binding.todoList.itemAnimator = null

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.uiState.collectLatest {
                    it.todoList?.let { pagingData ->
                        adapter.submitData(pagingData)
                        Log.e("ITEMS IN UI", adapter.snapshot().items.toString())
                    }
                }
            }
        }

        binding.create.setOnClickListener {
            (activity as? HomeActivity)?.navigateToCreateTodoPage()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(): TodoListFragment = TodoListFragment()
    }
}