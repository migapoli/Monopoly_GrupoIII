package monopoly.main;

import monopoly.view.PantallaConfiguracion;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Configurar el Look and Feel del sistema
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("No se pudo establecer el Look and Feel: " + e.getMessage());
        }

        // Iniciar la aplicación en el Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            PantallaConfiguracion pantallaConfig = new PantallaConfiguracion();
            pantallaConfig.setVisible(true);
        });
    }
}
