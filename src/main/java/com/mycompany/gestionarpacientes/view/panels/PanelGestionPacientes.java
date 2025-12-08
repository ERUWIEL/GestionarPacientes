package com.mycompany.gestionarpacientes.view.panels;

import com.mycompany.gestionarpacientes.dto.PacienteDTO;
import com.mycompany.gestionarpacientes.exceptions.ServiceException;
import com.mycompany.gestionarpacientes.repository.impl.PacienteRepository;
import com.mycompany.gestionarpacientes.service.IPacienteService;
import com.mycompany.gestionarpacientes.service.impl.PacienteService;
import com.mycompany.gestionarpacientes.util.JpaUtil;
import com.mycompany.gestionarpacientes.view.components.PanelRound;
import com.mycompany.gestionarpacientes.view.components.TextFieldPanel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * Panel para la gestión de pacientes con CRUD completo
 *
 * @author gatog
 */
public class PanelGestionPacientes extends JPanel {

    private final IPacienteService pacienteService;

    // Componentes de formulario
    private TextFieldPanel txtNombre;
    private TextFieldPanel txtApellido;
    private TextFieldPanel txtDni;
    private TextFieldPanel txtEmail;
    private TextFieldPanel txtFechaNacimiento;
    private TextFieldPanel txtTipoSangre;
    private TextFieldPanel txtSeguroMedico;

    // Tabla y modelo
    private JTable tablaPacientes;
    private DefaultTableModel modeloTabla;

    // Botones
    private JButton btnAgregar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnLimpiar;
    private JButton btnBuscar;

    // Campo de búsqueda
    private TextFieldPanel txtBuscar;

    // ID del paciente seleccionado
    private Long pacienteSeleccionadoId = null;

    // Paginación
    private int paginaActual = 0;
    private final int registrosPorPagina = 20;

