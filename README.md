# InventarioTI

## Descripción del proyecto

InventarioTI es una aplicación móvil desarrollada en Android Studio utilizando Kotlin. Su objetivo es facilitar el control de inventario de productos tecnológicos, permitiendo consultar existencias, registrar entradas y salidas, revisar movimientos y detectar productos con stock bajo.

Este proyecto fue desarrollado con fines académicos utilizando datos ficticios.

## Tecnologías utilizadas

- Kotlin como lenguaje de programación.
- Android Studio como entorno de desarrollo.
- Jetpack Compose para las interfaces.
- Material Design 3 para los componentes visuales.
- SQLite para el almacenamiento local.
- MVVM para organizar la lógica y las pantallas.
- Git y GitHub para el control de versiones.

## Funcionalidades

La aplicación permite:

- Iniciar sesión con usuarios de prueba.
- Controlar el acceso según el rol del usuario.
- Visualizar productos tecnológicos y sus existencias.
- Registrar entradas y salidas de productos.
- Validar cantidades y evitar salidas superiores al stock disponible.
- Consultar el historial de movimientos.
- Mostrar alertas de stock bajo.
- Guardar productos y movimientos mediante SQLite.
- Acceder a la cámara del dispositivo para tomar fotografías de vista previa.
- Solicitar permisos y generar notificaciones de stock bajo.

## Usuarios de prueba

| Usuario | Contraseña | Rol |
|---|---|---|
| admin | 1234 | Administrador |
| bodega | 1234 | Bodega |
| ventas | 1234 | Ventas |
| supervisor | 1234 | Supervisor |

Los permisos dependen del rol seleccionado. Las credenciales son ficticias y se utilizan únicamente para demostrar el funcionamiento del proyecto.

## Instalación y ejecución

Para ejecutar InventarioTI en otro computador, se necesita Android Studio y el SDK de Android correspondiente.

1. Descargar o clonar el repositorio desde GitHub.
2. Abrir Android Studio.
3. Seleccionar Open y abrir la carpeta principal del proyecto.
4. Esperar a que Gradle termine de sincronizar las dependencias.
5. Seleccionar un emulador Android compatible o conectar un dispositivo físico.
6. Presionar Run para ejecutar la aplicación.

La aplicación utiliza una base de datos SQLite local, que se crea con productos ficticios cuando se ejecuta por primera vez.

## Organización del proyecto

**MainActivity.kt:** inicia la aplicación y coordina la navegación.

**model:** contiene las clases Producto y Movimiento.

**data:** contiene la base de datos SQLite, los productos de prueba y las notificaciones.

**screens:** contiene las pantallas de productos, movimientos, historial, alertas y cámara.

**viewmodel:** contiene la lógica de validación, usuarios y operaciones del inventario.

**ui.theme:** contiene la configuración visual de la aplicación.

## Estado del proyecto

El proyecto cuenta con las funcionalidades principales de control de inventario y persistencia local.

Se encuentran pendientes de implementación o verificación las animaciones funcionales, el consumo de una API REST de prueba y las pruebas de ejecución en otro computador.

## Consideraciones

La aplicación trabaja con datos ficticios y no se conecta con los sistemas reales de ninguna empresa.

Las fotografías tomadas con la cámara se muestran temporalmente y no se almacenan en la base de datos.

Cada instalación utiliza su propia base de datos local.

## Proyecto académico

Desarrollo de aplicaciones móviles con Kotlin y Android Studio.

Proyecto: InventarioTI.
