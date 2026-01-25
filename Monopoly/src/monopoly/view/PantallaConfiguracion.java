package monopoly.view;

import monopoly.controller.ControladorJuego;
import monopoly.model.casilla.Casilla;
import monopoly.model.casilla.Propiedad;
import monopoly.model.jugador.Jugador;
import monopoly.model.persistencia.GestorArchivos;
import monopoly.model.persistencia.GestorArchivos.DatosPartidaGuardada;
import monopoly.model.tablero.Partida;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Pantalla de configuración inicial del juego Megapoly.
 * Permite seleccionar el número de jugadores, sus nombres y símbolos.
 */
public class PantallaConfiguracion extends JFrame {
    private Partida partida;
    private List<JTextField> camposNombres;
    private List<JComboBox<String>> combosSimbolos;
    private JComboBox<Integer> comboNumJugadores;
    private JPanel panelJugadores;
    
    // Colores del diseño
    private static final Color COLOR_FONDO = new Color(200, 200, 210);
    private static final Color COLOR_PANEL_JUGADOR = new Color(245, 245, 248);
    private static final Color COLOR_TITULO = new Color(35, 45, 65);
    private static final Color COLOR_BOTON = new Color(45, 55, 75);
    private static final Color COLOR_LINEA = new Color(45, 55, 75);
    
    // Símbolos disponibles con emojis
    private static final String[] SIMBOLOS_DISPONIBLES = {
        "Sombrero 🎩", "Coche 🚗", "Perro 🐕", "Barco ⛵"
    };
    
    // Colores asociados a cada símbolo
    private static final Color[] COLORES_DISPONIBLES = {
        Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW
    };
    
    public PantallaConfiguracion() {
        this.partida = new Partida();
        this.camposNombres = new ArrayList<>();
        this.combosSimbolos = new ArrayList<>();
        
        inicializarVentana();
        inicializarComponentes();
    }
    
