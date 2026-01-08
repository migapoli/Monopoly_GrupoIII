package monopoly.view;

import monopoly.controller.ControladorJuego;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PanelHistorial extends JPanel {
    private ControladorJuego controlador;
    private JTextArea areaHistorial;
    private SimpleDateFormat formatoHora;
    
    public PanelHistorial(ControladorJuego controlador) {
        this.controlador = controlador;
        this.formatoHora = new SimpleDateFormat("HH:mm:ss");
        inicializarComponentes();
    }
    
    private void inicializarComponentes() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 0, 0, Color.BLACK),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(0, 250));
        
        // Título del panel
        JLabel lblTitulo = new JLabel("PANEL DE HISTORIAL");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        // Área de texto para el historial
        areaHistorial = new JTextArea();
        areaHistorial.setEditable(false);
        areaHistorial.setFont(new Font("Arial", Font.PLAIN, 11));
        areaHistorial.setLineWrap(true);
        areaHistorial.setWrapStyleWord(true);
        areaHistorial.setBackground(Color.WHITE);
        areaHistorial.setMargin(new Insets(5, 5, 5, 5));
        
        // ScrollPane para el área de texto
        JScrollPane scrollPane = new JScrollPane(areaHistorial);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        
        add(lblTitulo, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        
        // Mensaje inicial
        agregarMensaje("Sistema iniciado");
    }
    
    public void agregarMensaje(String mensaje) {
        String linea = "> " + mensaje + "\n";
        areaHistorial.append(linea);
        
        // Auto-scroll al final
        areaHistorial.setCaretPosition(areaHistorial.getDocument().getLength());
    }
    
    public void actualizar() {
        // Cargar todo el historial de la partida
        areaHistorial.setText("");
        for (String evento : controlador.getPartida().getHistorial()) {
            areaHistorial.append("> " + evento + "\n");
        }
        
        // Auto-scroll al final
        SwingUtilities.invokeLater(() -> {
            areaHistorial.setCaretPosition(areaHistorial.getDocument().getLength());
        });
    }
    
    private void limpiarHistorial() {
        int respuesta = JOptionPane.showConfirmDialog(
            this,
            "¿Estás seguro de que deseas limpiar el historial?",
            "Confirmar limpieza",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (respuesta == JOptionPane.YES_OPTION) {
            areaHistorial.setText("");
            agregarMensaje("Historial limpiado");
        }
    }
}
