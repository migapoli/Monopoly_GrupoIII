package monopoly.view;

import monopoly.controller.ControladorJuego;
import monopoly.controller.IControladorJuego;
import monopoly.model.casilla.Casilla;
import monopoly.model.casilla.Propiedad;
import monopoly.model.jugador.IJugador;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame implements ControladorJuego.ObservadorExtendido {
    private ControladorJuego controlador;
    private PanelTablero panelTablero;
    private PanelControles panelControles;
    private PanelInfoJugador panelInfoJugador;
    private PanelHistorial panelHistorial;
    
    public VentanaPrincipal(ControladorJuego controlador) {
        this.controlador = controlador;
        this.controlador.agregarObservador(this);
        
        inicializarVentana();
        inicializarComponentes();
    }
    
    private void inicializarVentana() {
        setTitle("Megapoly - The Game");
        setSize(1400, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0, 0));
        
        // Establecer look and feel del sistema
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void inicializarComponentes() {
        // Crear menú superior
        JPanel menuSuperior = crearMenuSuperior();
        
        // Panel principal
        JPanel panelPrincipal = new JPanel(new BorderLayout(0, 0));
        
        // Crear los paneles
        panelTablero = new PanelTablero(controlador);
        panelControles = new PanelControles(controlador);
        panelInfoJugador = new PanelInfoJugador(controlador);
        panelHistorial = new PanelHistorial(controlador);
        
        // Panel izquierdo: Tablero
        JPanel panelIzquierdo = new JPanel(new BorderLayout());
        panelIzquierdo.add(panelTablero, BorderLayout.CENTER);
        
        // Panel derecho: Información + Juego + Historial
        JPanel panelDerecho = new JPanel(new BorderLayout(0, 0));
        panelDerecho.setPreferredSize(new Dimension(380, 0));
        panelDerecho.setBackground(Color.WHITE);
        
        panelDerecho.add(panelInfoJugador, BorderLayout.NORTH);
        panelDerecho.add(panelControles, BorderLayout.CENTER);
        panelDerecho.add(panelHistorial, BorderLayout.SOUTH);
        
        // Agregar paneles al panel principal
        panelPrincipal.add(panelIzquierdo, BorderLayout.CENTER);
        panelPrincipal.add(panelDerecho, BorderLayout.EAST);
        
        add(menuSuperior, BorderLayout.NORTH);
        add(panelPrincipal, BorderLayout.CENTER);
    }
    
    private JPanel crearMenuSuperior() {
        JPanel menu = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        menu.setBackground(new Color(180, 180, 180));
        menu.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));
        
        JLabel lblMenu = new JLabel("BARRA DE MENÚ DE OPCIONES");
        lblMenu.setFont(new Font("Arial", Font.BOLD, 14));
        menu.add(lblMenu);
        
        return menu;
    }
    
    public void mostrar() {
        setVisible(true);
    }
    
    // Implementación de ObservadorJuego
    @Override
    public void onActualizacionJuego() {
        SwingUtilities.invokeLater(() -> {
            panelTablero.actualizar();
            panelInfoJugador.actualizar();
            panelHistorial.actualizar();
        });
    }
    
    @Override
    public void onCambioTurno(IJugador jugador) {
        SwingUtilities.invokeLater(() -> {
            panelInfoJugador.actualizar();
            panelControles.actualizar();
        });
    }
    
    @Override
    public void onDadosLanzados(int dado1, int dado2) {
        SwingUtilities.invokeLater(() -> {
            panelControles.mostrarDados(dado1, dado2);
            panelTablero.actualizar();
        });
    }
    
    @Override
    public void onPropiedadComprada(Propiedad propiedad, IJugador jugador) {
        SwingUtilities.invokeLater(() -> {
            panelTablero.actualizar();
            panelInfoJugador.actualizar();
        });
    }
    
    @Override
    public void onMensaje(String mensaje) {
        SwingUtilities.invokeLater(() -> {
            panelHistorial.agregarMensaje(mensaje);
        });
    }
    
    @Override
    public void onOfertaPropiedad(Propiedad propiedad, IJugador jugador) {
        SwingUtilities.invokeLater(() -> {
            DialogoComprarPropiedad dialogo = new DialogoComprarPropiedad(this, propiedad, jugador);
            dialogo.setVisible(true);
            
            if (dialogo.isComprar()) {
                controlador.comprarPropiedad();
            }
        });
    }
    
    @Override
    public void onCartaEvento(String tipoCarta) {
        SwingUtilities.invokeLater(() -> {
            String descripcion = "Has sacado una carta de " + tipoCarta + ". " +
                "Esta carta tiene un efecto especial sobre tu jugador.";
            String efecto = tipoCarta.equals("SUERTE") ? 
                "Avanza 3 casillas" : "Cobra 50 EUR del banco";
            
            DialogoCartaEvento dialogo = new DialogoCartaEvento(this, tipoCarta, descripcion, efecto);
            dialogo.setVisible(true);
        });
    }
}
