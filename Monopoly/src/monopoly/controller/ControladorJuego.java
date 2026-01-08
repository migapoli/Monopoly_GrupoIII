package monopoly.controller;

import monopoly.model.casilla.Casilla;
import monopoly.model.casilla.Propiedad;
import monopoly.model.jugador.Dado;
import monopoly.model.jugador.IJugador;
import monopoly.model.tablero.Partida;

import java.util.ArrayList;
import java.util.List;

public class ControladorJuego implements IControladorJuego {
    private Partida partida;
    private List<ObservadorJuego> observadores;
    private boolean dadosLanzados;
    
    public ControladorJuego() {
        this.partida = new Partida();
        this.observadores = new ArrayList<>();
        this.dadosLanzados = false;
    }
    
    public ControladorJuego(Partida partida) {
        this.partida = partida;
        this.observadores = new ArrayList<>();
        this.dadosLanzados = false;
    }
    
    @Override
    public void iniciarPartida() {
        if (partida.getJugadores().size() < 2) {
            notificarMensaje("Se necesitan al menos 2 jugadores para iniciar");
            return;
        }
        notificarMensaje("¡Partida iniciada!");
        notificarCambioTurno(partida.getJugadorActual());
        notificarActualizacion();
    }
    
    @Override
    public void tirarDados() {
        if (!dadosLanzados) {
            IJugador jugadorActual = partida.getJugadorActual();
            Dado dado = partida.getDado();
            int[] valores = dado.lanzar();
            
            dadosLanzados = true;
            notificarDadosLanzados(valores[0], valores[1]);
            
            // Mover al jugador
            int nuevaPosicion = (jugadorActual.getPosicion() + dado.getSuma()) % 40;
            
            // Verificar si pasó por la salida
            if (nuevaPosicion < jugadorActual.getPosicion()) {
                jugadorActual.sumarDinero(200);
                notificarMensaje(jugadorActual.getNombre() + " pasó por la Salida y cobró $200");
            }
            
            jugadorActual.setPosicion(nuevaPosicion);
            Casilla casillaActual = partida.getTablero().getCasilla(nuevaPosicion);
            
            String mensaje = String.format("%s sacó %d + %d = %d y cayó en %s", 
                jugadorActual.getNombre(), valores[0], valores[1], 
                dado.getSuma(), casillaActual.getNombre());
            
            partida.agregarHistorial(mensaje);
            notificarMensaje(mensaje);
            
            // Verificar el tipo de casilla y ejecutar acción
            ejecutarAccionCasilla(casillaActual, jugadorActual);
            
            notificarActualizacion();
        } else {
            notificarMensaje("Ya has lanzado los dados este turno");
        }
    }
    
    private void ejecutarAccionCasilla(Casilla casilla, IJugador jugador) {
        if (casilla instanceof Propiedad) {
            Propiedad propiedad = (Propiedad) casilla;
            if (!propiedad.tienePropietario()) {
                // Notificar que debe decidir si comprar
                notificarOfertaPropiedad(propiedad, jugador);
            } else if (propiedad.getPropietario() != jugador) {
                // Pagar alquiler
                int alquiler = propiedad.getAlquiler();
                jugador.restarDinero(alquiler);
                propiedad.getPropietario().sumarDinero(alquiler);
                String msg = String.format("%s pagó $%d de alquiler a %s", 
                    jugador.getNombre(), alquiler, propiedad.getPropietario().getNombre());
                partida.agregarHistorial(msg);
                notificarMensaje(msg);
            }
        } else if (casilla.getTipo() == Casilla.TipoCasilla.SUERTE || 
                   casilla.getTipo() == Casilla.TipoCasilla.COMUNIDAD) {
            // Mostrar carta
            String tipoCarta = casilla.getTipo() == Casilla.TipoCasilla.SUERTE ? "SUERTE" : "COMUNIDAD";
            notificarCartaEvento(tipoCarta);
        }
    }
    