    public PanelGestionPacientes() {
        this.pacienteService = new PacienteService();

        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(30, 30, 48));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        initComponents();
        cargarPacientes();
    }

    private void initComponents() {
        // Panel superior con título
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

        JLabel lblTitulo = new JLabel("Gestión de Pacientes");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setForeground(Color.WHITE);
        panel.add(lblTitulo, BorderLayout.WEST);

        // Panel de búsqueda
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBusqueda.setOpaque(false);

        txtBuscar = new TextFieldPanel(TextFieldPanel.NAME_REGEX, "Nombre inválido");
        txtBuscar.setMessage("Buscar paciente...");
        txtBuscar.setPreferredSize(new Dimension(300, 60));

        btnBuscar = crearBoton("Buscar", new Color(59, 130, 246));
        btnBuscar.addActionListener(e -> buscarPacientes());

        panelBusqueda.add(txtBuscar);
        panelBusqueda.add(btnBuscar);

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
        String[] columnas = {"ID", "Nombre", "Apellido", "DNI", "Email", "Fecha Nac.", "Tipo Sangre", "Seguro"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaPacientes = new JTable(modeloTabla);
        tablaPacientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaPacientes.setRowHeight(30);
        tablaPacientes.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tablaPacientes.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tablaPacientes.setBackground(new Color(47, 35, 72));
        tablaPacientes.setForeground(Color.WHITE);
        tablaPacientes.setGridColor(new Color(158, 140, 185));
        tablaPacientes.getTableHeader().setBackground(new Color(30, 30, 48));
        tablaPacientes.getTableHeader().setForeground(Color.WHITE);

        // Listener para selección
        tablaPacientes.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarPacienteSeleccionado();
            }
        });

        JScrollPane scrollPane = new JScrollPane(tablaPacientes);
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
                cargarPacientes();
            }
        });

        JButton btnSiguiente = crearBoton("Siguiente →", new Color(107, 114, 128));
        btnSiguiente.addActionListener(e -> {
            paginaActual++;
            cargarPacientes();
        });

        panel.add(btnAnterior);
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
        JLabel lblTituloForm = new JLabel("Formulario de Paciente");
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

        txtFechaNacimiento = new TextFieldPanel(TextFieldPanel.DATE_REGEX, "Formato: YYYY-MM-DD");
        txtFechaNacimiento.setMessage("Fecha Nacimiento (YYYY-MM-DD)");
        txtFechaNacimiento.setAlignmentX(Component.LEFT_ALIGNMENT);
        formulario.add(txtFechaNacimiento);
        formulario.add(Box.createRigidArea(new Dimension(0, 15)));

        txtTipoSangre = new TextFieldPanel("^(A|B|AB|O)[+-]$", "Ej: A+, O-, AB+");
        txtTipoSangre.setMessage("Tipo de Sangre");
        txtTipoSangre.setAlignmentX(Component.LEFT_ALIGNMENT);
        formulario.add(txtTipoSangre);
        formulario.add(Box.createRigidArea(new Dimension(0, 15)));

        txtSeguroMedico = new TextFieldPanel("^.{1,100}$", "Máximo 100 caracteres");
        txtSeguroMedico.setMessage("Seguro Médico");
        txtSeguroMedico.setAlignmentX(Component.LEFT_ALIGNMENT);
        formulario.add(txtSeguroMedico);
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
        btnAgregar.addActionListener(e -> agregarPaciente());

        btnActualizar = crearBoton("Actualizar", new Color(59, 130, 246));
        btnActualizar.addActionListener(e -> actualizarPaciente());
        btnActualizar.setEnabled(false);

        btnEliminar = crearBoton("Eliminar", new Color(239, 68, 68));
        btnEliminar.addActionListener(e -> eliminarPaciente());
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

    private void cargarPacientes() {
        try {
            int offset = paginaActual * registrosPorPagina;
            List<PacienteDTO> pacientes = pacienteService.listarTodosPacientes(registrosPorPagina, offset);

            modeloTabla.setRowCount(0);
            for (PacienteDTO p : pacientes) {
                Object[] fila = {
                    p.getId(),
                    p.getNombre(),
                    p.getApellido(),
                    p.getDni(),
                    p.getEmail(),
                    p.getFechaNacimiento(),
                    p.getTipoSangre(),
                    p.getSeguroMedico()
                };
                modeloTabla.addRow(fila);
            }
        } catch (ServiceException e) {
            mostrarError("Error al cargar pacientes: " + e.getMessage());
        }
    }

    private void buscarPacientes() {
        String busqueda = txtBuscar.getText().trim();
        if (busqueda.isEmpty() || busqueda.equals("Buscar paciente...")) {
            cargarPacientes();
            return;
        }

        try {
            List<PacienteDTO> pacientes = pacienteService.buscarPacientesPorNombre(busqueda, 50, 0);

            modeloTabla.setRowCount(0);
            for (PacienteDTO p : pacientes) {
                Object[] fila = {
                    p.getId(),
                    p.getNombre(),
                    p.getApellido(),
                    p.getDni(),
                    p.getEmail(),
                    p.getFechaNacimiento(),
                    p.getTipoSangre(),
                    p.getSeguroMedico()
                };
                modeloTabla.addRow(fila);
            }
        } catch (ServiceException e) {
            mostrarError("Error en la búsqueda: " + e.getMessage());
        }
    }

    private void agregarPaciente() {
        try {
            PacienteDTO paciente = obtenerDatosFormulario();
            PacienteDTO guardado = pacienteService.registrarPaciente(paciente);

            mostrarExito("Paciente registrado exitosamente");
            limpiarFormulario();
            cargarPacientes();
        } catch (ServiceException e) {
            mostrarError("Error al registrar: " + e.getMessage());
        }
    }

    private void actualizarPaciente() {
        if (pacienteSeleccionadoId == null) {
            mostrarError("Seleccione un paciente de la tabla");
            return;
        }

        try {
            PacienteDTO paciente = obtenerDatosFormulario();
            paciente.setId(pacienteSeleccionadoId);

            pacienteService.actualizarPaciente(paciente);
            mostrarExito("Paciente actualizado exitosamente");
            limpiarFormulario();
            cargarPacientes();
        } catch (ServiceException e) {
            mostrarError("Error al actualizar: " + e.getMessage());
        }
    }

    private void eliminarPaciente() {
        if (pacienteSeleccionadoId == null) {
            mostrarError("Seleccione un paciente de la tabla");
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro de eliminar este paciente?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                pacienteService.eliminarPaciente(pacienteSeleccionadoId);
                mostrarExito("Paciente eliminado exitosamente");
                limpiarFormulario();
                cargarPacientes();
            } catch (ServiceException e) {
                mostrarError("Error al eliminar: " + e.getMessage());
            }
        }
    }

    private void cargarPacienteSeleccionado() {
        int filaSeleccionada = tablaPacientes.getSelectedRow();
        if (filaSeleccionada >= 0) {
            pacienteSeleccionadoId = (Long) modeloTabla.getValueAt(filaSeleccionada, 0);

            txtNombre.setMessage((String) modeloTabla.getValueAt(filaSeleccionada, 1));
            txtApellido.setMessage((String) modeloTabla.getValueAt(filaSeleccionada, 2));
            txtDni.setMessage((String) modeloTabla.getValueAt(filaSeleccionada, 3));
            txtEmail.setMessage((String) modeloTabla.getValueAt(filaSeleccionada, 4));
            txtFechaNacimiento.setMessage(modeloTabla.getValueAt(filaSeleccionada, 5).toString());
            txtTipoSangre.setMessage((String) modeloTabla.getValueAt(filaSeleccionada, 6));
            txtSeguroMedico.setMessage((String) modeloTabla.getValueAt(filaSeleccionada, 7));

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
        txtFechaNacimiento.setMessage("Fecha Nacimiento (YYYY-MM-DD)");
        txtTipoSangre.setMessage("Tipo de Sangre");
        txtSeguroMedico.setMessage("Seguro Médico");

        pacienteSeleccionadoId = null;
        btnActualizar.setEnabled(false);
        btnEliminar.setEnabled(false);
        btnAgregar.setEnabled(true);

        tablaPacientes.clearSelection();
    }

    private PacienteDTO obtenerDatosFormulario() throws ServiceException {
        String nombre = txtNombre.getText();
        String apellido = txtApellido.getText();
        String dni = txtDni.getText();
        String email = txtEmail.getText();
        String fechaStr = txtFechaNacimiento.getText();
        String tipoSangre = txtTipoSangre.getText();
        String seguro = txtSeguroMedico.getText();

        try {
            LocalDate fecha = LocalDate.parse(fechaStr, DateTimeFormatter.ISO_LOCAL_DATE);
            return new PacienteDTO(nombre, apellido, dni, email, fecha, tipoSangre, seguro);
        } catch (Exception e) {
            throw new ServiceException("Formato de fecha inválido. Use YYYY-MM-DD");
        }
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void mostrarExito(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }
}
