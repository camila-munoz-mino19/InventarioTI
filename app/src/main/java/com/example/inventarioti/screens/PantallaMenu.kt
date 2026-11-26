
package com.example.inventarioti.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.inventarioti.components.BotonMenu
import com.example.inventarioti.ui.theme.BordeLavanda
import com.example.inventarioti.ui.theme.FondoClaro
import com.example.inventarioti.ui.theme.LavandaSuave
import com.example.inventarioti.ui.theme.MoradoPrincipal
import com.example.inventarioti.ui.theme.TextoMorado

@Composable
fun PantallaMenu(
    rol: String,
    puedeRegistrar: Boolean,
    puedeVerHistorial: Boolean,
    puedeVerAlertas: Boolean,
    mensajePermiso: String,
    verProductos: () -> Unit,
    verApiProductos: () -> Unit,
    verMovimientos: () -> Unit,
    verHistorial: () -> Unit,
    verAlertas: () -> Unit,
    verCamara: () -> Unit,
    activarNotificaciones: () -> Unit,
    cerrarSesion: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoClaro)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = LavandaSuave
            )
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = "Inventario TI",
                    style = MaterialTheme.typography.headlineLarge,
                    fontSize = 34.sp,
                    color = MoradoPrincipal
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Control de productos tecnológicos",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextoMorado
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Rol: $rol",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = MoradoPrincipal
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = "Panel de control",
            style = MaterialTheme.typography.titleLarge,
            color = MoradoPrincipal
        )

        Spacer(modifier = Modifier.height(20.dp))

        BotonMenu(
            texto = "Ver productos",
            onClick = verProductos,
            destacado = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        BotonMenu(
            texto = "Productos en línea",
            onClick = verApiProductos
        )

        if (puedeRegistrar) {
            Spacer(modifier = Modifier.height(12.dp))

            BotonMenu(
                texto = "Registrar movimiento",
                onClick = verMovimientos
            )
        }

        if (puedeVerHistorial) {
            Spacer(modifier = Modifier.height(12.dp))

            BotonMenu(
                texto = "Ver historial",
                onClick = verHistorial
            )
        }

        if (puedeVerAlertas) {
            Spacer(modifier = Modifier.height(12.dp))

            BotonMenu(
                texto = "Ver alertas",
                onClick = verAlertas
            )

            Spacer(modifier = Modifier.height(12.dp))

            BotonMenu(
                texto = "Activar notificaciones",
                onClick = activarNotificaciones
            )
        }

        if (puedeRegistrar) {
            Spacer(modifier = Modifier.height(12.dp))

            BotonMenu(
                texto = "Cámara de productos",
                onClick = verCamara
            )
        }

        if (mensajePermiso.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = mensajePermiso,
                style = MaterialTheme.typography.bodyMedium,
                color = MoradoPrincipal
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedButton(
            onClick = cerrarSesion,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, BordeLavanda),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MoradoPrincipal
            )
        ) {
            Text("Cerrar sesión")
        }
    }
}
