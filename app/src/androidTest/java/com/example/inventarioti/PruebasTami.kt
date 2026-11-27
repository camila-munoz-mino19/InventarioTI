package com.example.inventarioti

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PruebasTami {

    @get:Rule
    val regla = createAndroidComposeRule<MainActivity>()

    @Test
    fun inicioSesionCorrecto() {
        regla.onAllNodes(hasSetTextAction()).onFirst().performTextInput("admin")
        regla.onAllNodes(hasSetTextAction()).onLast().performTextInput("1234")
        regla.onNodeWithText("Iniciar sesión").performClick()
        regla.onNodeWithText("Panel de control").assertIsDisplayed()
    }

    @Test
    fun inicioSesionIncorrecto() {
        regla.onAllNodes(hasSetTextAction()).onFirst().performTextInput("admin")
        regla.onAllNodes(hasSetTextAction()).onLast().performTextInput("0000")
        regla.onNodeWithText("Iniciar sesión").performClick()
        regla.onNodeWithText("Iniciar sesión").assertIsDisplayed()
        regla.onNodeWithText("Panel de control").assertDoesNotExist()
    }

    @Test
    fun administradorTieneOpciones() {
        regla.onAllNodes(hasSetTextAction()).onFirst().performTextInput("admin")
        regla.onAllNodes(hasSetTextAction()).onLast().performTextInput("1234")
        regla.onNodeWithText("Iniciar sesión").performClick()
        regla.onNodeWithText("Registrar movimiento").assertExists()
        regla.onNodeWithText("Ver historial").assertExists()
        regla.onNodeWithText("Ver alertas").assertExists()
        regla.onNodeWithText("Cámara de productos").assertExists()
    }

    @Test
    fun navegarAProductosYVolver() {
        regla.onAllNodes(hasSetTextAction()).onFirst().performTextInput("admin")
        regla.onAllNodes(hasSetTextAction()).onLast().performTextInput("1234")
        regla.onNodeWithText("Iniciar sesión").performClick()
        regla.onNodeWithText("Ver productos").performClick()
        regla.onNodeWithText("Productos del inventario").assertIsDisplayed()
        regla.onNodeWithText("Volver al menú").performClick()
        regla.onNodeWithText("Panel de control").assertIsDisplayed()
    }
}

