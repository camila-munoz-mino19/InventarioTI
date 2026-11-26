
package com.example.inventarioti.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.inventarioti.model.Producto
import com.example.inventarioti.ui.theme.BordeLavanda
import com.example.inventarioti.ui.theme.FondoClaro
import com.example.inventarioti.ui.theme.Lavanda
import com.example.inventarioti.ui.theme.LavandaSuave
import com.example.inventarioti.ui.theme.MoradoPrincipal
import com.example.inventarioti.ui.theme.TextoMorado

@Composable
fun PantallaProductos(productos: List<Producto>) {

    val totalProductos = productos.size

    val productosBajos = productos.count {
        it.cantidad <= it.stockMinimo
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoClaro)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {

        Text(
            text = "Productos del inventario",
            style = MaterialTheme.typography.headlineSmall,
            color = MoradoPrincipal
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Consulta los productos y sus cantidades disponibles",
            style = MaterialTheme.typography.bodyMedium,
            color = TextoMorado
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Lavanda
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Productos",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextoMorado
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "$totalProductos",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MoradoPrincipal,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = LavandaSuave
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Stock bajo",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextoMorado
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "$productosBajos",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MoradoPrincipal,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (productos.isEmpty()) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = LavandaSuave
                )
            ) {
                Text(
                    text = "No hay productos registrados",
                    modifier = Modifier.padding(20.dp),
                    color = MoradoPrincipal,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

        } else {

            productos.forEach { producto ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 14.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 2.dp
                    )
                ) {

                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {

                        Text(
                            text = producto.nombre,
                            style = MaterialTheme.typography.titleLarge,
                            color = MoradoPrincipal
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "SKU: ${producto.sku}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextoMorado
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        HorizontalDivider(
                            color = BordeLavanda
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "Marca: ${producto.marca}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextoMorado
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Categoría: ${producto.categoria}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextoMorado
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = LavandaSuave
                            )
                        ) {

                            Column(
                                modifier = Modifier.padding(14.dp)
                            ) {

                                Text(
                                    text = "Stock disponible: ${producto.cantidad}",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MoradoPrincipal
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = "Stock mínimo: ${producto.stockMinimo}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextoMorado
                                )
                            }
                        }

                        if (producto.cantidad <= producto.stockMinimo) {

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Stock bajo",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }
}
