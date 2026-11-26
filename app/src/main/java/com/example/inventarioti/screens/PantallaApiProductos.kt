
package com.example.inventarioti.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.inventarioti.data.ApiProductos
import com.example.inventarioti.data.ProductoApi
import com.example.inventarioti.ui.theme.BordeLavanda
import com.example.inventarioti.ui.theme.FondoClaro
import com.example.inventarioti.ui.theme.Lavanda
import com.example.inventarioti.ui.theme.LavandaSuave
import com.example.inventarioti.ui.theme.MoradoBoton
import com.example.inventarioti.ui.theme.MoradoPrincipal
import com.example.inventarioti.ui.theme.TextoMorado

@Composable
fun PantallaApiProductos(onVolver: () -> Unit) {

    var productos by remember {
        mutableStateOf<List<ProductoApi>>(emptyList())
    }

    var mensaje by remember {
        mutableStateOf("Presiona el botón para consultar productos")
    }

    var cargando by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoClaro)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "Productos desde Internet",
            style = MaterialTheme.typography.headlineSmall,
            color = MoradoPrincipal
        )

        Text(
            text = "Consulta de productos utilizando una API REST",
            style = MaterialTheme.typography.bodyMedium,
            color = TextoMorado
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = LavandaSuave
            )
        ) {

            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                Text(
                    text = "Consulta en línea",
                    style = MaterialTheme.typography.titleMedium,
                    color = MoradoPrincipal
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Obtén productos y consulta su stock desde un servicio externo.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextoMorado
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        cargando = true
                        mensaje = "Consultando productos..."
                        productos = emptyList()

                        ApiProductos.consultarProductos { lista, error ->

                            cargando = false
                            productos = lista

                            mensaje = when {
                                error != null -> error
                                lista.isEmpty() -> "No se encontraron productos"
                                else -> "Productos encontrados: ${lista.size}"
                            }
                        }
                    },
                    enabled = !cargando,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MoradoBoton,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = if (cargando) {
                            "Consultando..."
                        } else {
                            "Consultar API"
                        },
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }

        if (cargando) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(
                    color = MoradoBoton
                )
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Lavanda
            )
        ) {

            Text(
                text = mensaje,
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = TextoMorado
            )
        }

        if (productos.isNotEmpty()) {

            Text(
                text = "Resultados de la consulta",
                style = MaterialTheme.typography.titleLarge,
                color = MoradoPrincipal
            )
        }

        productos.forEach { producto ->
            TarjetaProductoApi(producto)
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = onVolver,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, BordeLavanda),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Lavanda,
                contentColor = MoradoPrincipal
            )
        ) {
            Text(
                text = "Volver al menú",
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Composable
fun TarjetaProductoApi(producto: ProductoApi) {

    var mostrar by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        mostrar = true
    }

    AnimatedVisibility(
        visible = mostrar,
        enter = fadeIn(
            animationSpec = tween(450)
        ) + slideInVertically(
            initialOffsetY = { it / 6 },
            animationSpec = tween(450)
        )
    ) {

        Card(
            modifier = Modifier.fillMaxWidth(),
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

                Spacer(modifier = Modifier.height(14.dp))

                HorizontalDivider(
                    color = BordeLavanda
                )

                Spacer(modifier = Modifier.height(14.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = LavandaSuave
                    )
                ) {

                    Text(
                        text = "Stock disponible: ${producto.stock}",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MoradoPrincipal
                    )
                }
            }
        }
    }
}
