package test;

import monopoly.model.tablero.Partida;
import monopoly.model.jugador.Jugador;
import monopoly.model.jugador.IJugador;
import monopoly.model.casilla.Casilla;
import monopoly.model.casilla.Propiedad;
import monopoly.model.casilla.Casilla.TipoCasilla;
import monopoly.controller.ControladorJuego;
import java.awt.Color;
import java.util.List;

public class TestJuego {

    private static int testsPasados = 0;
    private static int testsFallados = 0;

    public static void main(String[] args) {
        System.out.println("=== INICIANDO SUITE DE PRUEBAS MONOPOLY ===");

        testInicializacionPartida();
        testMovimientoJugador();
        testPasoPorSalida();
        testCompraPropiedad();
        testPagoAlquiler();
        testBancarrota();

        System.out.println("\n=== RESUMEN DE PRUEBAS ===");
        System.out.println("Pruebas Pasadas: " + testsPasados);
        System.out.println("Pruebas Falladas: " + testsFallados);

        if (testsFallados == 0) {
            System.out.println("✅ TODOS LOS TEST PASARON CORRECTAMENTE");
        } else {
            System.out.println("❌ ALGUNOS TEST FALLARON");
            System.exit(1);
        }
    }

    private static void assertEquals(Object esperado, Object actual, String nombreTest) {
        if ((esperado == null && actual == null) || (esperado != null && esperado.equals(actual))) {
            System.out.println("✅ [PASS] " + nombreTest);
            testsPasados++;
        } else {
            System.out.println("❌ [FAIL] " + nombreTest + " - Esperado: " + esperado + ", Actual: " + actual);
            testsFallados++;
        }
    }

    private static void assertTrue(boolean condicion, String nombreTest) {
        if (condicion) {
            System.out.println("✅ [PASS] " + nombreTest);
            testsPasados++;
        } else {
            System.out.println("❌ [FAIL] " + nombreTest);
            testsFallados++;
        }
    }

    // 1. Test Iniciar Partida y Jugadores
    private static void testInicializacionPartida() {
        System.out.println("\n--- Test Inicialización ---");
        Partida partida = new Partida();
        Jugador j1 = new Jugador("Jugador 1", Color.RED);
        Jugador j2 = new Jugador("Jugador 2", Color.BLUE);

        partida.agregarJugador(j1);
        partida.agregarJugador(j2);

        assertEquals(2, partida.getNumeroJugadores(), "Número de jugadores correcto");
        assertEquals(0, partida.getTurnoActual(), "Turno inicial es 0");
        assertEquals(1500, j1.getDinero(), "Dinero inicial correcto");
        assertEquals(0, j1.getPosicion(), "Posición inicial correcta (Salida)");
    }

    // 2. Test Movimiento
    private static void testMovimientoJugador() {
        System.out.println("\n--- Test Movimiento ---");
        Partida partida = new Partida();
        Jugador j1 = new Jugador("MoverTest", Color.RED);
        partida.agregarJugador(j1);

        // Mover 5 casillas
        j1.mover(5, 40);
        assertEquals(5, j1.getPosicion(), "Movimiento básico correcto");

        // Mover 3 más
        j1.mover(3, 40);
        assertEquals(8, j1.getPosicion(), "Movimiento acumulativo correcto");
    }

    // 3. Test Paso por Salida
    private static void testPasoPorSalida() {
        System.out.println("\n--- Test Paso por Salida ---");
        Partida partida = new Partida();
        Jugador j1 = new Jugador("SalidaTest", Color.GREEN);
        j1.setDinero(1000); // Ponemos una cantidad conocida
        partida.agregarJugador(j1);

        // Colocar en la casilla 39
        j1.setPosicion(39);

        // Mover 2 casillas (debe pasar a la 1)
        j1.mover(2, 40);

        assertEquals(1, j1.getPosicion(), "Posición tras vuelta correcta");
        assertTrue(j1.pasoPorSalida(), "Flag pasoPorSalida activado");
        assertEquals(1200, j1.getDinero(), "Cobro de 200 por salida");
    }

