package monopoly.controller;

import monopoly.view.dto.JugadorDTO;
import monopoly.view.dto.PropiedadDTO;
import monopoly.view.dto.TableroDTO;

/**
 * INTERFAZ OBSERVADOR PURO - No expone el Modelo.
 * 
 * REGLA MVC: La Vista implementa esta interfaz para recibir notificaciones
 * del Controller. Los parámetros son SIEMPRE DTOs, NUNCA clases del Modelo.
 * 
 * De esta forma, el "Padre" (Controller) habla con el "Hijo" (Vista)
 * usando un lenguaje que NO requiere conocer al otro "Hijo" (Modelo).
 */
public interface IObservadorJuegoPuro {

    /**
     * Notifica que el estado del juego ha cambiado.
     * 
     * @param tableroDTO Estado completo del tablero (ya transformado)
     */
    void onActualizacionJuego(TableroDTO tableroDTO);

    /**
     * Notifica cambio de turno con datos del nuevo jugador.
     * 
     * @param jugadorDTO Datos del jugador actual (DTO, no Modelo)
     */
    void onCambioTurno(JugadorDTO jugadorDTO);

    /**
     * Notifica el resultado de lanzar los dados.
     * 
     * @param dado1         Valor del primer dado
     * @param dado2         Valor del segundo dado
     * @param nuevaPosicion Nueva posición del jugador
     * @param nombreCasilla Nombre de la casilla donde cayó
     */
    void onDadosLanzados(int dado1, int dado2, int nuevaPosicion, String nombreCasilla);

    /**
     * Notifica que una propiedad fue comprada.
     * 
     * @param propiedadDTO    Datos de la propiedad (DTO, no Modelo)
     * @param compradorNombre Nombre del comprador
     */
    void onPropiedadComprada(PropiedadDTO propiedadDTO, String compradorNombre);

    /**
     * Notifica un mensaje general al historial.
     * 
     * @param mensaje Texto del mensaje
     */
    void onMensaje(String mensaje);

    /**
     * Ofrece al jugador comprar una propiedad.
     * 
     * @param propiedadDTO Datos de la propiedad disponible (DTO, no Modelo)
     * @param jugadorDTO   Datos del jugador que puede comprar (DTO, no Modelo)
     */
    void onOfertaPropiedad(PropiedadDTO propiedadDTO, JugadorDTO jugadorDTO);

    /**
     * Notifica que se debe mostrar una carta de evento.
     * 
     * @param tipoCarta   Tipo de carta (SUERTE o COMUNIDAD)
     * @param descripcion Descripción de la carta
     * @param efecto      Efecto que aplica la carta
     */
    void onCartaEvento(String tipoCarta, String descripcion, String efecto);
}
