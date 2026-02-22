# 🗺️ Gestor de Rutas (Android)

Una aplicación móvil nativa para Android diseñada para registrar, visualizar y almacenar rutas geográficas en tiempo real utilizando el GPS del dispositivo. 

Esta aplicación es ideal para excursionistas, ciclistas o cualquier persona que desee llevar un registro detallado de sus recorridos al aire libre, manteniendo la total privacidad de sus datos gracias al almacenamiento local.

---

## 🚀 Funcionalidades Principales

* **Seguimiento GPS en Tiempo Real:** Registra la ubicación exacta del usuario cada 10 segundos para trazar un recorrido preciso.
* **Mapa Interactivo (OpenStreetMap):** Dibuja el trayecto en vivo sobre el mapa mediante una polilínea azul fluida, centrando automáticamente la cámara en la posición actual.
* **Métricas en Vivo:** Panel flotante que calcula y muestra al instante:
  * ⏱️ Tiempo transcurrido.
  * 📏 Distancia acumulada (calculada con alta precisión mediante la fórmula de Haversine).
  * ⚡ Velocidad media del recorrido (km/h).
* **Gestión de Waypoints:** Permite al usuario marcar puntos de interés en el mapa durante su ruta, añadiendo descripciones personalizadas (ej. "Mirador", "Fuente", "Cruce").
* **Historial de Entrenamientos:** Pantalla dedicada que lista todas las rutas guardadas con sus métricas finales (distancia total y duración).
* **Visualización de Rutas Pasadas:** Al seleccionar una ruta del historial, el mapa se limpia y redibuja todo el trayecto histórico junto con sus marcadores y métricas originales.

---

## 🛠️ Tecnologías y Herramientas

El proyecto ha sido desarrollado utilizando los estándares más modernos del ecosistema Android:

* **Lenguaje:** Kotlin
* **Interfaz de Usuario (UI):** Jetpack Compose (Material Design 3)
* **Arquitectura:** MVVM (Model-View-ViewModel)
* **Persistencia de Datos:** Room Database (SQLite local)
* **Mapas:** OSMDroid (Alternativa de código abierto a Google Maps)
* **Localización:** Fused Location Provider Client (Google Play Services)
* **Programación Asíncrona:** Corrutinas de Kotlin y `StateFlow` para una interfaz reactiva.

---

## 🏗️ Arquitectura y Componentes

La aplicación está estructurada de forma modular siguiendo el patrón **MVVM** para separar la lógica de negocio de la interfaz gráfica:

1. **Model (`/model`):** Contiene las entidades de la base de datos de Room (`Ruta`, `PuntoRuta`, `Waypoint`) y sus relaciones mediante claves foráneas.
2. **Data (`/data`):** Contiene el `GestorRutasDao` y el `GestorRutasRepository` que gestionan las consultas SQL y emiten flujos de datos (`Flow`) reactivos.
3. **ViewModel (`/viewmodel`):** El corazón lógico de la app (`UbicacionViewModel`). Gestiona el ciclo de vida de la grabación, los cálculos matemáticos (Haversine) y el estado de la UI.
4. **UI (`/ui`):** Componentes visuales construidos con Jetpack Compose, organizados en pantallas (`PantallaGrabacion`, `PantallaListaRutas`) y componentes reutilizables (`MapaOSM`, `PanelMetricas`).

---

## 📱 Instalación y Uso

### Requisitos previos
* Android Studio (versión Iguana o superior recomendada).
* Dispositivo físico con Android 7.0 (API 24) o superior, o un Emulador de Android.

### Pasos
1. Clona el repositorio:
   ```bash
   git clone [https://github.com/tu-usuario/gestorderutas.git](https://github.com/tu-usuario/gestorderutas.git)
