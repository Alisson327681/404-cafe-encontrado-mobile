404 Café Encontrado - Android Application

*INTEGRANTES DEL PROYECTO*
BRANDON CENTENO RAMOS

ALISSON CENTENO RAMOS

LILIANA LOPEZ CACERES

MEYLIN FUENTES APRIAZA


404 Café Encontrado es una aplicación móvil nativa para Android desarrollada en Kotlin. Está diseñada para la gestión integral de un cybercafé, combinando la administración de estaciones de cómputo, registro de clientes, inventario de snacks, punto de venta y reportes de facturación en una interfaz moderna con una paleta de colores café y dorado.

Tecnologías Utilizadas
Lenguaje: Kotlin
Plataforma: Android (SDK nativo)
IDE: Android Studio
Persistencia de Datos: SharedPreferences (Almacenamiento local)
Gestor de Dependencias: Gradle (Kotlin DSL / build.gradle.kts)
Diseño UI: Componentes Material Design con tema personalizado (Coffee & Gold)

Características y Módulos Implementados
1. Autenticación y Seguridad
Inicio de Sesión y Registro: Gestión local de cuentas de usuario dentro del dispositivo.
Recuperación de Contraseña: Flujo local para la actualización de credenciales de acceso.

   2. Gestión de Estaciones (Computadoras)
Control de estado en tiempo real para 10 computadoras (Disponible / Ocupada / Mantenimiento).
Asignación y liberación rápida de estaciones de cómputo.
Simulación de flujo de reserva (iniciar, extender y finalizar sesión).
3.  Administración de Clientes
Módulo CRUD completo (Crear, Leer, Editar y Eliminar) para el registro de clientes del cybercafé.

4. Inventario y Tienda de Snacks
Gestión de Productos: Alta, edición, eliminación, asignación de precios y control de existencias.

Catálogo y Carrito: Selección interactiva de snacks y bebidas para añadir a la cuenta del cliente.

5. Sistema de Cobro y Facturación
Cálculo automático de tarifas: Costo de tiempo en PC ($2.00 / hora) + consumos de snacks agregados al carrito.

Generación de resumen de cobro detalado.

6. Reportes y Estadísticas
Visualización global del estado de la tienda: resumen de uso de computadoras, total de clientes registrados, stock de productos y acumulado total de ventas.

Persistencia y Consideraciones Técnicas
 Nota sobre los datos: Esta aplicación utiliza almacenamiento local persistente (SharedPreferences). No requiere de un servidor externo ni base de datos en la nube para funcionar, lo que la hace ideal para entornos de demostración y ejecución rápida offline.

*Requisitos e Instalación*
Requisitos previos
Android Studio Ladybug (o superior recomendado).
JDK: versión 17 o superior.
Dispositivo físico o Emulador: Android 8.0 (API nivel 26) o superior.
Pasos para ejecutar el proyecto
Clonar el repositorio:

Bash
git clone https://github.com/tu-usuario/404-cafe-encontrado.git
Abrir en Android Studio:

Abre Android Studio.
Selecciona Open y navega hasta la carpeta raíz del proyecto (donde se encuentra settings.gradle.kts).
Sincronizar Gradle:
Espera a que Gradle descargue las dependencias y sincronice el proyecto correctamente.
Ejecutar:
Selecciona tu emulador o dispositivo físico y presiona Run (o Shift + F10).

