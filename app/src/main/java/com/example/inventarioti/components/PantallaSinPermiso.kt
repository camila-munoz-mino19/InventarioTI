
package com.example.inventarioti.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.inventarioti.ui.theme.FondoClaro
import com.example.inventarioti.ui.theme.MoradoBoton
import com.example.inventarioti.ui.theme.MoradoPrincipal

@Composable
fun PantallaSinPermiso(
    volverMenu: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoClaro)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "No tienes permiso para acceder",
            style = MaterialTheme.typography.titleMedium,
            color = MoradoPrincipal
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = volverMenu,
            colors = ButtonDefaults.buttonColors(
                containerColor = MoradoBoton
            )
        ) {
            Text("Volver al menú")
        }
    }
}
