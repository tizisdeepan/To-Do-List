package com.sample.app.todolist.todo.ui.home

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import com.sample.app.todolist.R

class PleaseWaitDialog(ctx: Context) : Dialog(ctx) {

    var shouldShow = true
    override fun onStart() {
        super.onStart()
        window?.setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_please_wait)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        setCancelable(false)
        setCanceledOnTouchOutside(false)
    }
}