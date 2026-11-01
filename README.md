
<div align="center">

<img src="./assets/logo-404-cafe.png" width="300" alt="404 Café Encontrado">

# ☕ 404 CAFÉ ENCONTRADO

### Encuentra tu café ideal.

**Aplicación móvil para descubrir cafeterías.**

</div>

# 404 Café Encontrado - Android

Aplicación Android Java con interfaz en español y tema café/dorado.

## Abrir el proyecto
1. Extrae el ZIP.
2. En Android Studio elige **Open** y selecciona la carpeta que contiene `settings.gradle.kts`.
3. Espera a que Gradle termine la sincronización.
4. Ejecuta con un emulador o teléfono Android.

## Módulos implementados
- Inicio de sesión y registro local de una cuenta en el dispositivo.
- Recuperación local de contraseña (no envía correo; cambia la contraseña guardada en el dispositivo).
- Panel principal.
- Estado de 10 computadoras, asignación y liberación.
- Registro, edición y eliminación de clientes.
- Inventario de snacks: alta, edición, eliminación, precio y existencias.
- Catálogo y carrito de snacks.
- Flujo de reserva para sesiones de PC (acciones de iniciar/extender/finalizar; es una demostración local y no un temporizador en segundo plano).
- Generación de cobro con costo de PC a $2.00 por hora más snacks.
- Reporte básico con computadoras, clientes, productos y total de cobros.

## Datos y limitaciones
Los datos se guardan localmente con `SharedPreferences`; no hay servidor ni base de datos en la nube. La recuperación de contraseña es local y no envía códigos al correo. Los registros de clientes y los estados de PC se conservan al cerrar la app; el inventario/carrito es de demostración y no sustituye una base de datos real. La interfaz usa componentes Android estándar creados en Java.
