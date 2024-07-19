package com.sample.app.todolist.todo

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TodoApp: Application() {
    override fun onCreate() {
        super.onCreate()
    }
}