
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
fun PantallaProductos(productos: List<Producto>) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        Text(
            text = "Productos del inventario",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        productos.forEach { producto ->

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
                    Text("Marca: ${producto.marca}")
                    Text("Categoria: ${producto.categoria}")
                    Text("Stock disponible: ${producto.cantidad}")
                    Text("Stock minimo: ${producto.stockMinimo}")

                    if (producto.cantidad <= producto.stockMinimo) {

                        Text(
                            text = "Stock bajo",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}
