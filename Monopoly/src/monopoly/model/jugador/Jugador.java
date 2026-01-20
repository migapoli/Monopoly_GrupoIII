package monopoly.model.jugador;

import monopoly.model.casilla.Propiedad;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa un jugador en el juego Monopoly.
 * Gestiona el dinero, posición, propiedades y estado del jugador.
 */
public class Jugador implements IJugador {
    private String nombre;
    private int dinero;
    private int posicion;
    private Color color;
    private List<Propiedad> propiedades;
    private boolean enCarcel;
    private int turnosEnCarcel;
    private boolean pasoPorSalida; // Flag para detectar si pasó por la salida

    private static final int DINERO_INICIAL = 1500;
    private static final int DINERO_SALIDA = 200;

    public Jugador(String nombre, Color color) {
        this.nombre = nombre;
        this.color = color;
        this.dinero = DINERO_INICIAL;
        this.posicion = 0; // Empieza en la casilla de Salida
        this.propiedades = new ArrayList<>();
        this.enCarcel = false;
        this.turnosEnCarcel = 0;
        this.pasoPorSalida = false;
    }

    // ===== INFORMACIÓN BÁSICA =====

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public Color getColor() {
        return color;
    }

    // ===== GESTIÓN DE DINERO =====

    @Override
    public int getDinero() {
        return dinero;
    }

    @Override
    public void setDinero(int dinero) {
        this.dinero = dinero;
    }

    @Override
    public void sumarDinero(int cantidad) {
        this.dinero += cantidad;
    }

    @Override
    public void restarDinero(int cantidad) {
        this.dinero -= cantidad;
    }

    @Override
    public boolean puedePermitirse(int cantidad) {
        return this.dinero >= cantidad;
    }

    @Override
    public boolean estaBancarrota() {
        return this.dinero < 0;
    }

    // ===== GESTIÓN DE POSICIÓN Y MOVIMIENTO =====

    @Override
    public int getPosicion() {
        return posicion;
    }

    @Override
    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    /**
     * Mueve al jugador un número de casillas.
     * Detecta si el jugador pasa por la Salida para cobrar 200 EUR.
     * 
     * @param casillas             Número de casillas a mover (puede ser negativo)
     * @param totalCasillasTablero Total de casillas en el tablero
     */
    @Override
    public void mover(int casillas, int totalCasillasTablero) {
        int posicionAnterior = this.posicion;
        int nuevaPosicion = (this.posicion + casillas) % totalCasillasTablero;

        // Manejar movimiento negativo
        if (nuevaPosicion < 0) {
            nuevaPosicion += totalCasillasTablero;
        }

        // Detectar si pasó por la Salida (solo si avanza, no retrocede)
        this.pasoPorSalida = false;
        if (casillas > 0 && nuevaPosicion < posicionAnterior) {
            this.pasoPorSalida = true;
            sumarDinero(DINERO_SALIDA);
        }

        this.posicion = nuevaPosicion;
    }

    @Override
    public boolean pasoPorSalida() {
        return this.pasoPorSalida;
    }

    // ===== GESTIÓN DE PROPIEDADES =====

    @Override
    public List<Propiedad> getPropiedades() {
        return new ArrayList<>(propiedades);
    }

    @Override
    public void agregarPropiedad(Propiedad propiedad) {
        propiedades.add(propiedad);
    }

    @Override
    public void quitarPropiedad(Propiedad propiedad) {
        propiedades.remove(propiedad);
    }

    /**
     * Calcula el valor total de las propiedades del jugador.
     */
    public int getValorTotalPropiedades() {
        return propiedades.stream()
                .mapToInt(Propiedad::getPrecio)
                .sum();
    }

    /**
     * Calcula el patrimonio total del jugador (dinero + valor de propiedades).
     */
    public int getPatrimonioTotal() {
        return dinero + getValorTotalPropiedades();
    }

    // ===== GESTIÓN DE CÁRCEL =====

    @Override
    public boolean isEnCarcel() {
        return enCarcel;
    }

    @Override
    public void setEnCarcel(boolean enCarcel) {
        this.enCarcel = enCarcel;
        if (!enCarcel) {
            this.turnosEnCarcel = 0;
        }
    }

    @Override
    public int getTurnosEnCarcel() {
        return turnosEnCarcel;
    }

    @Override
    public void incrementarTurnosEnCarcel() {
        turnosEnCarcel++;
    }

    @Override
    public void resetearTurnosEnCarcel() {
        turnosEnCarcel = 0;
    }

    /**
     * Verifica si el jugador puede salir de la cárcel pagando fianza.
     */
    public boolean puedePagarFianza() {
        return puedePermitirse(50); // Fianza estándar de Monopoly
    }

    /**
     * El jugador paga la fianza para salir de la cárcel.
     * 
     * @return true si pudo pagar, false si no tiene dinero suficiente
     */
    public boolean pagarFianza() {
        if (puedePagarFianza()) {
            restarDinero(50);
            setEnCarcel(false);
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return nombre + " (Dinero: " + dinero + " EUR, Posición: " + posicion + ")";
    }
}