    // 4. Test Compra de Propiedad
    private static void testCompraPropiedad() {
        System.out.println("\n--- Test Compra Propiedad ---");
        Partida partida = new Partida();
        Jugador j1 = new Jugador("Comprador", Color.YELLOW);
        partida.agregarJugador(j1);

        // Mover a casilla 1 (Avenida Mediterráneo, precio 60)
        j1.setPosicion(1);
        Casilla casilla = partida.getTablero().getCasilla(1);

        assertTrue(casilla instanceof Propiedad, "La casilla 1 es Propiedad");

        if (casilla instanceof Propiedad) {
            Propiedad prop = (Propiedad) casilla;
            ControladorJuego controlador = new ControladorJuego(partida);

            // Simular compra
            assertTrue(prop.estaDisponible(), "Propiedad inicialmente disponible");

            // Forzamos compra manual o vía controlador si es posible sin UI
            // Usamos lógica directa de modelo para test unitario
            assertTrue(j1.puedePermitirse(prop.getPrecio()), "Tiene dinero suficiente");

            boolean compraExitosa = prop.comprar(j1);
            assertTrue(compraExitosa, "Compra ejecutada correctamente");

            assertEquals(j1, prop.getPropietario(), "Propietario asignado");
            assertEquals(1500 - 60, j1.getDinero(), "Dinero descontado (1500 - 60)");
            assertEquals(1, j1.getPropiedades().size(), "Propiedad añadida a lista del jugador");
        }
    }

    // 5. Test Pago Alquiler
    private static void testPagoAlquiler() {
        System.out.println("\n--- Test Pago Alquiler ---");
        Partida partida = new Partida();
        Jugador propietario = new Jugador("Propietario", Color.RED);
        Jugador vistante = new Jugador("Visitante", Color.BLUE);

        partida.agregarJugador(propietario);
        partida.agregarJugador(vistante);

        // Propietario compra casilla 1 (Mediterráneo, Alquiler base 2)
        Propiedad prop = (Propiedad) partida.getTablero().getCasilla(1);
        prop.comprar(propietario);

        // Visitante cae en casilla 1
        vistante.setPosicion(1);

        // Verificar cálculo alquiler
        int alquiler = prop.cobrarAlquiler(vistante, false); // false = no tiene grupo completo

        // Realizar pago (lógica manual similar al controlador)
        // NOTA: cobrarAlquiler YA REALIZA LA TRANSFERENCIA en el modelo Propiedad.java

        assertEquals(2, alquiler, "Alquiler base correcto");
        assertEquals(1500 - 2, vistante.getDinero(), "Dinero visitante descontado");
        assertEquals(1500 - 60 + 2, propietario.getDinero(), "Dinero propietario recibido (desc. compra + alquiler)");
    }

    // 6. Test Bancarrota
    private static void testBancarrota() {
        System.out.println("\n--- Test Bancarrota ---");
        Partida partida = new Partida();
        Jugador pobre = new Jugador("Pobre", Color.GRAY);
        Jugador rico = new Jugador("Rico", Color.YELLOW);

        partida.agregarJugador(pobre);
        partida.agregarJugador(rico);

        pobre.setDinero(10); // Muy poco dinero

        // Rico compra propiedad cara (Boardwalk: 39, Alquiler 50)
        Propiedad prop = (Propiedad) partida.getTablero().getCasilla(39);
        prop.comprar(rico);

        // Pobre cae en Boardwalk
        pobre.setPosicion(39);
        int alquiler = prop.cobrarAlquiler(pobre, false);

        assertTrue(alquiler > 10, "El alquiler es mayor que el dinero del jugador");
        // pobre.restarDinero(alquiler); // No necesario, ya se cobró

        assertTrue(pobre.estaBancarrota(), "Jugador en estado de bancarrota");

        // Verificar gestión de eliminación en partida
        partida.verificarBancarrota(pobre);

        assertEquals(1, partida.getJugadores().size(), "Solo queda 1 jugador activo");
        assertEquals(rico, partida.getJugadores().get(0), "El jugador restante es Rico");

        partida.verificarVictoria();
        assertEquals(rico, partida.getGanador(), "Ganador identificado correctamente");
    }
}
