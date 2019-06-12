package com.app.avy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // finally change the color
        setContentView(getId())
        onViewReady()
    }

    abstract fun getId(): Int

    abstract fun onViewReady()

}