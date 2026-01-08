package monopoly.view;

import monopoly.controller.IJuegoControllerPuro;
import monopoly.controller.IObservadorJuegoPuro;
import monopoly.view.dto.JugadorDTO;
import monopoly.view.dto.PropiedadDTO;
import monopoly.view.dto.TableroDTO;
import monopoly.view.dto.CasillaDTO;

import javax.swing.*;
import java.awt.*;

/**
 * VENTANA PRINCIPAL PURA - Implementación correcta del patrón MVC.
 * 
 * ═══════════════════════════════════════════════════════════════════
 * 🎯 REGLAS DEL "HIJO" (Vista)
 * ═══════════════════════════════════════════════════════════════════
 * 
 * 1. La Vista SOLO conoce al Controller (el "Padre")
 * 2. La Vista NUNCA importa clases del paquete .model
 * 3. La Vista recibe datos a través de DTOs
 * 4. La Vista solicita acciones al Controller, NO ejecuta lógica
 * 
 * ═══════════════════════════════════════════════════════════════════
 * 
 * IMPORTS PERMITIDOS:
 * ✅ monopoly.controller.*
 * ✅ monopoly.view.dto.*
 * ✅ javax.swing.*
 * ✅ java.awt.*
 * 
 * IMPORTS PROHIBIDOS:
 * ❌ monopoly.model.* (CUALQUIER clase del modelo)
 * ❌ monopoly.model.jugador.*
 * ❌ monopoly.model.casilla.*
 * ❌ monopoly.model.tablero.*
 * 
 * ═══════════════════════════════════════════════════════════════════
 */
public class VentanaPrincipalPura extends JFrame implements IObservadorJuegoPuro {

    // ===== REFERENCIA AL PADRE (Controller) =====
    private final IJuegoControllerPuro controller;

    // ===== COMPONENTES DE UI =====
    private PanelTableroPuro panelTablero;
    private JPanel panelInfoJugador;
    private JTextArea areaHistorial;
    private JButton btnTirarDados;
    private JButton btnFinalizarTurno;
    private JLabel lblDado1;
    private JLabel lblDado2;
    private JLabel lblJugadorActual;
    private JLabel lblDinero;

    // ===== ESTADO LOCAL DE LA VISTA (cache de DTOs) =====
    private TableroDTO estadoActual;

    public VentanaPrincipalPura(IJuegoControllerPuro controller) {
        this.controller = controller;
        this.controller.agregarObservador(this);

        inicializarVentana();
        inicializarComponentes();
        configurarEventos();
    }

    private void inicializarVentana() {
        setTitle("Megapoly - The Game (MVC Puro)");
        setSize(1400, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
    }

    private void inicializarComponentes() {
        // Panel del tablero (puro)
        panelTablero = new PanelTableroPuro();

        // Panel de controles
        JPanel panelControles = crearPanelControles();

        // Panel de información del jugador
        panelInfoJugador = crearPanelInfoJugador();

        // Panel de historial
        JPanel panelHistorial = crearPanelHistorial();

        // Panel derecho
        JPanel panelDerecho = new JPanel(new BorderLayout(0, 10));
        panelDerecho.setPreferredSize(new Dimension(350, 0));
        panelDerecho.add(panelInfoJugador, BorderLayout.NORTH);
        panelDerecho.add(panelControles, BorderLayout.CENTER);
        panelDerecho.add(panelHistorial, BorderLayout.SOUTH);

        // Agregar al frame
        add(panelTablero, BorderLayout.CENTER);
        add(panelDerecho, BorderLayout.EAST);
    }

    private JPanel crearPanelControles() {
        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Controles"));

        // Dados
        JPanel panelDados = new JPanel(new FlowLayout());
        lblDado1 = new JLabel("?", SwingConstants.CENTER);
        lblDado1.setPreferredSize(new Dimension(50, 50));
        lblDado1.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        lblDado1.setFont(new Font("Arial", Font.BOLD, 24));

        lblDado2 = new JLabel("?", SwingConstants.CENTER);
        lblDado2.setPreferredSize(new Dimension(50, 50));
        lblDado2.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        lblDado2.setFont(new Font("Arial", Font.BOLD, 24));

        panelDados.add(lblDado1);
        panelDados.add(lblDado2);

        // Botones
        btnTirarDados = new JButton("🎲 Tirar Dados");
        btnTirarDados.setFont(new Font("Arial", Font.BOLD, 16));

        btnFinalizarTurno = new JButton("✓ Finalizar Turno");
        btnFinalizarTurno.setFont(new Font("Arial", Font.BOLD, 16));

        panel.add(panelDados);
        panel.add(btnTirarDados);
        panel.add(btnFinalizarTurno);

        return panel;
    }

    private JPanel crearPanelInfoJugador() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Jugador Actual"));
        panel.setPreferredSize(new Dimension(350, 120));

        lblJugadorActual = new JLabel("Esperando...", SwingConstants.CENTER);
        lblJugadorActual.setFont(new Font("Arial", Font.BOLD, 18));

        lblDinero = new JLabel("$0", SwingConstants.CENTER);
        lblDinero.setFont(new Font("Arial", Font.PLAIN, 16));

        panel.add(lblJugadorActual);
        panel.add(lblDinero);

        return panel;
    }