    private void inicializarVentana() {
        setTitle("Megapoly - Configuración de Partida");
        setSize(900, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void inicializarComponentes() {
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(COLOR_FONDO);
        
        // Panel superior: Título
        JPanel panelTitulo = crearPanelTitulo();
        
        // Panel central: Configuración
        JPanel panelConfig = crearPanelConfiguracion();
        
        // Panel inferior: Botones y footer
        JPanel panelInferior = crearPanelInferior();
        
        panelPrincipal.add(panelTitulo, BorderLayout.NORTH);
        panelPrincipal.add(panelConfig, BorderLayout.CENTER);
        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);
        
        add(panelPrincipal);
    }
    
    private JPanel crearPanelTitulo() {
        JPanel panel = new JPanel();
        panel.setBackground(COLOR_FONDO);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(30, 0, 10, 0));
        
        // Panel para el título principal
        JPanel panelTextoTitulo = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panelTextoTitulo.setBackground(COLOR_FONDO);
        
        // "MEGAPOLY:" en negrita
        JLabel lblMegapoly = new JLabel("MEGAPOLY: ");
        lblMegapoly.setFont(new Font("Georgia", Font.BOLD, 48));
        lblMegapoly.setForeground(COLOR_TITULO);
        
        // "THE GAME" en cursiva
        JLabel lblTheGame = new JLabel("THE GAME");
        lblTheGame.setFont(new Font("Georgia", Font.BOLD | Font.ITALIC, 48));
        lblTheGame.setForeground(COLOR_TITULO);
        
        panelTextoTitulo.add(lblMegapoly);
        panelTextoTitulo.add(lblTheGame);
        
        // Línea decorativa
        JPanel lineaDecorativa = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(COLOR_LINEA);
                g2.setStroke(new BasicStroke(2));
                int y = getHeight() / 2;
                g2.drawLine(50, y, getWidth() - 50, y);
            }
        };
        lineaDecorativa.setPreferredSize(new Dimension(800, 20));
        lineaDecorativa.setBackground(COLOR_FONDO);
        
        panel.add(panelTextoTitulo);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(lineaDecorativa);
        
        return panel;
    }
    
    private JPanel crearPanelConfiguracion() {
        JPanel panel = new JPanel(new BorderLayout(10, 15));
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(new EmptyBorder(20, 40, 20, 40));
        
        // Panel para seleccionar número de jugadores
        JPanel panelNumJugadores = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelNumJugadores.setBackground(COLOR_FONDO);
        
        JLabel lblNumJugadores = new JLabel("Número de jugadores:");
        lblNumJugadores.setFont(new Font("Arial", Font.BOLD, 14));
        lblNumJugadores.setForeground(COLOR_TITULO);
        
        // Máximo 4 jugadores
        Integer[] numeros = {2, 3, 4};
        comboNumJugadores = new JComboBox<>(numeros);
        comboNumJugadores.setSelectedItem(4);
        comboNumJugadores.setFont(new Font("Arial", Font.PLAIN, 14));
        comboNumJugadores.setPreferredSize(new Dimension(60, 30));
        comboNumJugadores.addActionListener(e -> actualizarPanelJugadores());
        
        panelNumJugadores.add(lblNumJugadores);
        panelNumJugadores.add(comboNumJugadores);
        
        // Panel para configurar jugadores
        panelJugadores = new JPanel();
        panelJugadores.setBackground(COLOR_FONDO);
        
        JScrollPane scrollPane = new JScrollPane(panelJugadores);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(COLOR_FONDO);
        
        panel.add(panelNumJugadores, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Inicializar con 4 jugadores por defecto
        actualizarPanelJugadores();
        
        return panel;
    }
    
    private void actualizarPanelJugadores() {
        panelJugadores.removeAll();
        camposNombres.clear();
        combosSimbolos.clear();
        
        int numJugadores = (Integer) comboNumJugadores.getSelectedItem();
        
        // Calcular filas necesarias (2 jugadores por fila)
        int filas = (int) Math.ceil(numJugadores / 2.0);
        
        panelJugadores.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        
        int jugadorIndex = 0;
        for (int fila = 0; fila < filas; fila++) {
            for (int col = 0; col < 2 && jugadorIndex < numJugadores; col++) {
                JPanel panelJugador = crearPanelConfigJugador(jugadorIndex + 1);
                gbc.gridx = col;
                gbc.gridy = fila;
                panelJugadores.add(panelJugador, gbc);
                jugadorIndex++;
            }
        }
        
        panelJugadores.revalidate();
        panelJugadores.repaint();
    }
    
    private JPanel crearPanelConfigJugador(int numero) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(COLOR_PANEL_JUGADOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(180, 180, 185), 1, true),
            new EmptyBorder(20, 25, 20, 25)
        ));
        panel.setPreferredSize(new Dimension(350, 150));
        
        // Título del jugador
        JLabel lblTitulo = new JLabel("-- JUGADOR " + numero + ": --");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        lblTitulo.setForeground(COLOR_TITULO);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(lblTitulo);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Panel Nombre
        JPanel panelNombre = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        panelNombre.setBackground(COLOR_PANEL_JUGADOR);
        panelNombre.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        
        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setFont(new Font("Arial", Font.BOLD, 12));
        lblNombre.setForeground(COLOR_TITULO);
        lblNombre.setPreferredSize(new Dimension(60, 25));
        
        JTextField txtNombre = new JTextField();
        txtNombre.setFont(new Font("Arial", Font.PLAIN, 12));
        txtNombre.setPreferredSize(new Dimension(180, 28));
        txtNombre.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(2, 8, 2, 8)
        ));
        // Placeholder
        txtNombre.setText("Escribe tu nombre");
        txtNombre.setForeground(Color.GRAY);
        txtNombre.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (txtNombre.getText().equals("Escribe tu nombre")) {
                    txtNombre.setText("");
                    txtNombre.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (txtNombre.getText().isEmpty()) {
                    txtNombre.setText("Escribe tu nombre");
                    txtNombre.setForeground(Color.GRAY);
                }
            }
        });
        camposNombres.add(txtNombre);
        
        panelNombre.add(lblNombre);
        panelNombre.add(txtNombre);
        
        panel.add(panelNombre);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Panel Símbolo
        JPanel panelSimbolo = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        panelSimbolo.setBackground(COLOR_PANEL_JUGADOR);
        panelSimbolo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        
        JLabel lblSimbolo = new JLabel("Símbolo:");
        lblSimbolo.setFont(new Font("Arial", Font.BOLD, 12));
        lblSimbolo.setForeground(COLOR_TITULO);
        lblSimbolo.setPreferredSize(new Dimension(60, 25));
        
        JComboBox<String> comboSimbolo = new JComboBox<>(SIMBOLOS_DISPONIBLES);
        comboSimbolo.setSelectedIndex((numero - 1) % SIMBOLOS_DISPONIBLES.length);
        comboSimbolo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        comboSimbolo.setPreferredSize(new Dimension(180, 28));
        comboSimbolo.setBackground(Color.WHITE);
        combosSimbolos.add(comboSimbolo);
        
        panelSimbolo.add(lblSimbolo);
        panelSimbolo.add(comboSimbolo);
        
        panel.add(panelSimbolo);
        
        return panel;
    }
    
    private JPanel crearPanelInferior() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(new EmptyBorder(10, 0, 20, 0));
        
        // Panel del botón
        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBoton.setBackground(COLOR_FONDO);
        
        JButton btnIniciar = crearBotonAccion("INICIAR PARTIDA", 220);
        btnIniciar.addActionListener(e -> iniciarPartida());

        JButton btnCargar = crearBotonAccion("CARGAR PARTIDA", 220);
        btnCargar.addActionListener(e -> cargarPartidaGuardada());

        panelBoton.add(btnIniciar);
        panelBoton.add(Box.createRigidArea(new Dimension(15, 0)));
        panelBoton.add(btnCargar);
        
        // Panel del footer
        JPanel panelFooter = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelFooter.setBackground(COLOR_FONDO);
        panelFooter.setBorder(new EmptyBorder(30, 0, 0, 0));
        
        JLabel lblFooter = new JLabel("© 2024 Megapoly Inc. All rights reserved.");
        lblFooter.setFont(new Font("Arial", Font.PLAIN, 11));
        lblFooter.setForeground(new Color(100, 100, 110));
        
        panelFooter.add(lblFooter);
        
        panel.add(panelBoton);
        panel.add(panelFooter);
        
        return panel;
    }

    private JButton crearBotonAccion(String texto, int ancho) {
        JButton boton = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2.setColor(COLOR_BOTON.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(COLOR_BOTON.brighter());
                } else {
                    g2.setColor(COLOR_BOTON);
                }
                
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                
                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);
                
                g2.dispose();
            }
        };
        boton.setFont(new Font("Arial", Font.BOLD, 16));
        boton.setForeground(Color.WHITE);
        boton.setPreferredSize(new Dimension(ancho, 50));
        boton.setContentAreaFilled(false);
        boton.setBorderPainted(false);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return boton;
    }

    private void cargarPartidaGuardada() {
        List<String> partidas = GestorArchivos.listarPartidasGuardadas();
        if (partidas.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "No hay partidas guardadas en la carpeta 'saves'.",
                "Sin partidas",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String seleccion = (String) JOptionPane.showInputDialog(
            this,
            "Selecciona una partida guardada:",
            "Cargar Partida",
            JOptionPane.PLAIN_MESSAGE,
            null,
            partidas.toArray(new String[0]),
            partidas.get(0)
        );

        if (seleccion == null || seleccion.trim().isEmpty()) {
            return;
        }

        DatosPartidaGuardada datos = GestorArchivos.cargar(seleccion.trim());
        if (datos == null) {
            JOptionPane.showMessageDialog(this,
                "No se pudo cargar la partida seleccionada.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        Partida partidaCargada = reconstruirPartida(datos);
        if (partidaCargada == null) {
            JOptionPane.showMessageDialog(this,
                "La partida está corrupta o incompleta.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        ControladorJuego controlador = new ControladorJuego(partidaCargada);
        VentanaPrincipal ventana = new VentanaPrincipal(controlador);
        controlador.iniciarPartida();
        ventana.mostrar();
        this.dispose();
    }

    private Partida reconstruirPartida(DatosPartidaGuardada datos) {
        if (datos == null || datos.datosJugadores == null) {
            return null;
        }

        Partida partida = new Partida();
        Map<String, Jugador> jugadoresPorNombre = new HashMap<>();

        for (GestorArchivos.DatosJugador datoJugador : datos.datosJugadores) {
            if (datoJugador == null || datoJugador.nombre == null) {
                continue;
            }

            Color color = new Color(datoJugador.colorRGB);
            Jugador jugador = new Jugador(datoJugador.nombre, color);
            jugador.setDinero(datoJugador.dinero);
            jugador.setPosicion(datoJugador.posicion);
            jugador.setEnCarcel(datoJugador.enCarcel);
            if (datoJugador.enCarcel && datoJugador.turnosEnCarcel > 0) {
                for (int i = 0; i < datoJugador.turnosEnCarcel; i++) {
                    jugador.incrementarTurnosEnCarcel();
                }
            }

            partida.agregarJugador(jugador);
            jugadoresPorNombre.put(jugador.getNombre(), jugador);
        }

        // Limpiar historial generado por agregar jugadores
        partida.limpiarHistorial();

        // Restaurar propiedades
        for (GestorArchivos.DatosPropiedad datoProp : datos.propiedades) {
            Casilla casilla = partida.getTablero().getCasilla(datoProp.posicion);
            if (casilla instanceof Propiedad) {
                Propiedad propiedad = (Propiedad) casilla;

                for (int i = 0; i < datoProp.casas; i++) {
                    propiedad.agregarCasa();
                }

                if (datoProp.tieneHotel) {
                    while (propiedad.getCasas() < 4) {
                        propiedad.agregarCasa();
                    }
                    propiedad.construirHotel();
                }

                if (datoProp.nombrePropietario != null) {
                    Jugador propietario = jugadoresPorNombre.get(datoProp.nombrePropietario);
                    if (propietario != null) {
                        propiedad.setPropietario(propietario);
                        propietario.agregarPropiedad(propiedad);
                    }
                }
            }
        }

        // Restaurar historial
        for (String evento : datos.historial) {
            partida.agregarHistorial(evento);
        }

        partida.setTurnoActual(datos.turnoActual);
        partida.setJuegoTerminado(datos.juegoTerminado);

        return partida;
    }
    
    private void iniciarPartida() {
        // Validar nombres
        for (int i = 0; i < camposNombres.size(); i++) {
            String nombre = camposNombres.get(i).getText().trim();
            if (nombre.isEmpty() || nombre.equals("Escribe tu nombre")) {
                JOptionPane.showMessageDialog(this,
                    "Por favor, ingresa un nombre para el Jugador " + (i + 1),
                    "Error de validación",
                    JOptionPane.ERROR_MESSAGE);
                camposNombres.get(i).requestFocus();
                return;
            }
        }
        
        // Validar símbolos únicos
        List<Integer> simbolosUsados = new ArrayList<>();
        for (int i = 0; i < combosSimbolos.size(); i++) {
            int simboloIndex = combosSimbolos.get(i).getSelectedIndex();
            if (simbolosUsados.contains(simboloIndex)) {
                JOptionPane.showMessageDialog(this,
                    "Cada jugador debe tener un símbolo diferente. El Jugador " + (i + 1) + 
                    " tiene el mismo símbolo que otro jugador.",
                    "Error de validación",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            simbolosUsados.add(simboloIndex);
        }
        
        // Crear jugadores
        for (int i = 0; i < camposNombres.size(); i++) {
            String nombre = camposNombres.get(i).getText().trim();
            int indiceSimbolo = combosSimbolos.get(i).getSelectedIndex();
            Color color = COLORES_DISPONIBLES[indiceSimbolo];
            
            Jugador jugador = new Jugador(nombre, color);
            partida.agregarJugador(jugador);
        }
        
        // Crear controlador y ventana principal
        ControladorJuego controlador = new ControladorJuego(partida);
        VentanaPrincipal ventana = new VentanaPrincipal(controlador);
        
        // Iniciar partida
        controlador.iniciarPartida();
        
        // Mostrar ventana principal y cerrar configuración
        ventana.mostrar();
        this.dispose();
    }
}
