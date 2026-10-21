
package com.example.inventarioti.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.inventarioti.model.Producto

@Composable
fun PantallaMovimiento(
    productos: List<Producto>,
    guardarMovimiento: (String, String, Int) -> String,
    volverMenu: () -> Unit
) {

    var sku by remember { mutableStateOf(productos.first().sku) }
    var tipo by remember { mutableStateOf("Entrada") }
    var cantidad by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }
    var expandido by remember { mutableStateOf(false) }

    val productoSeleccionado = productos.find { it.sku == sku }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        Text(
            text = "Registrar movimiento",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text("Seleccionar producto")

        Box {

            OutlinedButton(
                onClick = { expandido = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(productoSeleccionado?.nombre ?: "Producto")
            }

            DropdownMenu(
                expanded = expandido,
                onDismissRequest = { expandido = false }
            ) {

                productos.forEach { producto ->

                    DropdownMenuItem(
                        text = { Text(producto.nombre) },
                        onClick = {
                            sku = producto.sku
                            expandido = false
                            mensaje = ""
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Tipo de movimiento")

        Row {

            RadioButton(
                selected = tipo == "Entrada",
                onClick = { tipo = "Entrada" }
            )

            Text(
                text = "Entrada",
                modifier = Modifier.padding(top = 12.dp)
            )

            RadioButton(
                selected = tipo == "Salida",
                onClick = { tipo = "Salida" }
            )

            Text(
                text = "Salida",
                modifier = Modifier.padding(top = 12.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = cantidad,
            onValueChange = {
                cantidad = it
                mensaje = ""
            },
            label = { Text("Cantidad") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (mensaje.isNotEmpty()) {

            Text(
                text = mensaje,
                color = if (mensaje == "Movimiento registrado")
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.error
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
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar movimiento")
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = volverMenu,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver al menu")
        }
    }
}
