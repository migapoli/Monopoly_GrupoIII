package monopoly.model.jugador;

import monopoly.model.casilla.Propiedad;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Jugador implements IJugador {
    private String nombre;
    private int dinero;
    private int posicion;
    private Color color;
    private List<Propiedad> propiedades;
    private boolean enCarcel;
    private int turnosEnCarcel;
    
    public Jugador(String nombre, Color color) {
        this.nombre = nombre;
        this.color = color;
        this.dinero = 1500; // Dinero inicial en Monopoly
        this.posicion = 0; // Empieza en la casilla de Salida
        this.propiedades = new ArrayList<>();
        this.enCarcel = false;
        this.turnosEnCarcel = 0;
    }
    
    @Override
    public String getNombre() {
        return nombre;
    }
    
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
    public int getPosicion() {
        return posicion;
    }
    
    @Override
    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }
    
    @Override
    public Color getColor() {
        return color;
    }
    
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
}
