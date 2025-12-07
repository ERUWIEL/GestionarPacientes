package com.mycompany.gestionarpacientes;

import com.mycompany.gestionarpacientes.view.MainFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author gatog
 */
public class App {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}
