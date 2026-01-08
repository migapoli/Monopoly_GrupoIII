package monopoly.controller;

import monopoly.model.casilla.Casilla;
import monopoly.model.casilla.Propiedad;
import monopoly.model.jugador.IJugador;
import monopoly.model.jugador.Dado;
import monopoly.model.tablero.Partida;
import monopoly.model.tablero.Tablero;

import monopoly.view.dto.CasillaDTO;
import monopoly.view.dto.JugadorDTO;
import monopoly.view.dto.PropiedadDTO;
import monopoly.view.dto.TableroDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * JUEGOCONTROLLER PURO - Implementación correcta del patrón MVC.
 * 
 * ═══════════════════════════════════════════════════════════════════
 * 🎯 REGLAS DEL "PADRE"
 * ═══════════════════════════════════════════════════════════════════
 * 
 * 1. El Controller es el ÚNICO que conoce AMBOS hijos (Model y View)
 * 2. El Controller TRANSFORMA datos del Model a DTOs para la View
 * 3. El Controller RECIBE acciones de la View y las ejecuta en el Model
 * 4. Los "Hijos" (Model y View) NUNCA se comunican directamente
 * 
 * ═══════════════════════════════════════════════════════════════════
 */
public class JuegoControllerPuro implements IJuegoControllerPuro {

    // ===== REFERENCIAS AL MODELO (Solo el Controller las conoce) =====
    private final Partida partida;
    private final Dado dado;

    // ===== ESTADO DEL CONTROLLER =====
    private boolean dadosLanzados;
    private int turnoNumero;

    // ===== OBSERVADORES (Vista registrada) =====
    private final List<IObservadorJuegoPuro> observadores;

    // ═══════════════════════════════════════════════════════════════════
    // CONSTRUCTOR
    // ═══════════════════════════════════════════════════════════════════

    public JuegoControllerPuro() {
        this.partida = new Partida();
        this.dado = new Dado();
        this.observadores = new ArrayList<>();
        this.dadosLanzados = false;
        this.turnoNumero = 1;
    }

    public JuegoControllerPuro(Partida partida) {
        this.partida = partida;
        this.dado = partida.getDado();
        this.observadores = new ArrayList<>();
        this.dadosLanzados = false;
        this.turnoNumero = 1;
    }

    // ═══════════════════════════════════════════════════════════════════
    // TRANSFORMADORES: MODEL → DTO
    // (Esta es la CLAVE del patrón - el Controller traduce)
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Transforma un Jugador del Modelo a un JugadorDTO para la Vista.
     * La Vista NUNCA verá la clase Jugador, solo este DTO.
     */
    private JugadorDTO transformarJugador(IJugador jugador, boolean esTurnoActivo) {
        List<String> nombresPropiedades = jugador.getPropiedades().stream()
                .map(Propiedad::getNombre)
                .collect(Collectors.toList());

        return new JugadorDTO(
                jugador.getNombre(),
                jugador.getDinero(),
                jugador.getPosicion(),
                jugador.getColor(),
                esTurnoActivo,
                nombresPropiedades);
    }

    /**
     * Transforma una Casilla del Modelo a un CasillaDTO para la Vista.
     */
    private CasillaDTO transformarCasilla(Casilla casilla) {
        CasillaDTO.TipoCasillaDTO tipoDTO = convertirTipoCasilla(casilla.getTipo());

        // Si es propiedad, extraer info adicional
        if (casilla instanceof Propiedad) {
            Propiedad prop = (Propiedad) casilla;
            String propietarioNombre = prop.tienePropietario() ? prop.getPropietario().getNombre() : null;
            java.awt.Color colorProp = prop.tienePropietario() ? prop.getPropietario().getColor() : null;

            return new CasillaDTO(
                    casilla.getPosicion(),
                    casilla.getNombre(),
                    tipoDTO,
                    prop.getGrupoColor(),
                    prop.getPrecio(),
                    propietarioNombre,
                    colorProp);
        }

        return new CasillaDTO(
                casilla.getPosicion(),
                casilla.getNombre(),
                tipoDTO,
                null, null, null, null);
    }

