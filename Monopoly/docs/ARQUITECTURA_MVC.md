# 🏗️ Arquitectura MVC Estricta - Monopoly

## Metáfora Padre-Hijo

```
                    ┌─────────────────────┐
                    │   🎯 CONTROLLER     │
                    │      (PADRE)        │
                    │                     │
                    │  JuegoControllerPuro│
                    └──────────┬──────────┘
                               │
              ┌────────────────┼────────────────┐
              │                │                │
              │   Coordina     │   Coordina     │
              │   y Transforma │   y Transforma │
              ▼                                 ▼
    ┌─────────────────┐               ┌─────────────────┐
    │   🔵 MODEL      │               │   🟢 VIEW       │
    │    (HIJO 1)     │◄──── ⛔ ────►│    (HIJO 2)     │
    │                 │   PROHIBIDO   │                 │
    │  Jugador        │   HABLARSE    │  VentanaPrincipal│
    │  Tablero        │               │  PanelTablero    │
    │  Casilla        │               │  PanelControles  │
    │  Dado           │               │                 │
    └─────────────────┘               └─────────────────┘
```

---

## 📦 Estructura de Paquetes

```
src/monopoly/
├── model/                          # 🔵 Lógica de Negocio PURA
│   ├── jugador/
│   │   ├── Jugador.java
│   │   ├── IJugador.java
│   │   └── Dado.java
│   ├── casilla/
│   │   ├── Casilla.java
│   │   ├── Propiedad.java
│   │   └── ...
│   ├── tablero/
│   │   ├── Tablero.java
│   │   └── Partida.java
│   └── cartas/
│       └── Carta.java
│
├── view/                           # 🟢 Presentación (UI)
│   ├── VentanaPrincipalPura.java   # ✅ Vista MVC correcta
│   ├── PanelTableroPuro.java       # ✅ Panel MVC correcto
│   ├── PanelControles.java
│   ├── PanelInfoJugador.java
│   ├── PanelHistorial.java
│   ├── DialogoComprarPropiedad.java
│   ├── DialogoCartaEvento.java
│   └── dto/                        # 🔴 DTOs (clave para MVC puro)
│       ├── JugadorDTO.java
│       ├── CasillaDTO.java
│       ├── PropiedadDTO.java
│       └── TableroDTO.java
│
└── controller/                     # 🟡 Orquestador
    ├── JuegoControllerPuro.java    # ✅ Controller MVC correcto
    ├── IJuegoControllerPuro.java
    ├── IObservadorJuegoPuro.java
    └── (archivos legacy...)
```

---

## 🚫 IMPORTS PROHIBIDOS POR PAQUETE

### En `monopoly.view.*` (PROHIBIDO importar del Model)

```java
// ❌ PROHIBIDOS - Rompen el patrón MVC
import monopoly.model.jugador.Jugador;
import monopoly.model.jugador.IJugador;
import monopoly.model.casilla.Casilla;
import monopoly.model.casilla.Propiedad;
import monopoly.model.tablero.Tablero;
import monopoly.model.tablero.Partida;
import monopoly.model.cartas.*;

// ✅ PERMITIDOS
import monopoly.controller.*;           // El Controller es el "Padre"
import monopoly.view.dto.*;             // DTOs son el "idioma" de la Vista
import javax.swing.*;
import java.awt.*;
```

### En `monopoly.model.*` (PROHIBIDO importar de View y Controller)

```java
// ❌ PROHIBIDOS - El Model no sabe que existen
import monopoly.view.*;
import monopoly.view.dto.*;
import monopoly.controller.*;
import javax.swing.*;  // ¡El Model NO tiene UI!

// ✅ PERMITIDOS
import java.util.*;
import java.io.*;
// Solo librería estándar de Java
```

### En `monopoly.controller.*` (PUEDE importar de ambos)

```java
// ✅ PERMITIDOS - El Controller conoce a ambos "hijos"
import monopoly.model.jugador.*;
import monopoly.model.casilla.*;
import monopoly.model.tablero.*;
import monopoly.view.dto.*;  // Para transformar Model → DTO

// ❌ PROHIBIDOS - El Controller NO crea componentes UI
import javax.swing.JFrame;
import javax.swing.JPanel;
// El Controller solo usa los DTOs, no componentes Swing
```

