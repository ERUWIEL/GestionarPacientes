package com.mycompany.gestionarpacientes.view.panels;

import com.mycompany.gestionarpacientes.dto.DoctorDTO;
import com.mycompany.gestionarpacientes.exceptions.ServiceException;
import com.mycompany.gestionarpacientes.repository.impl.DoctorRepository;
import com.mycompany.gestionarpacientes.service.IDoctorService;
import com.mycompany.gestionarpacientes.service.impl.DoctorService;
import com.mycompany.gestionarpacientes.util.JpaUtil;
import com.mycompany.gestionarpacientes.view.components.PanelRound;
import com.mycompany.gestionarpacientes.view.components.TextFieldPanel;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * Panel para la gestión de doctores con CRUD completo
 *
 * @author gatog
 */
public class PanelGestionDoctores extends JPanel {

    private final IDoctorService doctorService;

    // Componentes de formulario
    private TextFieldPanel txtNombre;
    private TextFieldPanel txtApellido;
    private TextFieldPanel txtDni;
    private TextFieldPanel txtEmail;
    private TextFieldPanel txtEspecialidad;
    private TextFieldPanel txtCedulaProfesional;

    // Tabla y modelo
    private JTable tablaDoctores;
    private DefaultTableModel modeloTabla;

    // Botones
    private JButton btnAgregar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnLimpiar;
    private JButton btnBuscar;
    private JButton btnBuscarEspecialidad;

    // Campos de búsqueda
    private TextFieldPanel txtBuscar;
    private TextFieldPanel txtBuscarEspecialidad;

    // ID del doctor seleccionado
    private Long doctorSeleccionadoId = null;

    // Paginación
    private int paginaActual = 0;
    private final int registrosPorPagina = 20;

