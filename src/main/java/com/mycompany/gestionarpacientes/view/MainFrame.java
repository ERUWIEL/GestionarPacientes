package com.mycompany.gestionarpacientes.view;

import com.mycompany.gestionarpacientes.view.components.TextFieldPanel;
import java.awt.BorderLayout;
import javax.swing.JFrame;

/**
 * clase jframe para modelar los componentes
 *
 * @author gatog
 */
public class MainFrame extends JFrame {

    public MainFrame() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(900, 600);
        setLayout(new BorderLayout());
        
        initComponents();
    }

    private void initComponents() {
        TextFieldPanel correo = new TextFieldPanel(TextFieldPanel.EMAIL_REGEX, "campo necesario");
        correo.setMessage("ingrese su correo electronico");
        add(correo, BorderLayout.NORTH);
    }

}
