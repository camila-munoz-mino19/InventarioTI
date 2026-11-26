
package com.example.inventarioti.screens

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.example.inventarioti.ui.theme.BordeLavanda
import com.example.inventarioti.ui.theme.FondoClaro
import com.example.inventarioti.ui.theme.Lavanda
import com.example.inventarioti.ui.theme.LavandaSuave
import com.example.inventarioti.ui.theme.MoradoBoton
import com.example.inventarioti.ui.theme.MoradoPrincipal
import com.example.inventarioti.ui.theme.TextoMorado

@Composable
fun PantallaCamara(volverMenu: () -> Unit) {

    var foto by remember {
        mutableStateOf<Bitmap?>(null)
    }

    val camara = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { resultado ->
        if (resultado != null) {
            foto = resultado
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoClaro)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Cámara de productos",
            style = MaterialTheme.typography.headlineSmall,
            color = MoradoPrincipal
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Captura una fotografía de un producto",
            style = MaterialTheme.typography.bodyMedium,
            color = TextoMorado
        )

        Spacer(modifier = Modifier.height(24.dp))

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
                    text = "Fotografía del producto",
                    style = MaterialTheme.typography.titleMedium,
                    color = MoradoPrincipal
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Utiliza la cámara para tomar una foto y visualizarla en la aplicación.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextoMorado
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                camara.launch(null)
            },
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
                text = if (foto == null) {
                    "Tomar foto"
                } else {
                    "Tomar otra foto"
                },
                style = MaterialTheme.typography.labelLarge
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (foto == null) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Lavanda
                )
            ) {

                Text(
                    text = "Todavía no has tomado una fotografía",
                    modifier = Modifier.padding(20.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextoMorado
                )
            }
        }

        AnimatedVisibility(
            visible = foto != null,
            enter = fadeIn(
                animationSpec = tween(450)
            ) + expandVertically(
                animationSpec = tween(450)
            )
        ) {

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    text = "Vista previa",
                    style = MaterialTheme.typography.titleMedium,
                    color = MoradoPrincipal
                )

                Spacer(modifier = Modifier.height(12.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {

                    foto?.let { imagen ->

                        Image(
                            bitmap = imagen.asImageBitmap(),
                            contentDescription = "Foto del producto",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp)
                                .padding(12.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Fotografía capturada correctamente",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MoradoPrincipal
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedButton(
            onClick = volverMenu,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                BordeLavanda
            ),
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
