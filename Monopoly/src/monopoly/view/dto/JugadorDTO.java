package monopoly.view.dto;

import java.awt.Color;
import java.util.List;

/**
 * Data Transfer Object para Jugador.
 * La Vista SOLO conoce este DTO, NUNCA la clase Jugador del Modelo.
 * Este es el "contrato" que el Controller expone a la Vista.
 */
public class JugadorDTO {
    private final String nombre;
    private final int dinero;
    private final int posicion;
    private final Color color;
    private final boolean esTurnoActivo;
    private final List<String> nombresPropiedades;
    
    public JugadorDTO(String nombre, int dinero, int posicion, Color color, 
                      boolean esTurnoActivo, List<String> nombresPropiedades) {
        this.nombre = nombre;
        this.dinero = dinero;
        this.posicion = posicion;
        this.color = color;
        this.esTurnoActivo = esTurnoActivo;
        this.nombresPropiedades = nombresPropiedades;
    }
    
    // Solo getters - inmutable
    public String getNombre() { return nombre; }
    public int getDinero() { return dinero; }
    public int getPosicion() { return posicion; }
    public Color getColor() { return color; }
    public boolean isEsTurnoActivo() { return esTurnoActivo; }
    public List<String> getNombresPropiedades() { return nombresPropiedades; }
}