    /**
     * Transforma una Propiedad del Modelo a PropiedadDTO.
     */
    private PropiedadDTO transformarPropiedad(Propiedad propiedad) {
        String propietarioNombre = propiedad.tienePropietario() ? propiedad.getPropietario().getNombre() : null;

        return new PropiedadDTO(
                propiedad.getPosicion(),
                propiedad.getNombre(),
                propiedad.getPrecio(),
                propiedad.getAlquiler(),
                propiedad.getGrupoColor(),
                propietarioNombre,
                !propiedad.tienePropietario());
    }

    /**
     * Convierte el enum del Modelo al enum del DTO.
     */
    private CasillaDTO.TipoCasillaDTO convertirTipoCasilla(Casilla.TipoCasilla tipo) {
        switch (tipo) {
            case SALIDA:
                return CasillaDTO.TipoCasillaDTO.SALIDA;
            case PROPIEDAD:
                return CasillaDTO.TipoCasillaDTO.PROPIEDAD;
            case SUERTE:
                return CasillaDTO.TipoCasillaDTO.SUERTE;
            case COMUNIDAD:
                return CasillaDTO.TipoCasillaDTO.COMUNIDAD;
            case CARCEL:
                return CasillaDTO.TipoCasillaDTO.CARCEL;
            case PARKING:
                return CasillaDTO.TipoCasillaDTO.PARKING;
            case IR_CARCEL:
                return CasillaDTO.TipoCasillaDTO.IR_CARCEL;
            case IMPUESTO:
                return CasillaDTO.TipoCasillaDTO.IMPUESTO;
            case ESTACION:
                return CasillaDTO.TipoCasillaDTO.ESTACION;
            case SERVICIO:
                return CasillaDTO.TipoCasillaDTO.SERVICIO;
            default:
                return CasillaDTO.TipoCasillaDTO.PROPIEDAD;
        }
    }

    // ═══════════════════════════════════════════════════════════════════
    // IMPLEMENTACIÓN DE CONSULTAS (IJuegoControllerPuro)
    // ═══════════════════════════════════════════════════════════════════

    @Override
    public TableroDTO obtenerEstadoTablero() {
        IJugador jugadorActual = partida.getJugadorActual();

        // Transformar todas las casillas
        List<CasillaDTO> casillasDTO = new ArrayList<>();
        for (Casilla casilla : partida.getTablero().getCasillas()) {
            casillasDTO.add(transformarCasilla(casilla));
        }

        // Transformar todos los jugadores
        List<JugadorDTO> jugadoresDTO = new ArrayList<>();
        for (IJugador jugador : partida.getJugadores()) {
            boolean esActivo = jugador == jugadorActual;
            jugadoresDTO.add(transformarJugador(jugador, esActivo));
        }

        return new TableroDTO(
                casillasDTO,
                jugadoresDTO,
                jugadorActual.getNombre(),
                turnoNumero,
                2000 // Fondo común (ejemplo)
        );
    }

    @Override
    public JugadorDTO obtenerJugadorActual() {
        IJugador jugador = partida.getJugadorActual();
        return transformarJugador(jugador, true);
    }

    @Override
    public boolean dadosYaLanzados() {
        return dadosLanzados;
    }

    // ═══════════════════════════════════════════════════════════════════
    // IMPLEMENTACIÓN DE ACCIONES (IJuegoControllerPuro)
    // ═══════════════════════════════════════════════════════════════════

