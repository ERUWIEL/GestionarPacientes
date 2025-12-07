package com.mycompany.gestionarpacientes.dto;

import java.time.LocalDate;

/**
 * DTO para transferir informacion de Paciente entre capas
 *
 * @author gatog
 */
public class PacienteDTO {

    private Long id;
    private String nombre;
    private String apellido;
    private String dni;
    private String email;
    private LocalDate fechaNacimiento;
    private String tipoSangre;
    private String seguroMedico;

    public PacienteDTO() {
    }

    public PacienteDTO(Long id, String nombre, String apellido, String dni,
            String email, LocalDate fechaNacimiento, String tipoSangre, String seguroMedico) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.email = email;
        this.fechaNacimiento = fechaNacimiento;
        this.tipoSangre = tipoSangre;
        this.seguroMedico = seguroMedico;
    }

    public PacienteDTO(String nombre, String apellido, String dni, String email,
            LocalDate fechaNacimiento, String tipoSangre, String seguroMedico) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.email = email;
        this.fechaNacimiento = fechaNacimiento;
        this.tipoSangre = tipoSangre;
        this.seguroMedico = seguroMedico;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getTipoSangre() {
        return tipoSangre;
    }

    public void setTipoSangre(String tipoSangre) {
        this.tipoSangre = tipoSangre;
    }

    public String getSeguroMedico() {
        return seguroMedico;
    }

    public void setSeguroMedico(String seguroMedico) {
        this.seguroMedico = seguroMedico;
    }

    @Override
    public String toString() {
        return "PacienteDTO{"
                + "id=" + id
                + ", nombre='" + nombre + '\''
                + ", apellido='" + apellido + '\''
                + ", dni='" + dni + '\''
                + ", email='" + email + '\''
                + ", fechaNacimiento=" + fechaNacimiento
                + ", tipoSangre='" + tipoSangre + '\''
                + ", seguroMedico='" + seguroMedico + '\''
                + '}';
    }
}
