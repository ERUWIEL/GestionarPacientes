package com.mycompany.gestionarpacientes.dto;

/**
 * DTO para transferir informacion de Doctor entre capas
 *
 * @author gatog
 */
public class DoctorDTO {

    private Long id;
    private String nombre;
    private String apellido;
    private String dni;
    private String email;
    private String especialidad;
    private String cedulaProfesional;

    public DoctorDTO() {
    }

    public DoctorDTO(Long id, String nombre, String apellido, String dni,
            String email, String especialidad, String cedulaProfesional) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.email = email;
        this.especialidad = especialidad;
        this.cedulaProfesional = cedulaProfesional;
    }

    public DoctorDTO(String nombre, String apellido, String dni, String email,
            String especialidad, String cedulaProfesional) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.email = email;
        this.especialidad = especialidad;
        this.cedulaProfesional = cedulaProfesional;
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

    @Override
    public String toString() {
        return "DoctorDTO{"
                + "id=" + id
                + ", nombre='" + nombre + '\''
                + ", apellido='" + apellido + '\''
                + ", dni='" + dni + '\''
                + ", email='" + email + '\''
                + ", especialidad='" + especialidad + '\''
                + ", cedulaProfesional='" + cedulaProfesional + '\''
                + '}';
    }
}