    private JPanel crearPanelHistorial() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Historial"));
        panel.setPreferredSize(new Dimension(350, 200));

        areaHistorial = new JTextArea();
        areaHistorial.setEditable(false);
        areaHistorial.setFont(new Font("Consolas", Font.PLAIN, 12));

        JScrollPane scroll = new JScrollPane(areaHistorial);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Configura los eventos de los botones.
     * La Vista DELEGA las acciones al Controller - no ejecuta lógica.
     */
    private void configurarEventos() {
        // Al hacer clic en "Tirar Dados", delegamos al Controller
        btnTirarDados.addActionListener(e -> {
            // ✅ CORRECTO: La Vista pide al Controller que ejecute la acción
            controller.tirarDados();
            // La Vista NO mueve jugadores, NO calcula posiciones, NO accede al Modelo
        });

        // Al hacer clic en "Finalizar Turno", delegamos al Controller
        btnFinalizarTurno.addActionListener(e -> {
            // ✅ CORRECTO: La Vista pide al Controller que ejecute la acción
            controller.finalizarTurno();
        });
    }

    public void mostrar() {
        setVisible(true);
    }

    // ═══════════════════════════════════════════════════════════════════
    // IMPLEMENTACIÓN DE IObservadorJuegoPuro
    // (Recibe DTOs del Controller, NUNCA objetos del Modelo)
    // ═══════════════════════════════════════════════════════════════════

    @Override
    public void onActualizacionJuego(TableroDTO tableroDTO) {
        // Guardamos el DTO para usarlo en la UI
        this.estadoActual = tableroDTO;

        SwingUtilities.invokeLater(() -> {
            // Actualizamos el tablero con el DTO
            panelTablero.actualizarConDTO(tableroDTO);
        });
    }

    @Override
    public void onCambioTurno(JugadorDTO jugadorDTO) {
        SwingUtilities.invokeLater(() -> {
            // ✅ Usamos el DTO, no accedemos al Modelo
            lblJugadorActual.setText(jugadorDTO.getNombre());
            lblJugadorActual.setForeground(jugadorDTO.getColor());
            lblDinero.setText("$" + jugadorDTO.getDinero());

            // Reset de dados
            lblDado1.setText("?");
            lblDado2.setText("?");
        });
    }

    @Override
    public void onDadosLanzados(int dado1, int dado2, int nuevaPosicion, String nombreCasilla) {
        SwingUtilities.invokeLater(() -> {
            // ✅ Recibimos valores primitivos, no objetos del Modelo
            lblDado1.setText(String.valueOf(dado1));
            lblDado2.setText(String.valueOf(dado2));
        });
    }

    @Override
    public void onPropiedadComprada(PropiedadDTO propiedadDTO, String compradorNombre) {
        SwingUtilities.invokeLater(() -> {
            // ✅ Usamos el DTO para mostrar información
            panelTablero.repaint();

            // Actualizar info del jugador (pedimos al controller el estado actual)
            JugadorDTO jugadorActual = controller.obtenerJugadorActual();
            lblDinero.setText("$" + jugadorActual.getDinero());
        });
    }

    @Override
    public void onMensaje(String mensaje) {
        SwingUtilities.invokeLater(() -> {
            areaHistorial.append(mensaje + "\n");
            areaHistorial.setCaretPosition(areaHistorial.getDocument().getLength());
        });
    }

    @Override
    public void onOfertaPropiedad(PropiedadDTO propiedadDTO, JugadorDTO jugadorDTO) {
        SwingUtilities.invokeLater(() -> {
            // ✅ Creamos diálogo usando DTOs, no el Modelo
            int opcion = JOptionPane.showConfirmDialog(
                    this,
                    String.format("¿Deseas comprar %s por $%d?\n\nTu dinero: $%d\nAlquiler: $%d",
                            propiedadDTO.getNombre(),
                            propiedadDTO.getPrecio(),
                            jugadorDTO.getDinero(),
                            propiedadDTO.getAlquiler()),
                    "Comprar Propiedad",
                    JOptionPane.YES_NO_OPTION);

            if (opcion == JOptionPane.YES_OPTION) {
                // ✅ CORRECTO: Delegamos la compra al Controller
                controller.comprarPropiedad();
            } else {
                controller.rechazarCompra();
            }
        });
    }

    @Override
    public void onCartaEvento(String tipoCarta, String descripcion, String efecto) {
        SwingUtilities.invokeLater(() -> {
            // ✅ Recibimos strings, no objetos del Modelo
            JOptionPane.showMessageDialog(
                    this,
                    descripcion + "\n\nEfecto: " + efecto,
                    "Carta de " + tipoCarta,
                    JOptionPane.INFORMATION_MESSAGE);
        });
    }
}