    @Override
    public void iniciarPartida() {
        if (partida.getJugadores().size() < 2) {
            notificarMensaje("Se necesitan al menos 2 jugadores para iniciar");
            return;
        }

        notificarMensaje("¡Partida iniciada!");
        notificarCambioTurno();
        notificarActualizacion();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════
     * EJEMPLO CLAVE: "Jugador tira dados y cae en propiedad"
     * ═══════════════════════════════════════════════════════════════════
     * 
     * FLUJO CORRECTO MVC:
     * 
     * 1. Vista llama: controller.tirarDados()
     * 2. Controller opera sobre el Modelo (Jugador, Dado, Tablero)
     * 3. Controller TRANSFORMA el resultado a DTOs
     * 4. Controller NOTIFICA a la Vista con DTOs
     * 5. Vista actualiza su UI usando solo los DTOs recibidos
     * 
     * ¡En ningún momento la Vista toca el Modelo!
     */
    @Override
    public void tirarDados() {
        if (dadosLanzados) {
            notificarMensaje("Ya has lanzado los dados este turno");
            return;
        }

        // ===== PASO 1: Operar sobre el MODELO =====
        IJugador jugadorActual = partida.getJugadorActual();
        int[] valores = dado.lanzar();
        int suma = valores[0] + valores[1];

        // Calcular nueva posición
        int posicionAnterior = jugadorActual.getPosicion();
        int nuevaPosicion = (posicionAnterior + suma) % 40;

        // Verificar paso por salida
        if (nuevaPosicion < posicionAnterior) {
            jugadorActual.sumarDinero(200);
            notificarMensaje(jugadorActual.getNombre() + " pasó por la Salida y cobró $200");
        }

        // Mover jugador en el MODELO
        jugadorActual.setPosicion(nuevaPosicion);
        Casilla casillaActual = partida.getTablero().getCasilla(nuevaPosicion);

        dadosLanzados = true;

        // ===== PASO 2: Notificar a la VISTA con datos primitivos/DTOs =====
        // La Vista recibe valores simples, NO objetos del Modelo
        notificarDadosLanzados(valores[0], valores[1], nuevaPosicion, casillaActual.getNombre());

        // Registrar en historial
        String mensaje = String.format("%s sacó %d + %d = %d y cayó en %s",
                jugadorActual.getNombre(), valores[0], valores[1],
                suma, casillaActual.getNombre());
        partida.agregarHistorial(mensaje);
        notificarMensaje(mensaje);

        // ===== PASO 3: Procesar acción de la casilla =====
        ejecutarAccionCasilla(casillaActual, jugadorActual);

        // ===== PASO 4: Actualizar estado completo (como DTO) =====
        notificarActualizacion();
    }

    /**
     * Procesa la acción de una casilla - interno del Controller.
     */
    private void ejecutarAccionCasilla(Casilla casilla, IJugador jugador) {
        if (casilla instanceof Propiedad) {
            Propiedad propiedad = (Propiedad) casilla;

            if (!propiedad.tienePropietario()) {
                // ===== OFERTA DE PROPIEDAD =====
                // Transformar a DTOs antes de notificar a la Vista
                PropiedadDTO propDTO = transformarPropiedad(propiedad);
                JugadorDTO jugadorDTO = transformarJugador(jugador, true);

                // Notificar con DTOs - ¡La Vista NO conoce Propiedad ni Jugador!
                notificarOfertaPropiedad(propDTO, jugadorDTO);

            } else if (propiedad.getPropietario() != jugador) {
                // ===== PAGAR ALQUILER =====
                int alquiler = propiedad.getAlquiler();
                jugador.restarDinero(alquiler);
                propiedad.getPropietario().sumarDinero(alquiler);

                String msg = String.format("%s pagó $%d de alquiler a %s",
                        jugador.getNombre(), alquiler, propiedad.getPropietario().getNombre());
                partida.agregarHistorial(msg);
                notificarMensaje(msg);
            }

        } else if (casilla.getTipo() == Casilla.TipoCasilla.SUERTE) {
            notificarCartaEvento("SUERTE", "Has sacado una carta de Suerte", "Avanza 3 casillas");

        } else if (casilla.getTipo() == Casilla.TipoCasilla.COMUNIDAD) {
            notificarCartaEvento("COMUNIDAD", "Has sacado una carta de Caja de Comunidad", "Cobra 50 EUR del banco");
        }
    }

    @Override
    public void comprarPropiedad() {
        IJugador jugadorActual = partida.getJugadorActual();
        Casilla casillaActual = partida.getTablero().getCasilla(jugadorActual.getPosicion());

        if (!(casillaActual instanceof Propiedad)) {
            notificarMensaje("No puedes comprar esta casilla");
            return;
        }

        Propiedad propiedad = (Propiedad) casillaActual;

        if (propiedad.tienePropietario()) {
            notificarMensaje("Esta propiedad ya tiene dueño");
            return;
        }

        if (jugadorActual.getDinero() < propiedad.getPrecio()) {
            notificarMensaje("No tienes suficiente dinero para comprar esta propiedad");
            return;
        }

        // Ejecutar compra en el MODELO
        jugadorActual.restarDinero(propiedad.getPrecio());
        propiedad.setPropietario(jugadorActual);
        jugadorActual.agregarPropiedad(propiedad);

        // Notificar a la VISTA con DTOs
        PropiedadDTO propDTO = transformarPropiedad(propiedad);
        String mensaje = String.format("%s compró %s por $%d",
                jugadorActual.getNombre(), propiedad.getNombre(), propiedad.getPrecio());

        partida.agregarHistorial(mensaje);
        notificarPropiedadComprada(propDTO, jugadorActual.getNombre());
        notificarMensaje(mensaje);
        notificarActualizacion();
    }

    @Override
    public void rechazarCompra() {
        notificarMensaje("Has decidido no comprar la propiedad");
    }

    @Override
    public void finalizarTurno() {
        if (!dadosLanzados) {
            notificarMensaje("Debes lanzar los dados antes de finalizar tu turno");
            return;
        }

        partida.siguienteTurno();
        dadosLanzados = false;
        turnoNumero++;

        String mensaje = "Turno de " + partida.getJugadorActual().getNombre();
        partida.agregarHistorial(mensaje);

        notificarCambioTurno();
        notificarMensaje(mensaje);
        notificarActualizacion();
    }

    // ═══════════════════════════════════════════════════════════════════
    // GESTIÓN DE OBSERVADORES
    // ═══════════════════════════════════════════════════════════════════

    @Override
    public void agregarObservador(IObservadorJuegoPuro observador) {
        if (!observadores.contains(observador)) {
            observadores.add(observador);
        }
    }

    @Override
    public void eliminarObservador(IObservadorJuegoPuro observador) {
        observadores.remove(observador);
    }

    // ═══════════════════════════════════════════════════════════════════
    // MÉTODOS PRIVADOS DE NOTIFICACIÓN
    // (Siempre envían DTOs o primitivos, NUNCA el Modelo)
    // ═══════════════════════════════════════════════════════════════════

    private void notificarActualizacion() {
        TableroDTO tableroDTO = obtenerEstadoTablero();
        for (IObservadorJuegoPuro obs : observadores) {
            obs.onActualizacionJuego(tableroDTO);
        }
    }

    private void notificarCambioTurno() {
        JugadorDTO jugadorDTO = obtenerJugadorActual();
        for (IObservadorJuegoPuro obs : observadores) {
            obs.onCambioTurno(jugadorDTO);
        }
    }

    private void notificarDadosLanzados(int d1, int d2, int pos, String casilla) {
        for (IObservadorJuegoPuro obs : observadores) {
            obs.onDadosLanzados(d1, d2, pos, casilla);
        }
    }

    private void notificarPropiedadComprada(PropiedadDTO propDTO, String comprador) {
        for (IObservadorJuegoPuro obs : observadores) {
            obs.onPropiedadComprada(propDTO, comprador);
        }
    }

    private void notificarMensaje(String mensaje) {
        for (IObservadorJuegoPuro obs : observadores) {
            obs.onMensaje(mensaje);
        }
    }

    private void notificarOfertaPropiedad(PropiedadDTO propDTO, JugadorDTO jugadorDTO) {
        for (IObservadorJuegoPuro obs : observadores) {
            obs.onOfertaPropiedad(propDTO, jugadorDTO);
        }
    }

    private void notificarCartaEvento(String tipo, String desc, String efecto) {
        for (IObservadorJuegoPuro obs : observadores) {
            obs.onCartaEvento(tipo, desc, efecto);
        }
    }

    // ===== MÉTODO DE ACCESO A PARTIDA (para configuración inicial) =====
    public Partida getPartida() {
        return partida;
    }
}