    private void notificarOfertaPropiedad(Propiedad propiedad, IJugador jugador) {
        for (ObservadorJuego obs : observadores) {
            if (obs instanceof ObservadorExtendido) {
                ((ObservadorExtendido) obs).onOfertaPropiedad(propiedad, jugador);
            }
        }
    }
    
    private void notificarCartaEvento(String tipoCarta) {
        for (ObservadorJuego obs : observadores) {
            if (obs instanceof ObservadorExtendido) {
                ((ObservadorExtendido) obs).onCartaEvento(tipoCarta);
            }
        }
    }
    
    public interface ObservadorExtendido extends ObservadorJuego {
        void onOfertaPropiedad(Propiedad propiedad, IJugador jugador);
        void onCartaEvento(String tipoCarta);
    }
    
    @Override
    public void comprarPropiedad() {
        IJugador jugadorActual = partida.getJugadorActual();
        Casilla casillaActual = partida.getTablero().getCasilla(jugadorActual.getPosicion());
        
        if (casillaActual instanceof Propiedad) {
            Propiedad propiedad = (Propiedad) casillaActual;
            
            if (!propiedad.tienePropietario()) {
                if (jugadorActual.getDinero() >= propiedad.getPrecio()) {
                    jugadorActual.restarDinero(propiedad.getPrecio());
                    propiedad.setPropietario(jugadorActual);
                    jugadorActual.agregarPropiedad(propiedad);
                    
                    String mensaje = String.format("%s compró %s por $%d", 
                        jugadorActual.getNombre(), propiedad.getNombre(), propiedad.getPrecio());
                    
                    partida.agregarHistorial(mensaje);
                    notificarPropiedadComprada(propiedad, jugadorActual);
                    notificarMensaje(mensaje);
                    notificarActualizacion();
                } else {
                    notificarMensaje("No tienes suficiente dinero para comprar esta propiedad");
                }
            } else {
                notificarMensaje("Esta propiedad ya tiene dueño");
            }
        } else {
            notificarMensaje("No puedes comprar esta casilla");
        }
    }
    
    @Override
    public void finalizarTurno() {
        if (dadosLanzados) {
            partida.siguienteTurno();
            dadosLanzados = false;
            IJugador siguienteJugador = partida.getJugadorActual();
            
            String mensaje = "Turno de " + siguienteJugador.getNombre();
            partida.agregarHistorial(mensaje);
            notificarCambioTurno(siguienteJugador);
            notificarMensaje(mensaje);
            notificarActualizacion();
        } else {
            notificarMensaje("Debes lanzar los dados antes de finalizar tu turno");
        }
    }
    
    @Override
    public Partida getPartida() {
        return partida;
    }
    
    @Override
    public void agregarObservador(ObservadorJuego observador) {
        if (!observadores.contains(observador)) {
            observadores.add(observador);
        }
    }
    
    @Override
    public void eliminarObservador(ObservadorJuego observador) {
        observadores.remove(observador);
    }
    
    private void notificarActualizacion() {
        for (ObservadorJuego obs : observadores) {
            obs.onActualizacionJuego();
        }
    }
    
    private void notificarCambioTurno(IJugador jugador) {
        for (ObservadorJuego obs : observadores) {
            obs.onCambioTurno(jugador);
        }
    }
    
    private void notificarDadosLanzados(int dado1, int dado2) {
        for (ObservadorJuego obs : observadores) {
            obs.onDadosLanzados(dado1, dado2);
        }
    }
    
    private void notificarPropiedadComprada(Propiedad propiedad, IJugador jugador) {
        for (ObservadorJuego obs : observadores) {
            obs.onPropiedadComprada(propiedad, jugador);
        }
    }
    
    private void notificarMensaje(String mensaje) {
        for (ObservadorJuego obs : observadores) {
            obs.onMensaje(mensaje);
        }
    }
}
