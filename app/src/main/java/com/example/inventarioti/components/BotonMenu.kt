
package com.example.inventarioti.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.inventarioti.ui.theme.Lavanda
import com.example.inventarioti.ui.theme.MoradoBoton
import com.example.inventarioti.ui.theme.TextoMorado

@Composable
fun BotonMenu(
    texto: String,
    onClick: () -> Unit,
    destacado: Boolean = false
) {
    val colorBoton = if (destacado) {
        MoradoBoton
    } else {
        Lavanda
    }

    val colorTexto = if (destacado) {
        Color.White
    } else {
        TextoMorado
    }

    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorBoton,
            contentColor = colorTexto
        )
    ) {
        Text(
            text = texto,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold
        )
    }
}
