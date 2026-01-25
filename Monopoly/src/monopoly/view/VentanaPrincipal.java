package monopoly.view;

import monopoly.controller.ControladorJuego;
import monopoly.model.casilla.Propiedad;
import monopoly.model.jugador.IJugador;
import monopoly.model.persistencia.GestorArchivos;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class VentanaPrincipal extends JFrame implements ControladorJuego.ObservadorExtendido {
    private ControladorJuego controlador;
    private PanelTablero panelTablero;
    private PanelControles panelControles;
    private PanelInfoJugador panelInfoJugador;
    private PanelHistorial panelHistorial;
    private String nombrePartida;

    public VentanaPrincipal(ControladorJuego controlador) {
        this(controlador, null);
    }

    public VentanaPrincipal(ControladorJuego controlador, String nombrePartida) {
        this.controlador = controlador;
        this.nombrePartida = nombrePartida;
        this.controlador.agregarObservador(this);

        inicializarVentana();
        inicializarComponentes();
    }

    private void inicializarVentana() {
        setTitle(construirTituloVentana());
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

    private String construirTituloVentana() {
        String tituloBase = "Megapoly - The Game";
        if (nombrePartida != null && !nombrePartida.isBlank()) {
            return tituloBase + " - " + nombrePartida.trim();
        }
        return tituloBase;
    }

    private void inicializarComponentes() {
        // Crear menú superior
        setJMenuBar(crearBarraMenu());

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

        add(panelPrincipal, BorderLayout.CENTER);
    }

    private JMenuBar crearBarraMenu() {
        JMenuBar menuBar = new JMenuBar();

        // Menú Archivo
        JMenu menuArchivo = new JMenu("Archivo");

        JMenuItem itemGuardar = new JMenuItem("Guardar Partida");
        itemGuardar.addActionListener(e -> guardarPartida());

        JMenuItem itemSalir = new JMenuItem("Salir");
        itemSalir.addActionListener(e -> System.exit(0));

        menuArchivo.add(itemGuardar);
        menuArchivo.addSeparator();
        menuArchivo.add(itemSalir);

        // Menú Ayuda
        JMenu menuAyuda = new JMenu("Ayuda");
        JMenuItem itemAcerca = new JMenuItem("Acerca de Megapoly");
        itemAcerca.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Megapoly: The Game\nVersion 1.0\n\nDesarrollado por Grupo III",
                "Acerca de", JOptionPane.INFORMATION_MESSAGE));

        menuAyuda.add(itemAcerca);

        menuBar.add(menuArchivo);
        menuBar.add(menuAyuda);

        return menuBar;
    }

    private void guardarPartida() {
        String nombreArchivo = obtenerNombreParaGuardar();
        if (nombreArchivo == null || nombreArchivo.trim().isEmpty()) {
            return;
        }

        boolean exito = GestorArchivos.guardar(controlador.getPartida(), nombreArchivo.trim());
        if (exito) {
            nombrePartida = nombreArchivo.trim();
            setTitle(construirTituloVentana());
            JOptionPane.showMessageDialog(this, "Partida guardada correctamente.", "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
            panelHistorial.agregarMensaje("Partida guardada como '" + nombrePartida + "'");
        } else {
            JOptionPane.showMessageDialog(this, "Error al guardar la partida.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String obtenerNombreParaGuardar() {
        if (nombrePartida != null && !nombrePartida.isBlank()) {
            int opcion = JOptionPane.showConfirmDialog(this,
                    "¿Deseas sobrescribir la partida guardada '" + nombrePartida + "'?",
                    "Guardar Partida",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (opcion == JOptionPane.CANCEL_OPTION || opcion == JOptionPane.CLOSED_OPTION) {
                return null;
            }

            if (opcion == JOptionPane.YES_OPTION) {
                return nombrePartida;
            }
        }

        List<String> partidas = GestorArchivos.listarPartidasGuardadas();
        List<String> opciones = new ArrayList<>();
        opciones.add("Nueva partida...");
        opciones.addAll(partidas);

        String seleccion = (String) JOptionPane.showInputDialog(
                this,
                "Selecciona una partida para sobrescribir o crea una nueva:",
                "Guardar Partida",
                JOptionPane.PLAIN_MESSAGE,
                null,
                opciones.toArray(new String[0]),
                opciones.get(0));

        if (seleccion == null) {
            return null;
        }

        if ("Nueva partida...".equals(seleccion)) {
            String nombreNuevo = JOptionPane.showInputDialog(this,
                    "Nombre del archivo para guardar:",
                    "Guardar Partida", JOptionPane.PLAIN_MESSAGE);
            if (nombreNuevo == null) {
                return null;
            }
            return nombreNuevo.trim();
        }

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Deseas sobrescribir la partida guardada '" + seleccion + "'?",
                "Guardar Partida",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirmacion == JOptionPane.YES_OPTION) {
            return seleccion.trim();
        }

        return null;
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
    public void onCartaEvento(String tipoCarta, String descripcion, String efecto) {
        SwingUtilities.invokeLater(() -> {
            DialogoCartaEvento dialogo = new DialogoCartaEvento(this, tipoCarta, descripcion, efecto);
            dialogo.setVisible(true);
        });
    }

    @Override
    public void onPagoImpuesto(String nombreCasilla, int monto, IJugador jugador) {
        SwingUtilities.invokeLater(() -> {
            DialogoImpuesto dialogo = new DialogoImpuesto(this, nombreCasilla, monto, jugador);
            dialogo.setVisible(true);
        });
    }

    @Override
    public void onEstadoCarcel(IJugador jugador, int turnos) {
        SwingUtilities.invokeLater(() -> {
            DialogoCarcel dialogo = new DialogoCarcel(this, jugador, turnos);
            dialogo.setVisible(true);

            if (dialogo.isPagarFianza()) {
                controlador.pagarFianza();
            } else if (dialogo.isIntentarDobles()) {
                controlador.intentarDobles();
            }
        });
    }
}
