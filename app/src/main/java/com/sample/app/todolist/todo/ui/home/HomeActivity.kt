package com.sample.app.todolist.todo.ui.home

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.sample.app.todolist.R
import com.sample.app.todolist.databinding.ActivityHomeBinding
import com.sample.app.todolist.todo.data.model.Todo
import com.sample.app.todolist.todo.ui.create.CreateTodoFragment
import com.sample.app.todolist.todo.ui.details.TodoDetailsFragment
import com.sample.app.todolist.todo.ui.list.TodoListFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val viewModel: HomeViewModel by viewModels()

    private val pleaseWaitDialog: PleaseWaitDialog by lazy { PleaseWaitDialog(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction().apply {
            setReorderingAllowed(true)
            add(binding.parentHost.id, TodoListFragment.newInstance())
        }.commit()

        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.createEntries -> {
                    viewModel.createTestEntries()
                }

                R.id.clearEntries -> {
                    viewModel.clearAllEntries()
                }
            }
            true
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.uiState.collectLatest { state ->
                    if (state.isLoading) {
                        pleaseWaitDialog.show()
                    }
                    if (state.areEntriesAdded.consume() == true || state.areEntriesCleared.consume() == true) {
                        pleaseWaitDialog.dismiss()
                        refreshTodoList()
                    }
                }
            }
        }
    }

    fun refreshTodoList() {
        (supportFragmentManager.fragments.firstOrNull { it is TodoListFragment } as? TodoListFragment)?.refreshPage()
    }

    fun navigateToCreateTodoPage() {
        supportFragmentManager.beginTransaction().apply {
            setReorderingAllowed(true)
            add(binding.childHost.id, CreateTodoFragment.newInstance(), CreateTodoFragment.Companion::class.java.simpleName)
            addToBackStack(CreateTodoFragment.Companion::class.java.simpleName)
        }.commit()
    }

    fun navigateToTodoDetailsPage(id: Long) {
        supportFragmentManager.beginTransaction().apply {
            setReorderingAllowed(true)
            add(binding.childHost.id, TodoDetailsFragment.newInstance(id), TodoDetailsFragment.Companion::class.java.simpleName)
            addToBackStack(TodoDetailsFragment.Companion::class.java.simpleName)
        }.commit()
    }
}