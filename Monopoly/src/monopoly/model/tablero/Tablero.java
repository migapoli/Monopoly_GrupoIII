package monopoly.model.tablero;

import monopoly.model.casilla.Casilla;
import monopoly.model.casilla.Casilla.TipoCasilla;
import monopoly.model.casilla.Propiedad;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Tablero {
    private List<Casilla> casillas;
    private static final int TOTAL_CASILLAS = 40;
    
    public Tablero() {
        casillas = new ArrayList<>();
        inicializarCasillas();
    }
    
    private void inicializarCasillas() {
        // Casilla 0: Salida
        casillas.add(new Casilla("Salida", 0, TipoCasilla.SALIDA));
        
        // Casillas 1-39: Propiedades, servicios, estaciones, etc.
        casillas.add(new Propiedad("Avenida Mediterráneo", 1, 60, 2, new Color(139, 69, 19)));
        casillas.add(new Casilla("Caja de Comunidad", 2, TipoCasilla.COMUNIDAD));
        casillas.add(new Propiedad("Avenida Báltico", 3, 60, 4, new Color(139, 69, 19)));
        casillas.add(new Casilla("Impuesto sobre la Renta", 4, TipoCasilla.IMPUESTO));
        casillas.add(new Propiedad("Estación Reading", 5, 200, 25, Color.BLACK));
        casillas.add(new Propiedad("Avenida Oriental", 6, 100, 6, new Color(173, 216, 230)));
        casillas.add(new Casilla("Suerte", 7, TipoCasilla.SUERTE));
        casillas.add(new Propiedad("Avenida Vermont", 8, 100, 6, new Color(173, 216, 230)));
        casillas.add(new Propiedad("Avenida Connecticut", 9, 120, 8, new Color(173, 216, 230)));
        
        // Casilla 10: Cárcel
        casillas.add(new Casilla("Cárcel", 10, TipoCasilla.CARCEL));
        
        casillas.add(new Propiedad("Avenida St. Charles", 11, 140, 10, new Color(255, 0, 255)));
        casillas.add(new Propiedad("Compañía Eléctrica", 12, 150, 0, Color.WHITE));
        casillas.add(new Propiedad("Avenida Estados", 13, 140, 10, new Color(255, 0, 255)));
        casillas.add(new Propiedad("Avenida Virginia", 14, 160, 12, new Color(255, 0, 255)));
        casillas.add(new Propiedad("Estación Pennsylvania", 15, 200, 25, Color.BLACK));
        casillas.add(new Propiedad("Avenida St. James", 16, 180, 14, Color.ORANGE));
        casillas.add(new Casilla("Caja de Comunidad", 17, TipoCasilla.COMUNIDAD));
        casillas.add(new Propiedad("Avenida Tennessee", 18, 180, 14, Color.ORANGE));
        casillas.add(new Propiedad("Avenida New York", 19, 200, 16, Color.ORANGE));
        
        // Casilla 20: Parking gratuito
        casillas.add(new Casilla("Parking Gratuito", 20, TipoCasilla.PARKING));
        
        casillas.add(new Propiedad("Avenida Kentucky", 21, 220, 18, Color.RED));
        casillas.add(new Casilla("Suerte", 22, TipoCasilla.SUERTE));
        casillas.add(new Propiedad("Avenida Indiana", 23, 220, 18, Color.RED));
        casillas.add(new Propiedad("Avenida Illinois", 24, 240, 20, Color.RED));
        casillas.add(new Propiedad("Estación B&O", 25, 200, 25, Color.BLACK));
        casillas.add(new Propiedad("Avenida Atlantic", 26, 260, 22, Color.YELLOW));
        casillas.add(new Propiedad("Avenida Ventnor", 27, 260, 22, Color.YELLOW));
        casillas.add(new Propiedad("Compañía de Aguas", 28, 150, 0, Color.WHITE));
        casillas.add(new Propiedad("Avenida Marvin Gardens", 29, 280, 24, Color.YELLOW));
        
        // Casilla 30: Ir a la cárcel
        casillas.add(new Casilla("Ir a la Cárcel", 30, TipoCasilla.IR_CARCEL));
        
        casillas.add(new Propiedad("Avenida Pacific", 31, 300, 26, Color.GREEN));
        casillas.add(new Propiedad("Avenida North Carolina", 32, 300, 26, Color.GREEN));
        casillas.add(new Casilla("Caja de Comunidad", 33, TipoCasilla.COMUNIDAD));
        casillas.add(new Propiedad("Avenida Pennsylvania", 34, 320, 28, Color.GREEN));
        casillas.add(new Propiedad("Estación Short Line", 35, 200, 25, Color.BLACK));
        casillas.add(new Casilla("Suerte", 36, TipoCasilla.SUERTE));
        casillas.add(new Propiedad("Park Place", 37, 350, 35, Color.BLUE));
        casillas.add(new Casilla("Impuesto de Lujo", 38, TipoCasilla.IMPUESTO));
        casillas.add(new Propiedad("Boardwalk", 39, 400, 50, Color.BLUE));
    }
    
    public Casilla getCasilla(int posicion) {
        if (posicion >= 0 && posicion < casillas.size()) {
            return casillas.get(posicion);
        }
        return null;
    }
    
    public List<Casilla> getCasillas() {
        return new ArrayList<>(casillas);
    }
    
    public int getTotalCasillas() {
        return TOTAL_CASILLAS;
    }
}