    public PanelGestionDoctores() {
        this.doctorService = new DoctorService();

        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(30, 30, 48));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        initComponents();
        cargarDoctores();
    }

    private void initComponents() {
        // Panel superior con título y búsqueda
        JPanel panelTitulo = crearPanelTitulo();
        add(panelTitulo, BorderLayout.NORTH);

        // Panel central con tabla
        JPanel panelTabla = crearPanelTabla();
        add(panelTabla, BorderLayout.CENTER);

        // Panel derecho con formulario
        JPanel panelFormulario = crearPanelFormulario();
        add(panelFormulario, BorderLayout.EAST);
    }

    private JPanel crearPanelTitulo() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JLabel lblTitulo = new JLabel("Gestión de Doctores");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setForeground(Color.WHITE);
        panel.add(lblTitulo, BorderLayout.WEST);

        // Panel de búsqueda
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBusqueda.setOpaque(false);

        txtBuscar = new TextFieldPanel(TextFieldPanel.NAME_REGEX, "Nombre inválido");
        txtBuscar.setMessage("Buscar por nombre...");
        txtBuscar.setPreferredSize(new Dimension(250, 60));

        btnBuscar = crearBoton("Buscar", new Color(59, 130, 246));
        btnBuscar.addActionListener(e -> buscarDoctoresPorNombre());

        txtBuscarEspecialidad = new TextFieldPanel(TextFieldPanel.NAME_REGEX, "Especialidad inválida");
        txtBuscarEspecialidad.setMessage("Buscar por especialidad...");
        txtBuscarEspecialidad.setPreferredSize(new Dimension(250, 60));

        btnBuscarEspecialidad = crearBoton("Buscar Esp.", new Color(147, 51, 234));
        btnBuscarEspecialidad.addActionListener(e -> buscarDoctoresPorEspecialidad());

        panelBusqueda.add(txtBuscar);
        panelBusqueda.add(btnBuscar);
        panelBusqueda.add(txtBuscarEspecialidad);
        panelBusqueda.add(btnBuscarEspecialidad);

        panel.add(panelBusqueda, BorderLayout.EAST);

        return panel;
    }

    private JPanel crearPanelTabla() {
        PanelRound panel = new PanelRound();
        panel.setBackground(new Color(47, 35, 72));
        panel.setRoundTopLeft(20);
        panel.setRoundTopRight(20);
        panel.setRoundBottomLeft(20);
        panel.setRoundBottomRight(20);
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Modelo de tabla
        String[] columnas = {"ID", "Nombre", "Apellido", "DNI", "Email", "Especialidad", "Cédula Profesional"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaDoctores = new JTable(modeloTabla);
        tablaDoctores.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaDoctores.setRowHeight(30);
        tablaDoctores.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tablaDoctores.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tablaDoctores.setBackground(new Color(47, 35, 72));
        tablaDoctores.setForeground(Color.WHITE);
        tablaDoctores.setGridColor(new Color(158, 140, 185));
        tablaDoctores.getTableHeader().setBackground(new Color(30, 30, 48));
        tablaDoctores.getTableHeader().setForeground(Color.WHITE);

        // Listener para selección
        tablaDoctores.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarDoctorSeleccionado();
            }
        });

        JScrollPane scrollPane = new JScrollPane(tablaDoctores);
        scrollPane.setBackground(new Color(47, 35, 72));
        scrollPane.getViewport().setBackground(new Color(47, 35, 72));
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(158, 140, 185), 1));

        panel.add(scrollPane, BorderLayout.CENTER);

        // Panel de paginación
        JPanel panelPaginacion = crearPanelPaginacion();
        panel.add(panelPaginacion, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearPanelPaginacion() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panel.setOpaque(false);

        JButton btnAnterior = crearBoton("← Anterior", new Color(107, 114, 128));
        btnAnterior.addActionListener(e -> {
            if (paginaActual > 0) {
                paginaActual--;
                cargarDoctores();
            }
        });

        JButton btnSiguiente = crearBoton("Siguiente →", new Color(107, 114, 128));
        btnSiguiente.addActionListener(e -> {
            paginaActual++;
            cargarDoctores();
        });

        JButton btnMostrarTodos = crearBoton("Mostrar Todos", new Color(59, 130, 246));
        btnMostrarTodos.addActionListener(e -> {
            paginaActual = 0;
            cargarDoctores();
        });

        panel.add(btnAnterior);
        panel.add(btnMostrarTodos);
        panel.add(btnSiguiente);

        return panel;
    }

    private JPanel crearPanelFormulario() {
        PanelRound panel = new PanelRound();
        panel.setBackground(new Color(47, 35, 72));
        panel.setRoundTopLeft(20);
        panel.setRoundTopRight(20);
        panel.setRoundBottomLeft(20);
        panel.setRoundBottomRight(20);
        panel.setPreferredSize(new Dimension(380, 0));
        panel.setLayout(new BorderLayout());

        JPanel formulario = new JPanel();
        formulario.setLayout(new BoxLayout(formulario, BoxLayout.Y_AXIS));
        formulario.setOpaque(false);
        formulario.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Título
        JLabel lblTituloForm = new JLabel("Formulario de Doctor");
        lblTituloForm.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTituloForm.setForeground(Color.WHITE);
        lblTituloForm.setAlignmentX(Component.LEFT_ALIGNMENT);
        formulario.add(lblTituloForm);
        formulario.add(Box.createRigidArea(new Dimension(0, 20)));

        // Campos
        txtNombre = new TextFieldPanel(TextFieldPanel.NAME_REGEX, "Nombre inválido");
        txtNombre.setMessage("Nombre");
        txtNombre.setAlignmentX(Component.LEFT_ALIGNMENT);
        formulario.add(txtNombre);
        formulario.add(Box.createRigidArea(new Dimension(0, 15)));

        txtApellido = new TextFieldPanel(TextFieldPanel.NAME_REGEX, "Apellido inválido");
        txtApellido.setMessage("Apellido");
        txtApellido.setAlignmentX(Component.LEFT_ALIGNMENT);
        formulario.add(txtApellido);
        formulario.add(Box.createRigidArea(new Dimension(0, 15)));

        txtDni = new TextFieldPanel("^[A-Z0-9]{5,20}$", "DNI inválido (5-20 caracteres)");
        txtDni.setMessage("DNI");
        txtDni.setAlignmentX(Component.LEFT_ALIGNMENT);
        formulario.add(txtDni);
        formulario.add(Box.createRigidArea(new Dimension(0, 15)));

        txtEmail = new TextFieldPanel(TextFieldPanel.EMAIL_REGEX, "Email inválido");
        txtEmail.setMessage("Email");
        txtEmail.setAlignmentX(Component.LEFT_ALIGNMENT);
        formulario.add(txtEmail);
        formulario.add(Box.createRigidArea(new Dimension(0, 15)));

        txtEspecialidad = new TextFieldPanel(TextFieldPanel.NAME_REGEX, "Especialidad inválida");
        txtEspecialidad.setMessage("Especialidad");
        txtEspecialidad.setAlignmentX(Component.LEFT_ALIGNMENT);
        formulario.add(txtEspecialidad);
        formulario.add(Box.createRigidArea(new Dimension(0, 15)));

        txtCedulaProfesional = new TextFieldPanel("^CED-[0-9]{8}$", "Formato: CED-12345678");
        txtCedulaProfesional.setMessage("Cédula Profesional");
        txtCedulaProfesional.setAlignmentX(Component.LEFT_ALIGNMENT);
        formulario.add(txtCedulaProfesional);
        formulario.add(Box.createRigidArea(new Dimension(0, 20)));

        // Panel de botones
        JPanel panelBotones = crearPanelBotones();
        panelBotones.setAlignmentX(Component.LEFT_ALIGNMENT);
        formulario.add(panelBotones);

        JScrollPane scrollFormulario = new JScrollPane(formulario);
        scrollFormulario.setOpaque(false);
        scrollFormulario.getViewport().setOpaque(false);
        scrollFormulario.setBorder(null);

        panel.add(scrollFormulario, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        btnAgregar = crearBoton("Agregar", new Color(34, 197, 94));
        btnAgregar.addActionListener(e -> agregarDoctor());

        btnActualizar = crearBoton("Actualizar", new Color(59, 130, 246));
        btnActualizar.addActionListener(e -> actualizarDoctor());
        btnActualizar.setEnabled(false);

        btnEliminar = crearBoton("Eliminar", new Color(239, 68, 68));
        btnEliminar.addActionListener(e -> eliminarDoctor());
        btnEliminar.setEnabled(false);

        btnLimpiar = crearBoton("Limpiar", new Color(107, 114, 128));
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        panel.add(btnAgregar);
        panel.add(btnActualizar);
        panel.add(btnEliminar);
        panel.add(btnLimpiar);

        return panel;
    }

    private JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        boton.setForeground(Color.WHITE);
        boton.setBackground(color);
        boton.setBorderPainted(false);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setPreferredSize(new Dimension(120, 40));

        // Efecto hover
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(color.brighter());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(color);
            }
        });

        return boton;
    }

    private void cargarDoctores() {
        try {
            int offset = paginaActual * registrosPorPagina;
            List<DoctorDTO> doctores = doctorService.listarTodosDoctores(registrosPorPagina, offset);

            actualizarTabla(doctores);
        } catch (ServiceException e) {
            mostrarError("Error al cargar doctores: " + e.getMessage());
        }
    }

    private void buscarDoctoresPorNombre() {
        String busqueda = txtBuscar.getText().trim();
        if (busqueda.isEmpty() || busqueda.equals("Buscar por nombre...")) {
            cargarDoctores();
            return;
        }

        try {
            List<DoctorDTO> doctores = doctorService.buscarDoctoresPorNombre(busqueda, 50, 0);
            actualizarTabla(doctores);
        } catch (ServiceException e) {
            mostrarError("Error en la búsqueda: " + e.getMessage());
        }
    }

    private void buscarDoctoresPorEspecialidad() {
        String busqueda = txtBuscarEspecialidad.getText().trim();
        if (busqueda.isEmpty() || busqueda.equals("Buscar por especialidad...")) {
            cargarDoctores();
            return;
        }

        try {
            List<DoctorDTO> doctores = doctorService.buscarDoctoresPorEspecialidad(busqueda, 50, 0);
            actualizarTabla(doctores);
        } catch (ServiceException e) {
            mostrarError("Error en la búsqueda: " + e.getMessage());
        }
    }

    private void actualizarTabla(List<DoctorDTO> doctores) {
        modeloTabla.setRowCount(0);
        for (DoctorDTO d : doctores) {
            Object[] fila = {
                d.getId(),
                d.getNombre(),
                d.getApellido(),
                d.getDni(),
                d.getEmail(),
                d.getEspecialidad(),
                d.getCedulaProfesional()
            };
            modeloTabla.addRow(fila);
        }
    }

    private void agregarDoctor() {
        try {
            DoctorDTO doctor = obtenerDatosFormulario();
            DoctorDTO guardado = doctorService.registrarDoctor(doctor);

            mostrarExito("Doctor registrado exitosamente");
            limpiarFormulario();
            cargarDoctores();
        } catch (ServiceException e) {
            mostrarError("Error al registrar: " + e.getMessage());
        }
    }

    private void actualizarDoctor() {
        if (doctorSeleccionadoId == null) {
            mostrarError("Seleccione un doctor de la tabla");
            return;
        }

        try {
            DoctorDTO doctor = obtenerDatosFormulario();
            doctor.setId(doctorSeleccionadoId);

            doctorService.actualizarDoctor(doctor);
            mostrarExito("Doctor actualizado exitosamente");
            limpiarFormulario();
            cargarDoctores();
        } catch (ServiceException e) {
            mostrarError("Error al actualizar: " + e.getMessage());
        }
    }

    private void eliminarDoctor() {
        if (doctorSeleccionadoId == null) {
            mostrarError("Seleccione un doctor de la tabla");
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro de eliminar este doctor?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                doctorService.eliminarDoctor(doctorSeleccionadoId);
                mostrarExito("Doctor eliminado exitosamente");
                limpiarFormulario();
                cargarDoctores();
            } catch (ServiceException e) {
                mostrarError("Error al eliminar: " + e.getMessage());
            }
        }
    }

    private void cargarDoctorSeleccionado() {
        int filaSeleccionada = tablaDoctores.getSelectedRow();
        if (filaSeleccionada >= 0) {
            doctorSeleccionadoId = (Long) modeloTabla.getValueAt(filaSeleccionada, 0);

            txtNombre.setMessage((String) modeloTabla.getValueAt(filaSeleccionada, 1));
            txtApellido.setMessage((String) modeloTabla.getValueAt(filaSeleccionada, 2));
            txtDni.setMessage((String) modeloTabla.getValueAt(filaSeleccionada, 3));
            txtEmail.setMessage((String) modeloTabla.getValueAt(filaSeleccionada, 4));
            txtEspecialidad.setMessage((String) modeloTabla.getValueAt(filaSeleccionada, 5));
            txtCedulaProfesional.setMessage((String) modeloTabla.getValueAt(filaSeleccionada, 6));

            btnActualizar.setEnabled(true);
            btnEliminar.setEnabled(true);
            btnAgregar.setEnabled(false);
        }
    }

    private void limpiarFormulario() {
        txtNombre.setMessage("Nombre");
        txtApellido.setMessage("Apellido");
        txtDni.setMessage("DNI");
        txtEmail.setMessage("Email");
        txtEspecialidad.setMessage("Especialidad");
        txtCedulaProfesional.setMessage("Cédula Profesional");

        doctorSeleccionadoId = null;
        btnActualizar.setEnabled(false);
        btnEliminar.setEnabled(false);
        btnAgregar.setEnabled(true);

        tablaDoctores.clearSelection();
    }

    private DoctorDTO obtenerDatosFormulario() {
        String nombre = txtNombre.getText();
        String apellido = txtApellido.getText();
        String dni = txtDni.getText();
        String email = txtEmail.getText();
        String especialidad = txtEspecialidad.getText();
        String cedula = txtCedulaProfesional.getText();

        return new DoctorDTO(nombre, apellido, dni, email, especialidad, cedula);
    }

    private void mostrarError(String mensaje) {
        System.err.println("ERROR: " + mensaje);
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
        });
    }

    private void mostrarExito(String mensaje) {
        System.out.println("ÉXITO: " + mensaje);
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(this, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
        });
    }
}
