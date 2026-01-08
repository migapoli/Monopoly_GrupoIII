package monopoly.model.jugador;

import java.util.Random;

public class Dado {
    private Random random;
    private int ultimoValor1;
    private int ultimoValor2;
    
    public Dado() {
        this.random = new Random();
        this.ultimoValor1 = 1;
        this.ultimoValor2 = 1;
    }
    
    public int[] lanzar() {
        ultimoValor1 = random.nextInt(6) + 1;
        ultimoValor2 = random.nextInt(6) + 1;
        return new int[]{ultimoValor1, ultimoValor2};
    }
    
    public int getUltimoValor1() {
        return ultimoValor1;
    }
    
    public int getUltimoValor2() {
        return ultimoValor2;
    }
    
    public int getSuma() {
        return ultimoValor1 + ultimoValor2;
    }
    
    public boolean esDoble() {
        return ultimoValor1 == ultimoValor2;
    }
}
