package com.mycompany.gestionarpacientes.entitys;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author angel
 */
@Entity
@Table(name = "pacientes")
public class Paciente extends Persona {

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Column(name = "tipo_sangre", length = 5)
    private String tipoSangre;

    @Column(name = "seguro_medico")
    private String seguroMedico;

    // Relación uno a muchos: Un paciente -> Muchas citas
    @OneToMany(mappedBy = "paciente", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Cita> historialMedico = new ArrayList<>();

    // Constructores, Getters y Setters
    public Paciente(LocalDate fechaNacimiento, String tipoSangre, String seguroMedico) {
        this.fechaNacimiento = fechaNacimiento;
        this.tipoSangre = tipoSangre;
        this.seguroMedico = seguroMedico;
    }

    public Paciente() {
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

    public List<Cita> getHistorialMedico() {
        return historialMedico;
    }

    public void setHistorialMedico(List<Cita> historialMedico) {
        this.historialMedico = historialMedico;
    }

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

}
