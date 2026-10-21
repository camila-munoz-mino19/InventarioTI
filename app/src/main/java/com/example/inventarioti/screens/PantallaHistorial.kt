
package com.example.inventarioti.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.inventarioti.model.Movimiento

@Composable
fun PantallaHistorial(movimientos: List<Movimiento>) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        Text(
            text = "Historial de movimientos",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (movimientos.isEmpty()) {

            Text("No hay movimientos registrados")

        } else {

            movimientos.asReversed().forEach { movimiento ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        Text(
                            text = movimiento.producto,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Text("SKU: ${movimiento.sku}")
                        Text("Tipo: ${movimiento.tipo}")
                        Text("Cantidad: ${movimiento.cantidad}")
                        Text("Fecha: ${movimiento.fecha}")
                        Text("Usuario: ${movimiento.usuario}")
                    }
                }
            }
        }
    }
}
