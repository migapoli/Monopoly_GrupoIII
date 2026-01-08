package monopoly.view;

import monopoly.controller.ControladorJuego;
import monopoly.model.casilla.Casilla;
import monopoly.model.casilla.Propiedad;
import monopoly.model.jugador.IJugador;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class PanelInfoJugador extends JPanel {
    private ControladorJuego controlador;
    private JPanel panelJugadores;
    
    public PanelInfoJugador(ControladorJuego controlador) {
        this.controlador = controlador;
        inicializarComponentes();
    }
    
    private void inicializarComponentes() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(0, 250));
        
        // Título del panel
        JLabel lblTitulo = new JLabel("PANEL DE INFORMACIÓN");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        // Panel con scroll para la información
        panelJugadores = new JPanel();
        panelJugadores.setLayout(new BoxLayout(panelJugadores, BoxLayout.Y_AXIS));
        panelJugadores.setBackground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(panelJugadores);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        add(lblTitulo, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        
        actualizar();
    }
    
    public void actualizar() {
        panelJugadores.removeAll();
        
        List<IJugador> jugadores = controlador.getPartida().getJugadores();
        IJugador jugadorActual = controlador.getPartida().getJugadorActual();
        
        if (jugadorActual != null) {
            JPanel infoActual = crearInfoJugadorActual(jugadorActual);
            panelJugadores.add(infoActual);
        }
        
        panelJugadores.add(Box.createVerticalGlue());
        
        revalidate();
        repaint();
    }
    
    private JPanel crearInfoJugadorActual(IJugador jugador) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        
        // Turno actual
        JLabel lblTurno = new JLabel("TURNO ACTUAL: " + jugador.getNombre() + " - *Símbolo*");
        lblTurno.setFont(new Font("Arial", Font.BOLD, 12));
        lblTurno.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Posición actual
        Casilla casillaActual = controlador.getPartida().getTablero().getCasilla(jugador.getPosicion());
        JLabel lblPosicion = new JLabel("• Posición actual: " + casillaActual.getNombre());
        lblPosicion.setFont(new Font("Arial", Font.PLAIN, 11));
        lblPosicion.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Dinero
        JLabel lblDinero = new JLabel("• Dinero acumulado: " + jugador.getDinero() + " EUR");
        lblDinero.setFont(new Font("Arial", Font.PLAIN, 11));
        lblDinero.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Propiedades
        JLabel lblPropiedadesTitulo = new JLabel("• Propiedades:");
        lblPropiedadesTitulo.setFont(new Font("Arial", Font.PLAIN, 11));
        lblPropiedadesTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panel.add(lblTurno);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        panel.add(lblPosicion);
        panel.add(Box.createRigidArea(new Dimension(0, 3)));
        panel.add(lblDinero);
        panel.add(Box.createRigidArea(new Dimension(0, 3)));
        panel.add(lblPropiedadesTitulo);
        
        // Lista de propiedades
        if (!jugador.getPropiedades().isEmpty()) {
            JTextArea areaPropiedades = new JTextArea();
            areaPropiedades.setEditable(false);
            areaPropiedades.setBackground(Color.WHITE);
            areaPropiedades.setFont(new Font("Arial", Font.PLAIN, 10));
            areaPropiedades.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < jugador.getPropiedades().size(); i++) {
                Propiedad prop = jugador.getPropiedades().get(i);
                sb.append("-- ").append(prop.getNombre()).append(" → X Casas\n");
            }
            areaPropiedades.setText(sb.toString());
            areaPropiedades.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            panel.add(Box.createRigidArea(new Dimension(0, 5)));
            panel.add(areaPropiedades);
        }
        
        return panel;
    }
    
    private JPanel crearPanelJugador(IJugador jugador, boolean esTurnoActual) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(esTurnoActual ? 
                new Color(255, 215, 0) : Color.GRAY, esTurnoActual ? 3 : 1),
            new EmptyBorder(10, 10, 10, 10)
        ));
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        
        // Panel izquierdo: Color y nombre
        JPanel panelIzquierdo = new JPanel(new BorderLayout(5, 5));
        panelIzquierdo.setBackground(Color.WHITE);
        
        // Indicador de color del jugador
        JPanel indicadorColor = new JPanel();
        indicadorColor.setPreferredSize(new Dimension(30, 30));
        indicadorColor.setBackground(jugador.getColor());
        indicadorColor.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        
        // Nombre del jugador
        JLabel lblNombre = new JLabel(jugador.getNombre());
        lblNombre.setFont(new Font("Arial", Font.BOLD, 14));
        if (esTurnoActual) {
            lblNombre.setText("▶ " + jugador.getNombre());
            lblNombre.setForeground(new Color(0, 100, 0));
        }
        
        panelIzquierdo.add(indicadorColor, BorderLayout.WEST);
        panelIzquierdo.add(lblNombre, BorderLayout.CENTER);
        
        // Panel central: Información
        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        panelInfo.setBackground(Color.WHITE);
        
        JLabel lblDinero = new JLabel("💰 Dinero: $" + jugador.getDinero());
        lblDinero.setFont(new Font("Arial", Font.PLAIN, 12));
        
        JLabel lblPosicion = new JLabel("📍 Posición: " + jugador.getPosicion());
        lblPosicion.setFont(new Font("Arial", Font.PLAIN, 12));
        
        JLabel lblPropiedades = new JLabel("🏠 Propiedades: " + jugador.getPropiedades().size());
        lblPropiedades.setFont(new Font("Arial", Font.PLAIN, 12));
        
        panelInfo.add(lblDinero);
        panelInfo.add(Box.createRigidArea(new Dimension(0, 3)));
        panelInfo.add(lblPosicion);
        panelInfo.add(Box.createRigidArea(new Dimension(0, 3)));
        panelInfo.add(lblPropiedades);
        
        // Si tiene propiedades, mostrar lista
        if (!jugador.getPropiedades().isEmpty()) {
            panelInfo.add(Box.createRigidArea(new Dimension(0, 5)));
            JPanel panelPropsList = crearListaPropiedades(jugador.getPropiedades());
            panelInfo.add(panelPropsList);
        }
        
        panel.add(panelIzquierdo, BorderLayout.NORTH);
        panel.add(panelInfo, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearListaPropiedades(List<Propiedad> propiedades) {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 2));
        panel.setBackground(Color.WHITE);
        
        for (Propiedad prop : propiedades) {
            JPanel cuadrito = new JPanel();
            cuadrito.setPreferredSize(new Dimension(20, 20));
            cuadrito.setBackground(prop.getGrupoColor());
            cuadrito.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            cuadrito.setToolTipText(prop.getNombre());
            panel.add(cuadrito);
        }
        
        return panel;
    }
}
