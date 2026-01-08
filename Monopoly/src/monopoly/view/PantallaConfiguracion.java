package monopoly.view;

import monopoly.controller.ControladorJuego;
import monopoly.model.jugador.Jugador;
import monopoly.model.tablero.Partida;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PantallaConfiguracion extends JFrame {
    private Partida partida;
    private List<JTextField> camposNombres;
    private List<JComboBox<String>> combosColores;
    private JSpinner spinnerJugadores;
    private JPanel panelJugadores;
    
    private static final Color[] COLORES_DISPONIBLES = {
        Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW,
        new Color(255, 0, 255), new Color(255, 165, 0) // Magenta y Naranja
    };
    
    private static final String[] NOMBRES_COLORES = {
        "Rojo", "Azul", "Verde", "Amarillo", "Magenta", "Naranja"
    };
    
    public PantallaConfiguracion() {
        this.partida = new Partida();
        this.camposNombres = new ArrayList<>();
        this.combosColores = new ArrayList<>();
        
        inicializarVentana();
        inicializarComponentes();
    }
    
    private void inicializarVentana() {
        setTitle("Monopoly - Configuración de Partida");
        setSize(600, 700);
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
        JPanel panelPrincipal = new JPanel(new BorderLayout(15, 15));
        panelPrincipal.setBorder(new EmptyBorder(20, 20, 20, 20));
        panelPrincipal.setBackground(new Color(240, 248, 255));
        
        // Panel superior: Título
        JPanel panelTitulo = crearPanelTitulo();
        
        // Panel central: Configuración
        JPanel panelConfig = crearPanelConfiguracion();
        
        // Panel inferior: Botones
        JPanel panelBotones = crearPanelBotones();
        
        panelPrincipal.add(panelTitulo, BorderLayout.NORTH);
        panelPrincipal.add(panelConfig, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
        
        add(panelPrincipal);
    }
    
    private JPanel crearPanelTitulo() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(240, 248, 255));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        JLabel lblTitulo = new JLabel("MEGAPOLY: The Game");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 40));
        lblTitulo.setForeground(Color.BLACK);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(lblTitulo);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        return panel;
    }
    
    private JPanel crearPanelConfiguracion() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(240, 248, 255));
        
        // Panel para seleccionar número de jugadores
        JPanel panelNumJugadores = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelNumJugadores.setBackground(new Color(240, 248, 255));
        
        JLabel lblNumJugadores = new JLabel("Número de jugadores:");
        lblNumJugadores.setFont(new Font("Arial", Font.PLAIN, 14));
        
        spinnerJugadores = new JSpinner(new SpinnerNumberModel(2, 2, 6, 1));
        spinnerJugadores.setFont(new Font("Arial", Font.PLAIN, 14));
        spinnerJugadores.setPreferredSize(new Dimension(80, 30));
        spinnerJugadores.addChangeListener(e -> actualizarPanelJugadores());
        
        panelNumJugadores.add(lblNumJugadores);
        panelNumJugadores.add(spinnerJugadores);
        
        // Panel para configurar jugadores
        panelJugadores = new JPanel();
        panelJugadores.setLayout(new BoxLayout(panelJugadores, BoxLayout.Y_AXIS));
        panelJugadores.setBackground(new Color(240, 248, 255));
        panelJugadores.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(panelJugadores);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(null);
        scrollPane.setBackground(new Color(240, 248, 255));
        
        panel.add(panelNumJugadores, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Inicializar con 2 jugadores
        actualizarPanelJugadores();
        
        return panel;
    }
    
    private void actualizarPanelJugadores() {
        panelJugadores.removeAll();
        camposNombres.clear();
        combosColores.clear();
        
        int numJugadores = (Integer) spinnerJugadores.getValue();
        
        // Crear una cuadrícula de 2 columnas para los jugadores
        JPanel gridPanel = new JPanel(new GridLayout(0, 2, 15, 15));
        gridPanel.setBackground(new Color(240, 248, 255));
        
        for (int i = 0; i < numJugadores; i++) {
            JPanel panelJugador = crearPanelConfigJugador(i + 1);
            gridPanel.add(panelJugador);
        }
        
        panelJugadores.add(gridPanel);
        panelJugadores.add(Box.createVerticalGlue());
        
        panelJugadores.revalidate();
        panelJugadores.repaint();
    }
    
    private JPanel crearPanelConfigJugador(int numero) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(220, 220, 220));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        // Título del jugador
        JLabel lblTitulo = new JLabel("-- JUGADOR " + numero + ": --");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 12));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(lblTitulo);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Panel Nombre
        JPanel panelNombre = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panelNombre.setBackground(new Color(220, 220, 220));
        
        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setFont(new Font("Arial", Font.PLAIN, 12));
        
        JTextField txtNombre = new JTextField("Jugador " + numero, 12);
        txtNombre.setFont(new Font("Arial", Font.PLAIN, 12));
        camposNombres.add(txtNombre);
        
        panelNombre.add(lblNombre);
        panelNombre.add(txtNombre);
        
        panel.add(panelNombre);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Panel Símbolo
        JPanel panelSimbolo = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panelSimbolo.setBackground(new Color(220, 220, 220));
        
        JLabel lblSimbolo = new JLabel("Símbolo:");
        lblSimbolo.setFont(new Font("Arial", Font.PLAIN, 12));
        
        JComboBox<String> comboSimbolo = new JComboBox<>(NOMBRES_COLORES);
        comboSimbolo.setSelectedIndex(numero - 1);
        comboSimbolo.setFont(new Font("Arial", Font.PLAIN, 12));
        comboSimbolo.setRenderer(new ColorComboBoxRenderer());
        combosColores.add(comboSimbolo);
        
        panelSimbolo.add(lblSimbolo);
        panelSimbolo.add(comboSimbolo);
        
        panel.add(panelSimbolo);
        
        return panel;
    }
    
    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panel.setBackground(new Color(240, 248, 255));
        
        JButton btnIniciar = new JButton("INICIAR PARTIDA");
        btnIniciar.setFont(new Font("Arial", Font.BOLD, 14));
        btnIniciar.setBackground(new Color(45, 50, 70));
        btnIniciar.setForeground(Color.WHITE);
        btnIniciar.setFocusPainted(false);
        btnIniciar.setOpaque(true);
        btnIniciar.setBorderPainted(false);
        btnIniciar.setPreferredSize(new Dimension(200, 45));
        btnIniciar.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        btnIniciar.addActionListener(e -> iniciarPartida());
        
        panel.add(btnIniciar);
        
        return panel;
    }
    
    private void iniciarPartida() {
        // Validar nombres
        for (int i = 0; i < camposNombres.size(); i++) {
            String nombre = camposNombres.get(i).getText().trim();
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Por favor, ingresa un nombre para el Jugador " + (i + 1),
                    "Error de validación",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        // Crear jugadores
        for (int i = 0; i < camposNombres.size(); i++) {
            String nombre = camposNombres.get(i).getText().trim();
            int indiceColor = combosColores.get(i).getSelectedIndex();
            Color color = COLORES_DISPONIBLES[indiceColor];
            
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
    
    // Renderer personalizado para el ComboBox de colores
    private class ColorComboBoxRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value,
                                                     int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(
                list, value, index, isSelected, cellHasFocus);
            
            if (index >= 0 && index < COLORES_DISPONIBLES.length) {
                // Crear un panel con el color
                JPanel colorPanel = new JPanel();
                colorPanel.setPreferredSize(new Dimension(20, 20));
                colorPanel.setBackground(COLORES_DISPONIBLES[index]);
                colorPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                
                label.setIcon(new ColorIcon(COLORES_DISPONIBLES[index]));
            }
            
            return label;
        }
    }
    
    // Icono personalizado para mostrar colores
    private class ColorIcon implements Icon {
        private Color color;
        
        public ColorIcon(Color color) {
            this.color = color;
        }
        
        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            g.setColor(color);
            g.fillRect(x, y, getIconWidth(), getIconHeight());
            g.setColor(Color.BLACK);
            g.drawRect(x, y, getIconWidth(), getIconHeight());
        }
        
        @Override
        public int getIconWidth() {
            return 20;
        }
        
        @Override
        public int getIconHeight() {
            return 20;
        }
    }
}
