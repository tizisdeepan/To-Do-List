package com.sample.app.todolist.todo.ui.home

import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.sample.app.todolist.databinding.ActivityHomeBinding
import com.sample.app.todolist.todo.ui.create.CreateTodoFragment
import com.sample.app.todolist.todo.ui.list.TodoListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction().apply {
            setReorderingAllowed(true)
            add(binding.parentHost.id, TodoListFragment.newInstance())
        }.commit()
    }

    fun navigateToCreateTodoPage() {
        supportFragmentManager.beginTransaction().apply {
            setReorderingAllowed(true)
            add(binding.childHost.id, CreateTodoFragment.newInstance(), CreateTodoFragment.Companion::class.java.simpleName)
            addToBackStack(CreateTodoFragment.Companion::class.java.simpleName)
        }.commit()
    }
}