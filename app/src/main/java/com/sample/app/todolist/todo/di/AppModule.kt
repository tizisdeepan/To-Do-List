package com.sample.app.todolist.todo.di

import com.sample.app.todolist.todo.common.DefaultDispatcherProvider
import com.sample.app.todolist.todo.common.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideDefaultDispatcherProvider(): DispatcherProvider {
        return DefaultDispatcherProvider()
    }
}