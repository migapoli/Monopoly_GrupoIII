package monopoly.view.dto;

import java.awt.Color;

/**
 * Data Transfer Object para Casilla.
 * Representa cualquier tipo de casilla (propiedad, suerte, comunidad, especial)
 * de forma que la Vista pueda renderizarla sin conocer el Modelo.
 */
public class CasillaDTO {
    
    public enum TipoCasillaDTO {
        SALIDA, PROPIEDAD, SUERTE, COMUNIDAD, CARCEL, PARKING, IR_CARCEL, 
        IMPUESTO, ESTACION, SERVICIO
    }
    
    private final int posicion;
    private final String nombre;
    private final TipoCasillaDTO tipo;
    private final Color colorGrupo;     // null si no es propiedad
    private final Integer precio;        // null si no es propiedad
    private final String propietario;    // null si no tiene dueño
    private final Color colorPropietario; // null si no tiene dueño
    
    public CasillaDTO(int posicion, String nombre, TipoCasillaDTO tipo, 
                      Color colorGrupo, Integer precio, String propietario, 
                      Color colorPropietario) {
        this.posicion = posicion;
        this.nombre = nombre;
        this.tipo = tipo;
        this.colorGrupo = colorGrupo;
        this.precio = precio;
        this.propietario = propietario;
        this.colorPropietario = colorPropietario;
    }
    
    // Getters
    public int getPosicion() { return posicion; }
    public String getNombre() { return nombre; }
    public TipoCasillaDTO getTipo() { return tipo; }
    public Color getColorGrupo() { return colorGrupo; }
    public Integer getPrecio() { return precio; }
    public String getPropietario() { return propietario; }
    public Color getColorPropietario() { return colorPropietario; }
    
    public boolean esPropiedad() { 
        return tipo == TipoCasillaDTO.PROPIEDAD || 
               tipo == TipoCasillaDTO.ESTACION || 
               tipo == TipoCasillaDTO.SERVICIO; 
    }
    public boolean tienePropietario() { return propietario != null; }
}
