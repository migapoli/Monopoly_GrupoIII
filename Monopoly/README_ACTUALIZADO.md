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
- **Guardar/Cargar Partida**: Sistema básico para guardar el estado del juego en archivos de texto.

## 🛠️ Cómo Ejecutar

Desde la raíz del proyecto (carpeta `Monopoly`), ejecuta los siguientes comandos en tu terminal:

1. **Compilar**:
   ```bash
   javac -encoding UTF-8 -d out -sourcepath src src/App.java
   ```

2. **Ejecutar**:
   ```bash
   java -cp out App
   ```

## 📂 Estructura del Código

- `monopoly.model`: Lógica de negocio (Tablero, Jugador, Casilla, Cartas).
- `monopoly.view`: Interfaz gráfica (Ventanas, Paneles, Diálogos).
- `monopoly.controller`: Gestión del flujo del juego y comunicación Model-View.
- `monopoly.main`: Punto de entrada (`App.java` -> `Main.java`).

## ✨ Últimas Actualizaciones

- Implementación completa de la lógica de **Cárcel** (fianza, 3 turnos).
- Sistema de **Cartas de Suerte y Comunidad** con efectos reales.
- **Condición de Victoria**: El juego detecta cuando queda un solo jugador.
- **Estilo Visual Moderno**: Nuevos diseños para diálogos de eventos.
