package com.sample.app.todolist.todo.ui.home

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.sample.app.todolist.databinding.ActivityHomeBinding
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
            add(binding.host.id, TodoListFragment.newInstance(), TodoListFragment.Companion::class.java.simpleName)
        }.commit()
    }
}