package monopoly.view.dto;

import java.awt.Color;

/**
 * Data Transfer Object para Propiedad.
 * La Vista utiliza este DTO para mostrar información de propiedades
 * SIN conocer la implementación real del Modelo.
 */
public class PropiedadDTO {
    private final int posicion;
    private final String nombre;
    private final int precio;
    private final int alquiler;
    private final Color colorGrupo;
    private final String nombrePropietario; // null si no tiene dueño
    private final boolean disponible;
    
    public PropiedadDTO(int posicion, String nombre, int precio, int alquiler, 
                        Color colorGrupo, String nombrePropietario, boolean disponible) {
        this.posicion = posicion;
        this.nombre = nombre;
        this.precio = precio;
        this.alquiler = alquiler;
        this.colorGrupo = colorGrupo;
        this.nombrePropietario = nombrePropietario;
        this.disponible = disponible;
    }
    
    // Solo getters - objeto inmutable
    public int getPosicion() { return posicion; }
    public String getNombre() { return nombre; }
    public int getPrecio() { return precio; }
    public int getAlquiler() { return alquiler; }
    public Color getColorGrupo() { return colorGrupo; }
    public String getNombrePropietario() { return nombrePropietario; }
    public boolean isDisponible() { return disponible; }
    public boolean tienePropietario() { return nombrePropietario != null; }
}
