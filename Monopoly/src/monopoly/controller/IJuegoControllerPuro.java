package monopoly.controller;

import monopoly.view.dto.TableroDTO;
import monopoly.view.dto.JugadorDTO;

/**
 * INTERFAZ DEL CONTROLADOR - El "Padre" en la metáfora MVC.
 * 
 * Define las acciones que la Vista puede solicitar al Controller.
 * La Vista NO conoce el Modelo, solo conoce esta interfaz y los DTOs.
 */
public interface IJuegoControllerPuro {

    // ===== ACCIONES DEL JUEGO =====

    /** Inicia la partida con los jugadores configurados */
    void iniciarPartida();

    /** Lanza los dados para el jugador actual */
    void tirarDados();

    /** El jugador actual compra la propiedad donde está parado */
    void comprarPropiedad();

    /** El jugador actual rechaza comprar la propiedad */
    void rechazarCompra();

    /** Finaliza el turno del jugador actual */
    void finalizarTurno();

    // ===== CONSULTAS DE ESTADO (Devuelven DTOs) =====

    /**
     * Obtiene el estado completo del tablero como DTO.
     * 
     * @return TableroDTO con toda la información necesaria para la Vista
     */
    TableroDTO obtenerEstadoTablero();

    /**
     * Obtiene los datos del jugador actual como DTO.
     * 
     * @return JugadorDTO del jugador en turno
     */
    JugadorDTO obtenerJugadorActual();

    /**
     * Verifica si los dados ya fueron lanzados este turno.
     * 
     * @return true si ya se lanzaron los dados
     */
    boolean dadosYaLanzados();

    // ===== GESTIÓN DE OBSERVADORES =====

    /**
     * Registra un observador para recibir notificaciones.
     * 
     * @param observador Implementación de IObservadorJuegoPuro (típicamente la
     *                   Vista)
     */
    void agregarObservador(IObservadorJuegoPuro observador);

    /**
     * Elimina un observador registrado.
     * 
     * @param observador Observador a eliminar
     */
    void eliminarObservador(IObservadorJuegoPuro observador);
}
