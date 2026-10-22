
package com.example.inventarioti.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.inventarioti.model.Producto

@Composable
fun PantallaMovimiento(
    productos: List<Producto>,
    guardarMovimiento: (String, String, Int) -> String,
    volverMenu: () -> Unit
) {

    val morado = Color(0xFF63398F)
    val moradoBoton = Color(0xFF8159B2)
    val lavanda = Color(0xFFE3D6F2)
    val fondo = Color(0xFFFAF7FF)

    var sku by remember {
        mutableStateOf(productos.firstOrNull()?.sku ?: "")
    }

    var tipo by remember {
        mutableStateOf("Entrada")
    }

    var cantidad by remember {
        mutableStateOf("")
    }

    var mensaje by remember {
        mutableStateOf("")
    }

    var expandido by remember {
        mutableStateOf(false)
    }

    val productoSeleccionado = productos.find {
        it.sku == sku
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(fondo)
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {

        Text(
            text = "Registrar movimiento",
            style = MaterialTheme.typography.headlineSmall,
            color = morado
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Seleccionar producto",
            color = morado
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box {

            OutlinedButton(
                onClick = {
                    expandido = true
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, Color(0xFFC6B0E1)),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = morado,
                    containerColor = Color.White
                )
            ) {
                Text(
                    text = productoSeleccionado?.nombre ?: "Producto"
                )
            }

            DropdownMenu(
                expanded = expandido,
                onDismissRequest = {
                    expandido = false
                }
            ) {

                productos.forEach { producto ->

                    DropdownMenuItem(
                        text = {
                            Text(
                                text = producto.nombre,
                                color = morado
                            )
                        },
                        onClick = {
                            sku = producto.sku
                            expandido = false
                            mensaje = ""
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Tipo de movimiento",
            color = morado
        )

        Row {

            RadioButton(
                selected = tipo == "Entrada",
                onClick = {
                    tipo = "Entrada"
                },
                colors = RadioButtonDefaults.colors(
                    selectedColor = moradoBoton,
                    unselectedColor = Color(0xFF9D83BC)
                )
            )

            Text(
                text = "Entrada",
                color = morado,
                modifier = Modifier.padding(top = 12.dp)
            )

            RadioButton(
                selected = tipo == "Salida",
                onClick = {
                    tipo = "Salida"
                },
                colors = RadioButtonDefaults.colors(
                    selectedColor = moradoBoton,
                    unselectedColor = Color(0xFF9D83BC)
                )
            )

            Text(
                text = "Salida",
                color = morado,
                modifier = Modifier.padding(top = 12.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = cantidad,
            onValueChange = {
                cantidad = it
                mensaje = ""
            },
            label = {
                Text("Cantidad")
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = moradoBoton,
                unfocusedBorderColor = Color(0xFFC6B0E1),
                focusedLabelColor = morado,
                unfocusedLabelColor = morado,
                cursorColor = moradoBoton,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        if (mensaje.isNotEmpty()) {

            Text(
                text = mensaje,
                color = if (mensaje == "Movimiento registrado") {
                    morado
                } else {
                    MaterialTheme.colorScheme.error
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {

                val numero = cantidad.toIntOrNull()

                if (numero == null) {
                    mensaje = "Ingresa una cantidad valida"
                } else {

                    mensaje = guardarMovimiento(sku, tipo, numero)

                    if (mensaje == "Movimiento registrado") {
                        cantidad = ""
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = moradoBoton,
                contentColor = Color.White
            )
        ) {
            Text("Guardar movimiento")
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = volverMenu,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, Color(0xFFC6B0E1)),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = lavanda,
                contentColor = morado
            )
        ) {
            Text("Volver al menu")
        }
    }
}
