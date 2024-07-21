package com.sample.app.todolist.todo.presentation.home.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.sample.app.todolist.R
import com.sample.app.todolist.databinding.ActivityHomeBinding
import com.sample.app.todolist.todo.presentation.create.ui.CreateTaskFragment
import com.sample.app.todolist.todo.presentation.details.ui.TaskDetailsFragment
import com.sample.app.todolist.todo.presentation.home.HomeViewModel
import com.sample.app.todolist.todo.presentation.list.ui.TaskListFragment
import com.sample.app.todolist.todo.presentation.ui.PerformanceStatsDialog
import com.sample.app.todolist.todo.presentation.ui.PleaseWaitDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeActivity : AppCompatActivity(), HomeContract {

    private lateinit var binding: ActivityHomeBinding
    private val viewModel: HomeViewModel by viewModels()

    private val pleaseWaitDialog: PleaseWaitDialog by lazy { PleaseWaitDialog(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        hostTasksPage()

        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.createEntries -> {
                    createTasksForTesting()
                }

                R.id.clearEntries -> {
                    clearAllTasks()
                }

                R.id.monitorPerformance -> {
                    monitorPerformance()
                }
            }
            true
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.uiState.collect { state ->
                    if (state.isLoading) {
                        if (!pleaseWaitDialog.isShowing) pleaseWaitDialog.show()
                    } else {
                        if (pleaseWaitDialog.isShowing) pleaseWaitDialog.dismiss()
                    }

                    if (state.areEntriesAdded.consume() == true || state.areEntriesCleared.consume() == true) {
                        pleaseWaitDialog.dismiss()
                        updateTaskList()
                    }

                    state.databasePerformance?.let { databasePerformance ->
                        PerformanceStatsDialog(this@HomeActivity, databasePerformance).show()
                    }
                }
            }
        }
    }

    override fun hostTasksPage() {
        supportFragmentManager.beginTransaction().apply {
            setReorderingAllowed(true)
            add(binding.parentHost.id, TaskListFragment.newInstance())
        }.commit()
    }

    override fun updateTaskList() {
        (supportFragmentManager.fragments.firstOrNull { it is TaskListFragment } as? TaskListFragment)?.updatePage()
    }

    override fun navigateToCreateTaskPage() {
        supportFragmentManager.beginTransaction().apply {
            setReorderingAllowed(true)
            add(binding.childHost.id, CreateTaskFragment.newInstance(), CreateTaskFragment.Companion::class.java.simpleName)
            addToBackStack(CreateTaskFragment.Companion::class.java.simpleName)
        }.commit()
    }

    override fun navigateToTaskDetailsPage(id: Int) {
        supportFragmentManager.beginTransaction().apply {
            setReorderingAllowed(true)
            add(binding.childHost.id, TaskDetailsFragment.newInstance(id), TaskDetailsFragment.Companion::class.java.simpleName)
            addToBackStack(TaskDetailsFragment.Companion::class.java.simpleName)
        }.commit()
    }

    override fun createTasksForTesting() {
        viewModel.createTasksForTesting()
    }

    override fun clearAllTasks() {
        viewModel.clearAllTasks()
    }

    override fun monitorPerformance() {
        AlertDialog.Builder(this).setTitle(resources.getString(R.string.performance_monitoring_alert_title)).setMessage(resources.getString(R.string.performance_monitoring_alert_description)).setPositiveButton(resources.getString(R.string.performance_monitoring_alert_positive)) { dialog, _ ->
            dialog.dismiss()
            viewModel.calculateDatabasePerformance()
        }.setNegativeButton(resources.getString(R.string.performance_monitoring_alert_negative)) { dialog, _ ->
            dialog.dismiss()
        }.show()
    }
}