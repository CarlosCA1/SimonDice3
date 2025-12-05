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

        // Obtenemos el ViewModel que hereda AndroidViewModel usando ViewModelProvider.
        val miViewModel: MyViewModel = ViewModelProvider(this).get(MyViewModel::class.java)

        setContent {
            IU(miViewModel)
        }
    }
}
