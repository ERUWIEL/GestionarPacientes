package com.mycompany.gestionarpacientes.entitys;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author angel
 */
@Entity
@Table(name = "doctores")
public class Doctor extends Persona {

    @Column(nullable = false, length = 50)
    private String especialidad;

    @Column(name = "cedula_profesional", unique = true, nullable = false)
    private String cedulaProfesional;

    // Relación uno a muchos: Un doctor -> Muchas citas
    @OneToMany(mappedBy = "doctor", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Cita> historialCitas = new ArrayList<>();

    // Constructores, Getters y Setters
    public Doctor() {
    }

    public Doctor(String especialidad, String cedulaProfesional) {
        this.especialidad = especialidad;
        this.cedulaProfesional = cedulaProfesional;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getCedulaProfesional() {
        return cedulaProfesional;
    }

    public void setCedulaProfesional(String cedulaProfesional) {
        this.cedulaProfesional = cedulaProfesional;
    }

    public List<Cita> getHistorialCitas() {
        return historialCitas;
    }

    public void setHistorialCitas(List<Cita> historialCitas) {
        this.historialCitas = historialCitas;
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
