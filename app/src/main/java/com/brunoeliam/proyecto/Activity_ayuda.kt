package com.brunoeliam.proyecto

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class Activity_ayuda : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ayuda)
    }
    fun volver (v: View) {
        this.finish()
    }
}