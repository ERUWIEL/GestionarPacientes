package com.mycompany.gestionarpacientes.repository;

import com.mycompany.gestionarpacientes.entitys.Paciente;
import com.mycompany.gestionarpacientes.exceptions.RepositoryException;
import java.util.List;

/**
 *
 * @author gatog
 */
public interface IPacienteRepository {

    Paciente agregar(Paciente paciente) throws RepositoryException;

    Paciente actualizar(Paciente paciente) throws RepositoryException;

    Paciente eliminar(Long id) throws RepositoryException;

    Paciente buscarPorId(Long id) throws RepositoryException;
    
    List<Paciente> listarTodos(int limit, int offset) throws RepositoryException;

    List<Paciente> listarPorNombre(String nombre, int limit, int offset) throws RepositoryException;

    List<Paciente> listarPorTipoDeSeguro(String tipoSeguro, int limit, int offset) throws RepositoryException;
}
