
package com.example.inventarioti.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.inventarioti.model.Producto

@Composable
fun PantallaAlertas(productos: List<Producto>) {

    val productosBajos = productos.filter {
        it.cantidad <= it.stockMinimo
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        Text(
            text = "Alertas de stock",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Productos con stock bajo: ${productosBajos.size}"
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (productosBajos.isEmpty()) {

            Text("No hay productos con stock bajo")

        } else {

            productosBajos.forEach { producto ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        Text(
                            text = producto.nombre,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Text("SKU: ${producto.sku}")

                        Text("Stock actual: ${producto.cantidad}")

                        Text("Stock minimo: ${producto.stockMinimo}")

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Reponer producto",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}
