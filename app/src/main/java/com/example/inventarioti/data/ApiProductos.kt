
package com.example.inventarioti.data

import android.os.Handler
import android.os.Looper
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL
import java.net.UnknownHostException
import java.util.UUID

data class ProductoApi(
    val nombre: String,
    val stock: Int
)

object ApiProductos {

    private val direccionApi =
        "https://4iplvxq6we3aa4lu2h4vibkqtu0btfjj.lambda-url.us-east-2.on.aws/"

    fun consultarProductos(
        clave: String = "",
        onResultado: (List<ProductoApi>, String?) -> Unit
    ) {

        Thread {

            var conexion: HttpURLConnection? = null
            val manejador = Handler(Looper.getMainLooper())

            try {

                if (clave.isBlank()) {
                    throw Exception("Debes ingresar la clave de la API")
                }

                conexion = URL(direccionApi)
                    .openConnection() as HttpURLConnection

                conexion.requestMethod = "GET"
                conexion.connectTimeout = 10000
                conexion.readTimeout = 10000

                conexion.setRequestProperty(
                    "x-api-key",
                    clave
                )

                conexion.setRequestProperty(
                    "Accept",
                    "application/json"
                )

                val codigo = conexion.responseCode

                if (codigo != 200) {

                    val mensaje = if (codigo == 401) {
                        "Clave de acceso incorrecta"
                    } else {
                        "No se pudo conectar con AWS"
                    }

                    throw Exception(mensaje)
                }

                val respuesta = conexion.inputStream
                    .bufferedReader()
                    .use {
                        it.readText()
                    }

                val lista = JSONObject(respuesta)
                    .getJSONArray("productos")

                val productos = mutableListOf<ProductoApi>()

                for (i in 0 until lista.length()) {

                    val producto = lista.getJSONObject(i)

                    productos.add(
                        ProductoApi(
                            nombre = producto.getString("nombre"),
                            stock = producto.getInt("cantidad")
                        )
                    )
                }

                manejador.post {
                    onResultado(productos, null)
                }

            } catch (e: Exception) {

                manejador.post {
                    onResultado(
                        emptyList(),
                        e.message ?: "No se pudo consultar la API"
                    )
                }

            } finally {
                conexion?.disconnect()
            }
        }.start()
    }

    fun registrarMovimiento(
        sku: String,
        tipo: String,
        cantidad: Int,
        stockEsperado: Int,
        clave: String,
        idMovimiento: String = UUID.randomUUID().toString(),
        onResultado: (Int?, String?) -> Unit
    ) {

        Thread {

            var conexion: HttpURLConnection? = null
            val manejador = Handler(Looper.getMainLooper())

            try {

                if (clave.isBlank()) {
                    throw Exception("Debes ingresar la clave de AWS")
                }

                if (cantidad <= 0 || stockEsperado < 0) {
                    throw Exception("Cantidad incorrecta")
                }

                val datos = JSONObject()

                datos.put("accion", "movimiento")
                datos.put("sku", sku)
                datos.put("tipo", tipo)
                datos.put("cantidad", cantidad)
                datos.put("stockEsperado", stockEsperado)
                datos.put("idMovimiento", idMovimiento)

                conexion = URL(direccionApi)
                    .openConnection() as HttpURLConnection

                conexion.requestMethod = "POST"
                conexion.connectTimeout = 10000
                conexion.readTimeout = 10000
                conexion.doOutput = true

                conexion.setRequestProperty(
                    "x-api-key",
                    clave
                )

                conexion.setRequestProperty(
                    "Content-Type",
                    "application/json; charset=UTF-8"
                )

                conexion.setRequestProperty(
                    "Accept",
                    "application/json"
                )

                conexion.outputStream
                    .bufferedWriter(Charsets.UTF_8)
                    .use {
                        it.write(datos.toString())
                    }

                val codigo = conexion.responseCode

                val flujo = if (codigo in 200..299) {
                    conexion.inputStream
                } else {
                    conexion.errorStream
                }

                val respuesta = flujo
                    ?.bufferedReader()
                    ?.use {
                        it.readText()
                    } ?: ""

                if (codigo == 200) {

                    val resultado = JSONObject(respuesta)

                    val stockNuevo = resultado.getInt(
                        "stockActual"
                    )

                    manejador.post {
                        onResultado(stockNuevo, null)
                    }

                } else {

                    val mensaje = when (codigo) {

                        401 -> "Clave de AWS incorrecta"

                        409 -> "AWS rechazó el movimiento. Revisa el stock antes de continuar"

                        503 -> "La API de AWS no está configurada"

                        else -> "No se pudo confirmar el movimiento. Revisa AWS antes de repetirlo"
                    }

                    manejador.post {
                        onResultado(null, mensaje)
                    }
                }

            } catch (e: SocketTimeoutException) {

                manejador.post {
                    onResultado(
                        null,
                        "La solicitud tardó demasiado. El movimiento podría haberse guardado en AWS"
                    )
                }

            } catch (e: UnknownHostException) {

                manejador.post {
                    onResultado(
                        null,
                        "No hay conexión con AWS. Comprueba internet"
                    )
                }

            } catch (e: Exception) {

                manejador.post {
                    onResultado(
                        null,
                        "No se pudo confirmar el movimiento. Comprueba AWS antes de repetirlo"
                    )
                }

            } finally {
                conexion?.disconnect()
            }
        }.start()
    }
}
