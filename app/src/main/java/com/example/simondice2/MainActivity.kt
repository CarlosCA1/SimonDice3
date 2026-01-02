package com.example.simondice2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        val miViewModel: MyViewModel =
            ViewModelProvider(this)[MyViewModel::class.java]

        setContent {
            IU(miViewModel)
        }
    }
}
