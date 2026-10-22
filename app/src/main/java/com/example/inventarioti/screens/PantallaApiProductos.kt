
package com.example.inventarioti.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.inventarioti.data.ApiProductos
import com.example.inventarioti.data.ProductoApi

@Composable
fun PantallaApiProductos(onVolver: () -> Unit) {

    var productos by remember {
        mutableStateOf<List<ProductoApi>>(emptyList())
    }

    var mensaje by remember {
        mutableStateOf("Presiona el boton para consultar productos")
    }

    var cargando by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F2FC))
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text(
            text = "Productos desde Internet",
            fontSize = 24.sp,
            color = Color(0xFF59357A)
        )

        Text(
            text = "Consulta de productos utilizando una API REST",
            color = Color.DarkGray
        )

        Spacer(modifier = Modifier.height(8.dp))

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
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF8159B2)
            )
        ) {
            Text("Consultar API")
        }

        if (cargando) {
            CircularProgressIndicator(
                color = Color(0xFF8159B2)
            )
        }

        Text(
            text = mensaje,
            color = Color(0xFF59357A)
        )

        productos.forEach { producto ->
            TarjetaProductoApi(producto)
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onVolver,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF9D83BC)
            )
        ) {
            Text("Volver al menu")
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
            animationSpec = tween(600)
        ) + slideInVertically(
            animationSpec = tween(600),
            initialOffsetY = { it / 2 }
        )
    ) {

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {

            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Text(
                    text = producto.nombre,
                    fontSize = 18.sp,
                    color = Color(0xFF59357A)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Stock disponible: ${producto.stock}",
                    color = Color.DarkGray
                )
            }
        }
    }
}
