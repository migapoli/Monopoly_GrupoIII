# Monopoly Java Project - Grupo III

Implementación del juego Monopoly en Java utilizando patrón MVC (Modelo-Vista-Controlador) y Swing para la interfaz gráfica.

## 🚀 Estado del Proyecto

El proyecto está funcional e incluye las siguientes características clave:

### Mecánicas Principales
- **Gestión de Jugadores**: Movimiento, dinero, propiedades y bancarrota.
- **Tablero Completo**: 40 casillas incluyendo propiedades, estaciones, servicios, suerte, comunidad e impuestos.
- **Ciclo de Turnos**: Lanzar dados, mover ficha, ejecutar acción de casilla, pasar turno.
- **Reglas Especiales**:
  - Dobles en los dados.
  - Paso por Salida (cobra 200).
  - Cárcel (fianza, turnos sin jugar, dobles para salir).
  - Impuestos (pago directo).
  - Parking Gratuito.

### Interfaz Gráfica (Swing)
- **Pantalla de Configuración**: Selección de 2-4 jugadores con nombres y colores.
- **Tablero Visual**: Representación gráfica de todas las casillas y fichas de jugadores.
- **Paneles de Información**:
  - Panel de dados (animado).
  - Historial de eventos.
  - Tarjeta de información del jugador activo.
- **Diálogos Interactivos**:
  - Compra de propiedades.
  - Cartas de Suerte/Comunidad.
  - Pago de Impuestos.
  - Gestión de Cárcel.

### Persistencia
- **Guardar/Cargar Partida**: Sistema de guardado en archivos de texto en la carpeta `saves`.
- **Cargar desde la pantalla inicial**: Botón para seleccionar partidas guardadas.
- **Sobrescritura controlada**: Al guardar, permite sobrescribir partidas existentes o crear una nueva.
- **Título dinámico**: Muestra el nombre de la partida cargada/guardada.

## 🛠️ Cómo Ejecutar

Desde la raíz del proyecto (carpeta `Monopoly`), ejecuta los siguientes comandos en tu terminal:

1. **Compilar**:
  ```bash
  javac -encoding UTF-8 -d out -sourcepath src src/monopoly/main/Main.java
  ```

2. **Ejecutar**:
  ```bash
  java -cp out monopoly.main.Main
  ```

## 📂 Estructura del Código

- `monopoly.model`: Lógica de negocio (Tablero, Jugador, Casilla, Cartas).
- `monopoly.view`: Interfaz gráfica (Ventanas, Paneles, Diálogos).
- `monopoly.controller`: Gestión del flujo del juego y comunicación Model-View.
- `monopoly.main`: Punto de entrada (`Main.java`).

## ✨ Últimas Actualizaciones

- **Carga desde pantalla inicial** con selector de partidas guardadas.
- **Guardado con sobrescritura** y selección de partidas existentes.
- **Título de ventana** con nombre de la partida cargada/guardada.
- **Punto de entrada único** en `monopoly.main.Main`.
