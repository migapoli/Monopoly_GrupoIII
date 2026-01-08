package monopoly.model.casilla;

public class Casilla implements ICasilla {
    private String nombre;
    private int posicion;
    private TipoCasilla tipo;
    
    public enum TipoCasilla {
        SALIDA, PROPIEDAD, SUERTE, COMUNIDAD, IMPUESTO, 
        CARCEL, PARKING, IR_CARCEL, SERVICIO, ESTACION
    }
    
    public Casilla(String nombre, int posicion, TipoCasilla tipo) {
        this.nombre = nombre;
        this.posicion = posicion;
        this.tipo = tipo;
    }
    
    @Override
    public String getNombre() {
        return nombre;
    }
    
    @Override
    public int getPosicion() {
        return posicion;
    }
    
    @Override
    public TipoCasilla getTipo() {
        return tipo;
    }
    
    @Override
    public void ejecutarAccion() {
        // Implementación base
    }
}
