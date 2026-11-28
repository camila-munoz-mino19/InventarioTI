
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
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

    var clave by remember {
        mutableStateOf("")
    }

    var productos by remember {
        mutableStateOf<List<ProductoApi>>(emptyList())
    }

    var mensaje by remember {
        mutableStateOf("Ingresa tu clave para consultar AWS")
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
            text = "Productos desde AWS",
            style = MaterialTheme.typography.headlineSmall,
            color = MoradoPrincipal
        )

        Text(
            text = "Consulta de productos almacenados en Amazon DynamoDB",
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
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {

                Text(
                    text = "Conexión con AWS",
                    style = MaterialTheme.typography.titleMedium,
                    color = MoradoPrincipal
                )

                Text(
                    text = "Consulta el inventario almacenado en la nube mediante nuestra API REST.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextoMorado
                )

                OutlinedTextField(
                    value = clave,
                    onValueChange = {
                        clave = it
                    },
                    label = {
                        Text("Clave de acceso AWS")
                    },
                    placeholder = {
                        Text("Ingresa tu clave")
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MoradoPrincipal,
                        focusedLabelColor = MoradoPrincipal
                    )
                )

                Text(
                    text = "La clave se utiliza para realizar la consulta y no se guarda en el teléfono.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextoMorado
                )

                Button(
                    onClick = {

                        if (clave.isBlank()) {

                            mensaje = "Debes ingresar la clave de acceso"

                        } else {

                            cargando = true
                            mensaje = "Consultando productos en AWS..."
                            productos = emptyList()

                            ApiProductos.consultarProductos(
                                clave = clave
                            ) { lista, error ->

                                cargando = false
                                productos = lista

                                mensaje = when {
                                    error != null -> error
                                    lista.isEmpty() -> "No se encontraron productos en AWS"
                                    else -> "Productos encontrados: ${lista.size}"
                                }
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
                            "Consultando AWS..."
                        } else {
                            "Consultar productos AWS"
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
                text = "Productos almacenados en AWS",
                style = MaterialTheme.typography.titleLarge,
                color = MoradoPrincipal
            )
        }

        productos.forEach { producto ->
            TarjetaProductoApi(producto)
        }

        Spacer(
            modifier = Modifier.height(8.dp)
        )

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

                Spacer(
                    modifier = Modifier.height(14.dp)
                )

                HorizontalDivider(
                    color = BordeLavanda
                )

                Spacer(
                    modifier = Modifier.height(14.dp)
                )

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
