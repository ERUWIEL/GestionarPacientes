package com.mycompany.gestionarpacientes.view;

import com.mycompany.gestionarpacientes.view.panels.PanelGestionDoctores;
import com.mycompany.gestionarpacientes.view.panels.PanelGestionPacientes;
import java.awt.*;
import javax.swing.*;

/**
 * Ventana principal de la aplicación con sistema de pestañas
 *
 * @author gatog
 */
public class MainFrame extends JFrame {

    private JTabbedPane tabbedPane;
    private PanelGestionPacientes panelPacientes;
    private PanelGestionDoctores panelDoctores;

    public MainFrame() {
        setTitle("Sistema de Gestión de Citas Médicas");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(1400, 800);
        setMinimumSize(new Dimension(1200, 700));

        // Configurar look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        initComponents();
    }

    private void initComponents() {
        // Panel principal con fondo oscuro
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(new Color(30, 30, 48));

        // Crear panel de encabezado
        JPanel panelEncabezado = crearPanelEncabezado();
        panelPrincipal.add(panelEncabezado, BorderLayout.NORTH);

        // Crear pestañas
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabbedPane.setBackground(new Color(30, 30, 48));
        tabbedPane.setForeground(Color.BLACK);

        // Personalizar el aspecto de las pestañas
        UIManager.put("TabbedPane.selected", new Color(59, 130, 246));
        UIManager.put("TabbedPane.background", new Color(47, 35, 72));
        UIManager.put("TabbedPane.foreground", Color.WHITE);
        UIManager.put("TabbedPane.contentAreaColor", new Color(30, 30, 48));

        // Crear paneles
        panelPacientes = new PanelGestionPacientes();
        panelDoctores = new PanelGestionDoctores();

        // Agregar pestañas con iconos (opcional)
        tabbedPane.addTab("Pacientes", panelPacientes);
        tabbedPane.addTab("Doctores", panelDoctores);

        // Personalizar la altura de las pestañas
        tabbedPane.setPreferredSize(new Dimension(getWidth(), getHeight() - 100));

        panelPrincipal.add(tabbedPane, BorderLayout.CENTER);

        // Panel de pie de página
        JPanel panelPie = crearPanelPie();
        panelPrincipal.add(panelPie, BorderLayout.SOUTH);

        setContentPane(panelPrincipal);
    }

    private JPanel crearPanelEncabezado() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(47, 35, 72));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Título principal
        JLabel lblTitulo = new JLabel("Sistema de Gestión de Citas Médicas");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitulo.setForeground(Color.WHITE);

        // Subtítulo
        JLabel lblSubtitulo = new JLabel("Administración de Pacientes y Doctores");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblSubtitulo.setForeground(new Color(158, 140, 185));

        // Panel de títulos
        JPanel panelTitulos = new JPanel();
        panelTitulos.setLayout(new BoxLayout(panelTitulos, BoxLayout.Y_AXIS));
        panelTitulos.setOpaque(false);
        panelTitulos.add(lblTitulo);
        panelTitulos.add(Box.createRigidArea(new Dimension(0, 5)));
        panelTitulos.add(lblSubtitulo);

        panel.add(panelTitulos, BorderLayout.WEST);

        // Panel de información adicional (opcional)
        JPanel panelInfo = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        panelInfo.setOpaque(false);

        JLabel lblUsuario = new JLabel("Usuario: Admin");
        lblUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblUsuario.setForeground(Color.WHITE);

        JLabel lblFecha = new JLabel(java.time.LocalDate.now().toString());
        lblFecha.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblFecha.setForeground(Color.WHITE);

        panelInfo.add(lblUsuario);
        panelInfo.add(lblFecha);

        panel.add(panelInfo, BorderLayout.EAST);

        return panel;
    }

    private JPanel crearPanelPie() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(new Color(47, 35, 72));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JLabel lblCopyright = new JLabel("© 2024 Sistema de Gestión Médica - Versión 1.0");
        lblCopyright.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblCopyright.setForeground(new Color(158, 140, 185));

        panel.add(lblCopyright);

        return panel;
    }

    /**
     * Método para cambiar a la pestaña de pacientes
     */
    public void mostrarPestañaPacientes() {
        tabbedPane.setSelectedIndex(0);
    }

    /**
     * Método para cambiar a la pestaña de doctores
     */
    public void mostrarPestañaDoctores() {
        tabbedPane.setSelectedIndex(1);
    }
}
