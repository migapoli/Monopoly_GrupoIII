package monopoly.model.casilla;

import monopoly.model.jugador.IJugador;
import java.awt.Color;

public class Propiedad extends Casilla {
    private int precio;
    private int alquiler;
    private IJugador propietario;
    private Color grupoColor;
    private int casas;
    private boolean tieneHotel;
    
    public Propiedad(String nombre, int posicion, int precio, int alquiler, Color grupoColor) {
        super(nombre, posicion, TipoCasilla.PROPIEDAD);
        this.precio = precio;
        this.alquiler = alquiler;
        this.grupoColor = grupoColor;
        this.casas = 0;
        this.tieneHotel = false;
        this.propietario = null;
    }
    
    public int getPrecio() {
        return precio;
    }
    
    public int getAlquiler() {
        return alquiler;
    }
    
    public IJugador getPropietario() {
        return propietario;
    }
    
    public void setPropietario(IJugador propietario) {
        this.propietario = propietario;
    }
    
    public boolean tienePropietario() {
        return propietario != null;
    }
    
    public Color getGrupoColor() {
        return grupoColor;
    }
    
    public int getCasas() {
        return casas;
    }
    
    public void agregarCasa() {
        if (casas < 4 && !tieneHotel) {
            casas++;
        }
    }
    
    public boolean tieneHotel() {
        return tieneHotel;
    }
    
    public void construirHotel() {
        if (casas == 4) {
            casas = 0;
            tieneHotel = true;
        }
    }
    
    @Override
    public void ejecutarAccion() {
        // Lógica para cobrar alquiler si tiene propietario
    }
}
