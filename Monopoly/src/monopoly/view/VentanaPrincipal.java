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
    private PanelTableroImagen panelTablero;
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
        setSize(1500, 950);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0, 0));

        // Aplicar tema global
        MonopolyTheme.aplicarTemaGlobal();
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

        // Panel principal con fondo oscuro
        JPanel panelPrincipal = new JPanel(new BorderLayout(0, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(MonopolyTheme.FONDO_PRINCIPAL);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        // Crear los paneles
        panelTablero = new PanelTableroImagen(controlador);
        panelControles = new PanelControles(controlador);
        panelInfoJugador = new PanelInfoJugador(controlador);
        panelHistorial = new PanelHistorial(controlador);

        // Panel izquierdo: Tablero
        JPanel panelIzquierdo = new JPanel(new BorderLayout());
        panelIzquierdo.setOpaque(false);
        panelIzquierdo.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 10));
        panelIzquierdo.add(panelTablero, BorderLayout.CENTER);

        // Panel derecho: Información + Juego + Historial
        JPanel panelDerecho = new JPanel(new BorderLayout(0, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(30, 36, 50));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        panelDerecho.setPreferredSize(new Dimension(400, 0));
        panelDerecho.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 15));

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
        menuBar.setBackground(MonopolyTheme.HEADER_OSCURO);
        menuBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Menú Archivo
        JMenu menuArchivo = new JMenu("Archivo");
        menuArchivo.setForeground(MonopolyTheme.TEXTO_OSCURO);
        menuArchivo.setFont(MonopolyTheme.FUENTE_NORMAL);

        JMenuItem itemGuardar = new JMenuItem("Guardar Partida");
        itemGuardar.setBackground(MonopolyTheme.FONDO_PANEL);
        itemGuardar.setForeground(MonopolyTheme.TEXTO_OSCURO);
        itemGuardar.addActionListener(e -> guardarPartida());

        JMenuItem itemSalir = new JMenuItem("Salir");
        itemSalir.setBackground(MonopolyTheme.FONDO_PANEL);
        itemSalir.setForeground(MonopolyTheme.TEXTO_OSCURO);
        itemSalir.addActionListener(e -> System.exit(0));

        menuArchivo.add(itemGuardar);
        menuArchivo.addSeparator();
        menuArchivo.add(itemSalir);

        // Menú Ayuda
        JMenu menuAyuda = new JMenu("Ayuda");
        menuAyuda.setForeground(MonopolyTheme.TEXTO_OSCURO);
        menuAyuda.setFont(MonopolyTheme.FUENTE_NORMAL);

        JMenuItem itemAcerca = new JMenuItem("Acerca de Megapoly");
        itemAcerca.setBackground(MonopolyTheme.FONDO_PANEL);
        itemAcerca.setForeground(MonopolyTheme.TEXTO_OSCURO);
        itemAcerca.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Megapoly: The Game\nVersion 2.0\n\nDesarrollado por Grupo III",
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
