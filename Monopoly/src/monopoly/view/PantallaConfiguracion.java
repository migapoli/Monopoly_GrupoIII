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

    // Símbolos disponibles con emojis
    private static final String[] SIMBOLOS_DISPONIBLES = {
            "Sombrero 🎩", "Coche 🚗", "Perro 🐕", "Barco ⛵"
    };

    // Colores asociados a cada símbolo
    private static final Color[] COLORES_DISPONIBLES = {
            new Color(231, 76, 60), // Rojo coral
            new Color(52, 152, 219), // Azul brillante
            new Color(46, 204, 113), // Verde esmeralda
            new Color(241, 196, 15) // Amarillo dorado
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
        setSize(950, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Aplicar tema global
        MonopolyTheme.aplicarTemaGlobal();
    }

    private void inicializarComponentes() {
        // Panel principal con gradiente
        JPanel panelPrincipal = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Gradiente de fondo
                GradientPaint gradient = new GradientPaint(
                        0, 0, MonopolyTheme.FONDO_PRINCIPAL,
                        0, getHeight(), new Color(15, 20, 35));
                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        // Panel superior: Título
        JPanel panelTitulo = crearPanelTitulo();

        // Panel central: Configuración
        JPanel panelConfig = crearPanelConfiguracion();

        // Panel inferior: Botones y footer
        JPanel panelInferior = crearPanelInferior();

        panelPrincipal.add(panelTitulo, BorderLayout.NORTH);
        panelPrincipal.add(panelConfig, BorderLayout.CENTER);
        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);

        setContentPane(panelPrincipal);
    }

    private JPanel crearPanelTitulo() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(40, 0, 20, 0));

        // Panel para el título principal
        JPanel panelTextoTitulo = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panelTextoTitulo.setOpaque(false);

        // "MEGAPOLY:" en negrita con gradiente dorado
        JLabel lblMegapoly = new JLabel("MEGAPOLY: ") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                GradientPaint gradient = new GradientPaint(
                        0, 0, MonopolyTheme.ACENTO_DORADO,
                        0, getHeight(), MonopolyTheme.ACENTO_DORADO_OSCURO);
                g2.setPaint(gradient);
                g2.setFont(getFont());
                g2.drawString(getText(), 0, g2.getFontMetrics().getAscent());
                g2.dispose();
            }
        };
        lblMegapoly.setFont(MonopolyTheme.FUENTE_TITULO_GRANDE);

        // "THE GAME" en cursiva
        JLabel lblTheGame = new JLabel("THE GAME") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                g2.setColor(MonopolyTheme.TEXTO_PRINCIPAL);
                g2.setFont(getFont());
                g2.drawString(getText(), 0, g2.getFontMetrics().getAscent());
                g2.dispose();
            }
        };
        lblTheGame.setFont(new Font("Georgia", Font.BOLD | Font.ITALIC, 42));

        panelTextoTitulo.add(lblMegapoly);
        panelTextoTitulo.add(lblTheGame);

        // Línea decorativa dorada
        JPanel lineaDecorativa = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gradient = new GradientPaint(
                        50, 0, new Color(255, 215, 0, 0),
                        getWidth() / 2, 0, MonopolyTheme.ACENTO_DORADO);
                g2.setPaint(gradient);
                g2.setStroke(new BasicStroke(2));
                int y = getHeight() / 2;
                g2.drawLine(50, y, getWidth() / 2, y);

                gradient = new GradientPaint(
                        getWidth() / 2, 0, MonopolyTheme.ACENTO_DORADO,
                        getWidth() - 50, 0, new Color(255, 215, 0, 0));
                g2.setPaint(gradient);
                g2.drawLine(getWidth() / 2, y, getWidth() - 50, y);
            }
        };
        lineaDecorativa.setPreferredSize(new Dimension(800, 20));
        lineaDecorativa.setOpaque(false);

        // Subtítulo
        JLabel lblSubtitulo = new JLabel("Configuración de Partida");
        lblSubtitulo.setFont(MonopolyTheme.FUENTE_SUBTITULO);
        lblSubtitulo.setForeground(MonopolyTheme.TEXTO_SECUNDARIO);
        lblSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(panelTextoTitulo);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(lineaDecorativa);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(lblSubtitulo);

        return panel;
    }

    private JPanel crearPanelConfiguracion() {
        JPanel panel = new JPanel(new BorderLayout(10, 15));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(20, 50, 20, 50));

        // Panel para seleccionar número de jugadores
        JPanel panelNumJugadores = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelNumJugadores.setOpaque(false);

        JLabel lblNumJugadores = new JLabel("Número de jugadores:");
        lblNumJugadores.setFont(MonopolyTheme.FUENTE_SUBTITULO);
        lblNumJugadores.setForeground(MonopolyTheme.TEXTO_PRINCIPAL);

        // Máximo 4 jugadores
        Integer[] numeros = { 2, 3, 4 };
        comboNumJugadores = new JComboBox<>(numeros);
        comboNumJugadores.setSelectedItem(4);
        comboNumJugadores.setFont(MonopolyTheme.FUENTE_NORMAL);
        comboNumJugadores.setPreferredSize(new Dimension(70, 35));
        comboNumJugadores.setBackground(MonopolyTheme.FONDO_PANEL_CLARO);
        comboNumJugadores.setForeground(MonopolyTheme.TEXTO_OSCURO);
        comboNumJugadores.addActionListener(e -> actualizarPanelJugadores());

        panelNumJugadores.add(lblNumJugadores);
        panelNumJugadores.add(comboNumJugadores);

        // Panel para configurar jugadores
        panelJugadores = new JPanel();
        panelJugadores.setOpaque(false);

        JScrollPane scrollPane = new JScrollPane(panelJugadores);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

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
        gbc.insets = new Insets(12, 12, 12, 12);
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
        // Panel con efecto glass
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Fondo con trasparencia
                g2.setColor(new Color(37, 43, 61, 220));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                // Borde sutil
                g2.setColor(MonopolyTheme.BORDE_SUTIL);
                g2.setStroke(new BasicStroke(1));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);

                // Línea de acento arriba
                g2.setColor(COLORES_DISPONIBLES[(numero - 1) % COLORES_DISPONIBLES.length]);
                g2.fillRoundRect(0, 0, getWidth(), 4, 15, 15);
                g2.fillRect(0, 4, getWidth(), 2);

                g2.dispose();
            }
        };
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(25, 25, 20, 25));
        panel.setPreferredSize(new Dimension(380, 170));

        // Título del jugador
        JLabel lblTitulo = new JLabel("JUGADOR " + numero);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitulo.setForeground(COLORES_DISPONIBLES[(numero - 1) % COLORES_DISPONIBLES.length]);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(lblTitulo);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Panel Nombre
        JPanel panelNombre = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        panelNombre.setOpaque(false);
        panelNombre.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setFont(MonopolyTheme.FUENTE_NORMAL);
        lblNombre.setForeground(MonopolyTheme.TEXTO_SECUNDARIO);
        lblNombre.setPreferredSize(new Dimension(65, 25));

        JTextField txtNombre = crearCampoTextoEstilizado("Escribe tu nombre");
        camposNombres.add(txtNombre);

        panelNombre.add(lblNombre);
        panelNombre.add(txtNombre);

        panel.add(panelNombre);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Panel Símbolo
        JPanel panelSimbolo = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        panelSimbolo.setOpaque(false);
        panelSimbolo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JLabel lblSimbolo = new JLabel("Símbolo:");
        lblSimbolo.setFont(MonopolyTheme.FUENTE_NORMAL);
        lblSimbolo.setForeground(MonopolyTheme.TEXTO_SECUNDARIO);
        lblSimbolo.setPreferredSize(new Dimension(65, 25));

        JComboBox<String> comboSimbolo = new JComboBox<>(SIMBOLOS_DISPONIBLES);
        comboSimbolo.setSelectedIndex((numero - 1) % SIMBOLOS_DISPONIBLES.length);
        comboSimbolo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        comboSimbolo.setPreferredSize(new Dimension(200, 35));
        comboSimbolo.setBackground(MonopolyTheme.FONDO_PANEL_CLARO);
        comboSimbolo.setForeground(MonopolyTheme.TEXTO_OSCURO);
        combosSimbolos.add(comboSimbolo);

        panelSimbolo.add(lblSimbolo);
        panelSimbolo.add(comboSimbolo);

        panel.add(panelSimbolo);

        return panel;
    }

    private JTextField crearCampoTextoEstilizado(String placeholder) {
        JTextField campo = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(MonopolyTheme.FONDO_PANEL_CLARO);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);

                g2.dispose();
                super.paintComponent(g);
            }
        };
        campo.setFont(MonopolyTheme.FUENTE_NORMAL);
        campo.setForeground(MonopolyTheme.TEXTO_SECUNDARIO);
        campo.setCaretColor(MonopolyTheme.ACENTO_DORADO);
        campo.setOpaque(false);
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(MonopolyTheme.BORDE_SUTIL, 1, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        campo.setPreferredSize(new Dimension(200, 35));

        // Placeholder
        campo.setText(placeholder);
        campo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (campo.getText().equals(placeholder)) {
                    campo.setText("");
                    campo.setForeground(MonopolyTheme.TEXTO_PRINCIPAL);
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (campo.getText().isEmpty()) {
                    campo.setText(placeholder);
                    campo.setForeground(MonopolyTheme.TEXTO_SECUNDARIO);
                }
            }
        });

        return campo;
    }

    private JPanel crearPanelInferior() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(15, 0, 30, 0));

        // Panel del botón
        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        panelBoton.setOpaque(false);

        JButton btnIniciar = MonopolyTheme.crearBotonPrimario("INICIAR PARTIDA");
        btnIniciar.setPreferredSize(new Dimension(200, 50));
        btnIniciar.addActionListener(e -> iniciarPartida());

        JButton btnCargar = MonopolyTheme.crearBotonSecundario("CARGAR PARTIDA");
        btnCargar.setPreferredSize(new Dimension(200, 50));
        btnCargar.addActionListener(e -> cargarPartidaGuardada());

        panelBoton.add(btnIniciar);
        panelBoton.add(btnCargar);

        // Panel del footer
        JPanel panelFooter = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelFooter.setOpaque(false);
        panelFooter.setBorder(new EmptyBorder(25, 0, 0, 0));

        JLabel lblFooter = new JLabel("© 2024 Megapoly Inc. All rights reserved.");
        lblFooter.setFont(MonopolyTheme.FUENTE_PEQUENA);
        lblFooter.setForeground(MonopolyTheme.TEXTO_SECUNDARIO);

        panelFooter.add(lblFooter);

        panel.add(panelBoton);
        panel.add(panelFooter);

        return panel;
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
                partidas.get(0));

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
