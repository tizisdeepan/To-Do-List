package com.sample.app.todolist.todo.util

import com.sample.app.todolist.todo.common.DispatcherProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext

@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(private val dispatcher: DispatcherProvider = TestDispatcherProvider(), private val testScope: TestScope = TestScope(dispatcher.default)) : BeforeEachCallback,
    AfterEachCallback, BeforeAllCallback, AfterAllCallback {

    fun getDispatcherProvider(): DispatcherProvider = dispatcher
    fun getTestScope(): TestScope = testScope

    override fun beforeEach(p0: ExtensionContext?) {
        Dispatchers.setMain(dispatcher.default)
    }

    override fun afterEach(p0: ExtensionContext?) {
        Dispatchers.resetMain()
    }

    override fun beforeAll(p0: ExtensionContext?) {
        Dispatchers.setMain(dispatcher.default)
    }

    override fun afterAll(p0: ExtensionContext?) {
        Dispatchers.resetMain()
    }
}