---

## 🔄 Flujo de Datos: "Jugador tira dados y cae en propiedad"

### Paso 1: Usuario hace clic en "Tirar Dados"
```java
// En VentanaPrincipalPura.java (VISTA)
btnTirarDados.addActionListener(e -> {
    controller.tirarDados();  // ✅ Delega al Controller
    // ❌ NO hace: jugador.setPosicion(), dado.lanzar(), etc.
});
```

### Paso 2: Controller ejecuta lógica en el Modelo
```java
// En JuegoControllerPuro.java (CONTROLLER)
public void tirarDados() {
    // ✅ Opera sobre el MODELO
    IJugador jugador = partida.getJugadorActual();
    int[] valores = dado.lanzar();
    int nuevaPos = (jugador.getPosicion() + suma) % 40;
    jugador.setPosicion(nuevaPos);
    
    // ✅ Obtiene casilla del MODELO
    Casilla casilla = partida.getTablero().getCasilla(nuevaPos);
    
    // ✅ Notifica a la VISTA con datos primitivos/DTOs
    notificarDadosLanzados(valores[0], valores[1], nuevaPos, casilla.getNombre());
}
```

### Paso 3: Controller ofrece propiedad (si aplica)
```java
// En JuegoControllerPuro.java (CONTROLLER)
if (casilla instanceof Propiedad && !propiedad.tienePropietario()) {
    // ✅ TRANSFORMA Modelo → DTO antes de notificar
    PropiedadDTO propDTO = transformarPropiedad(propiedad);
    JugadorDTO jugadorDTO = transformarJugador(jugador, true);
    
    // ✅ Notifica con DTOs, NO con objetos del Modelo
    notificarOfertaPropiedad(propDTO, jugadorDTO);
}
```

### Paso 4: Vista recibe DTOs y actualiza UI
```java
// En VentanaPrincipalPura.java (VISTA)
@Override
public void onOfertaPropiedad(PropiedadDTO propDTO, JugadorDTO jugadorDTO) {
    // ✅ Usa DTOs para mostrar información
    String mensaje = String.format("¿Comprar %s por $%d?", 
        propDTO.getNombre(),   // ✅ Del DTO
        propDTO.getPrecio());  // ✅ Del DTO
    
    // ✅ Si acepta, delega al Controller
    if (usuarioAcepta) {
        controller.comprarPropiedad();
    }
}
```

---

## 📊 Resumen de Reglas

| Paquete | Puede Importar | NO Puede Importar |
|---------|----------------|-------------------|
| `model` | `java.*` | `view.*`, `controller.*`, `javax.swing.*` |
| `view` | `controller.*`, `view.dto.*`, `javax.swing.*` | `model.*` |
| `controller` | `model.*`, `view.dto.*` | `view.Component`, `javax.swing.J*` |

---

## ✅ Checklist de Validación MVC

- [ ] ¿Las clases de `view` tienen 0 imports de `model`?
- [ ] ¿Las clases de `model` tienen 0 imports de `view` y `controller`?
- [ ] ¿El Controller usa DTOs para comunicarse con la Vista?
- [ ] ¿Los eventos de UI delegan acciones al Controller?
- [ ] ¿El Modelo es testeabe sin dependencias de UI?

---

## 📁 Archivos Creados en esta Refactorización

### DTOs (Data Transfer Objects)
- `view/dto/JugadorDTO.java` - Datos del jugador para la Vista
- `view/dto/CasillaDTO.java` - Datos de casilla para la Vista  
- `view/dto/PropiedadDTO.java` - Datos de propiedad para la Vista
- `view/dto/TableroDTO.java` - Estado completo del tablero

### Interfaces Puras
- `controller/IJuegoControllerPuro.java` - Contrato del Controller
- `controller/IObservadorJuegoPuro.java` - Contrato de notificación

### Implementaciones MVC Correctas
- `controller/JuegoControllerPuro.java` - Controller que transforma Model→DTO
- `view/VentanaPrincipalPura.java` - Vista que solo usa DTOs
- `view/PanelTableroPuro.java` - Panel que renderiza con DTOs

---

*Documento generado para el proyecto Monopoly_GrupoIII*
*Patrón: MVC Estricto con DTOs*
