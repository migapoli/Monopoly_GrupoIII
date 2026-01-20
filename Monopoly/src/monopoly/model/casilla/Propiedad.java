package monopoly.model.casilla;

import monopoly.model.jugador.IJugador;
import java.awt.Color;

/**
 * Representa una propiedad comprable en el tablero de Monopoly.
 * Incluye la lógica de compra, alquiler y construcción de casas/hoteles.
 */
public class Propiedad extends Casilla {
    private int precio;
    private int alquilerBase;
    private IJugador propietario;
    private Color grupoColor;
    private int casas;
    private boolean tieneHotel;

    // Multiplicadores de alquiler según número de casas
    private static final double[] MULTIPLICADORES_CASAS = { 1.0, 5.0, 15.0, 45.0, 80.0 }; // 0-4 casas
    private static final double MULTIPLICADOR_HOTEL = 125.0;

    public Propiedad(String nombre, int posicion, int precio, int alquiler, Color grupoColor) {
        super(nombre, posicion, TipoCasilla.PROPIEDAD);
        this.precio = precio;
        this.alquilerBase = alquiler;
        this.grupoColor = grupoColor;
        this.casas = 0;
        this.tieneHotel = false;
        this.propietario = null;
    }

    // ===== GETTERS BÁSICOS =====

    public int getPrecio() {
        return precio;
    }

    public int getAlquilerBase() {
        return alquilerBase;
    }

    public Color getGrupoColor() {
        return grupoColor;
    }

    // ===== GESTIÓN DE PROPIETARIO =====

    public IJugador getPropietario() {
        return propietario;
    }

    public void setPropietario(IJugador propietario) {
        this.propietario = propietario;
    }

    public boolean tienePropietario() {
        return propietario != null;
    }

    public boolean estaDisponible() {
        return propietario == null;
    }

    // ===== CÁLCULO DE ALQUILER =====

    /**
     * Calcula el alquiler actual de la propiedad.
     * El alquiler aumenta según el número de casas u hotel.
     * 
     * @return Alquiler a pagar
     */
    public int getAlquiler() {
        if (tieneHotel) {
            return (int) (alquilerBase * MULTIPLICADOR_HOTEL);
        } else if (casas > 0) {
            return (int) (alquilerBase * MULTIPLICADORES_CASAS[casas]);
        }
        return alquilerBase;
    }

    /**
     * Calcula el alquiler considerando si el propietario tiene el grupo completo.
     * 
     * @param tieneGrupoCompleto Si el propietario tiene todas las propiedades del
     *                           color
     * @return Alquiler a pagar (doble si tiene grupo completo sin casas)
     */
    public int calcularAlquiler(boolean tieneGrupoCompleto) {
        int alquilerCalculado = getAlquiler();

        // Si tiene grupo completo pero no tiene casas ni hotel, el alquiler se duplica
        if (tieneGrupoCompleto && casas == 0 && !tieneHotel) {
            alquilerCalculado *= 2;
        }

        return alquilerCalculado;
    }

    // ===== GESTIÓN DE CONSTRUCCIONES =====

    public int getCasas() {
        return casas;
    }

    public boolean tieneHotel() {
        return tieneHotel;
    }

    /**
     * Verifica si se puede agregar una casa.
     */
    public boolean puedeAgregarCasa() {
        return casas < 4 && !tieneHotel;
    }

    /**
     * Agrega una casa a la propiedad.
     * 
     * @return true si se agregó correctamente
     */
    public boolean agregarCasa() {
        if (puedeAgregarCasa()) {
            casas++;
            return true;
        }
        return false;
    }

    /**
     * Verifica si se puede construir un hotel.
     */
    public boolean puedeConstruirHotel() {
        return casas == 4 && !tieneHotel;
    }

    /**
     * Construye un hotel en la propiedad (requiere 4 casas).
     * 
     * @return true si se construyó correctamente
     */
    public boolean construirHotel() {
        if (puedeConstruirHotel()) {
            casas = 0;
            tieneHotel = true;
            return true;
        }
        return false;
    }

    /**
     * Obtiene el precio de construcción de una casa (50% del precio de la
     * propiedad).
     */
    public int getPrecioCasa() {
        return precio / 2;
    }

    /**
     * Obtiene el precio de construcción de un hotel (50% del precio de la
     * propiedad).
     */
    public int getPrecioHotel() {
        return precio / 2;
    }

    // ===== TRANSFERENCIA DE PROPIEDAD =====

    /**
     * Realiza la compra de la propiedad por un jugador.
     * 
     * @param comprador El jugador que compra
     * @return true si la compra fue exitosa
     */
    public boolean comprar(IJugador comprador) {
        if (estaDisponible() && comprador.puedePermitirse(precio)) {
            comprador.restarDinero(precio);
            comprador.agregarPropiedad(this);
            setPropietario(comprador);
            return true;
        }
        return false;
    }

    /**
     * Cobra el alquiler al jugador que cayó en la propiedad.
     * 
     * @param inquilino          El jugador que debe pagar
     * @param tieneGrupoCompleto Si el propietario tiene el grupo completo
     * @return El monto del alquiler cobrado
     */
    public int cobrarAlquiler(IJugador inquilino, boolean tieneGrupoCompleto) {
        if (!tienePropietario() || propietario.equals(inquilino)) {
            return 0; // No hay propietario o es el mismo jugador
        }

        int alquilerACobrar = calcularAlquiler(tieneGrupoCompleto);

        // Transferir dinero del inquilino al propietario
        inquilino.restarDinero(alquilerACobrar);
        propietario.sumarDinero(alquilerACobrar);

        return alquilerACobrar;
    }

    @Override
    public void ejecutarAccion() {
        // La lógica de alquiler se maneja desde el controlador
        // ya que necesita acceso a ambos jugadores
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getNombre()).append(" (").append(precio).append(" EUR)");
        if (tienePropietario()) {
            sb.append(" - Dueño: ").append(propietario.getNombre());
        }
        if (tieneHotel) {
            sb.append(" [HOTEL]");
        } else if (casas > 0) {
            sb.append(" [").append(casas).append(" casas]");
        }
        return sb.toString();
    }
}
