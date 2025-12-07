package com.mycompany.gestionarpacientes.repository;

import com.mycompany.gestionarpacientes.entitys.Doctor;
import com.mycompany.gestionarpacientes.exceptions.RepositoryException;
import java.util.List;

/**
 *
 * @author gatog
 */
public interface IDoctorRepository {

    Doctor agregar(Doctor doctor) throws RepositoryException;

    Doctor actualizar(Doctor doctor) throws RepositoryException;

    Doctor eliminar(Long id) throws RepositoryException;

    Doctor buscarPorId(Long id) throws RepositoryException;

    List<Doctor> listarPorNombre(String nombre, int limit, int offset) throws RepositoryException;

    List<Doctor> listarPorEspecialidad(String especialidad, int limit, int offset) throws RepositoryException;
}
