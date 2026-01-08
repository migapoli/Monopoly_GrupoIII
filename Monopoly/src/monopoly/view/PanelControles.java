package monopoly.view;

import monopoly.controller.ControladorJuego;

import javax.swing.*;
import java.awt.*;

public class PanelControles extends JPanel {
    private ControladorJuego controlador;
    private JButton btnTirarDados;
    private JButton btnComprar;
    private JButton btnFinalizarTurno;
    private JLabel lblDado1;
    private JLabel lblDado2;
    private JPanel panelDados;
    
    public PanelControles(ControladorJuego controlador) {
        this.controlador = controlador;
        inicializarComponentes();
    }
    
    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)));
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(0, 200));
        
        // Título del panel
        JLabel lblTitulo = new JLabel("PANEL DE JUEGO");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Panel de botones
        JPanel panelBotones = crearPanelBotones();
        
        add(lblTitulo, BorderLayout.NORTH);
        add(panelBotones, BorderLayout.CENTER);
    }
    
    private JPanel crearPanelDados() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBackground(new Color(240, 240, 255));
        
        // Dado 1
        lblDado1 = new JLabel();
        lblDado1.setPreferredSize(new Dimension(60, 60));
        lblDado1.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        lblDado1.setOpaque(true);
        lblDado1.setBackground(Color.WHITE);
        lblDado1.setHorizontalAlignment(SwingConstants.CENTER);
        lblDado1.setFont(new Font("Arial", Font.BOLD, 32));
        actualizarDado(lblDado1, 1);
        
        // Dado 2
        lblDado2 = new JLabel();
        lblDado2.setPreferredSize(new Dimension(60, 60));
        lblDado2.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        lblDado2.setOpaque(true);
        lblDado2.setBackground(Color.WHITE);
        lblDado2.setHorizontalAlignment(SwingConstants.CENTER);
        lblDado2.setFont(new Font("Arial", Font.BOLD, 32));
        actualizarDado(lblDado2, 1);
        
        panel.add(lblDado1);
        panel.add(new JLabel("  +  "));
        panel.add(lblDado2);
        
        return panel;
    }
    
    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 20));
        panel.setBackground(Color.WHITE);
        
        // Botón Lanzar Dado
        btnTirarDados = crearBoton("LANZAR DADO", new Color(45, 50, 70));
        btnTirarDados.addActionListener(e -> {
            controlador.tirarDados();
        });
        
        // Botón Pasar Turno
        btnFinalizarTurno = crearBoton("PASAR TURNO", new Color(45, 50, 70));
        btnFinalizarTurno.addActionListener(e -> {
            controlador.finalizarTurno();
        });
        
        panel.add(btnTirarDados);
        panel.add(btnFinalizarTurno);
        
        return panel;
    }
    
    private JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.BOLD, 12));
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setOpaque(true);
        boton.setBorderPainted(false);
        boton.setPreferredSize(new Dimension(140, 45));
        
        return boton;
    }
    
    private void actualizarDado(JLabel lblDado, int valor) {
        lblDado.setText(String.valueOf(valor));
        
        // Cambiar color según el valor
        switch (valor) {
            case 1:
            case 6:
                lblDado.setForeground(new Color(200, 0, 0));
                break;
            case 2:
            case 5:
                lblDado.setForeground(new Color(0, 0, 200));
                break;
            default:
                lblDado.setForeground(Color.BLACK);
        }
    }
    
    public void mostrarDados(int dado1, int dado2) {
        // Si los labels no están inicializados, no hacer nada
        if (lblDado1 == null || lblDado2 == null) {
            return;
        }
        
        actualizarDado(lblDado1, dado1);
        actualizarDado(lblDado2, dado2);
        
        // Animación simple
        Timer timer = new Timer(100, null);
        final int[] contador = {0};
        timer.addActionListener(e -> {
            if (contador[0] < 3) {
                lblDado1.setBackground(lblDado1.getBackground() == Color.WHITE ? 
                    new Color(255, 255, 200) : Color.WHITE);
                lblDado2.setBackground(lblDado2.getBackground() == Color.WHITE ? 
                    new Color(255, 255, 200) : Color.WHITE);
                contador[0]++;
            } else {
                lblDado1.setBackground(Color.WHITE);
                lblDado2.setBackground(Color.WHITE);
                ((Timer)e.getSource()).stop();
            }
        });
        timer.start();
    }
    
    public void actualizar() {
        // Actualizar estado de botones si es necesario
        repaint();
    }
}
