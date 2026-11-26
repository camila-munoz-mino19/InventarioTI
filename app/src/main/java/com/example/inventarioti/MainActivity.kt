
package com.example.inventarioti

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.example.inventarioti.ui.theme.InventarioTITheme
import com.example.inventarioti.viewmodel.InventarioViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val modelo = ViewModelProvider(this)[InventarioViewModel::class.java]

        setContent {
            InventarioTITheme {
                InventarioApp(modelo)
            }
        }
    }
}
