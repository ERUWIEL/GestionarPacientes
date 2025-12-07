package com.mycompany.gestionarpacientes.service;

import com.mycompany.gestionarpacientes.dto.PacienteDTO;
import com.mycompany.gestionarpacientes.exceptions.ServiceException;
import java.util.List;

/**
 * Interfaz de servicio para la gestión de pacientes Define las operaciones de
 * negocio para administrar pacientes
 *
 * @author gatog
 */
public interface IPacienteService {

    /**
     * Registra un nuevo paciente en el sistema
     *
     * @param pacienteDTO datos del paciente a registrar
     * @return PacienteDTO con el ID asignado
     * @throws ServiceException si hay error en la validacion o persistencia
     */
    PacienteDTO registrarPaciente(PacienteDTO pacienteDTO) throws ServiceException;

    /**
     * Actualiza los datos de un paciente existente
     *
     * @param pacienteDTO datos actualizados del paciente
     * @return PacienteDTO actualizado
     * @throws ServiceException si el paciente no existe o hay error en
     * validacion
     */
    PacienteDTO actualizarPaciente(PacienteDTO pacienteDTO) throws ServiceException;

    /**
     * Elimina un paciente del sistema por su ID
     *
     * @param id identificador del paciente
     * @return PacienteDTO del paciente eliminado
     * @throws ServiceException si el paciente no existe o no puede eliminarse
     */
    PacienteDTO eliminarPaciente(Long id) throws ServiceException;

    /**
     * Busca un paciente por su ID
     *
     * @param id identificador del paciente
     * @return PacienteDTO encontrado o null si no existe
     * @throws ServiceException si hay error en la busqueda
     */
    PacienteDTO buscarPacientePorId(Long id) throws ServiceException;

    /**
     * Busca pacientes por nombre
     *
     * @param nombre nombre o parte del nombre a buscar
     * @return lista de pacientes que coinciden
     * @throws ServiceException si hay error en la busqueda
     */
    List<PacienteDTO> buscarPacientesPorNombre(String nombre) throws ServiceException;

    /**
     * Busca pacientes por tipo de seguro medico
     *
     * @param tipoSeguro tipo de seguro a buscar
     * @return lista de pacientes con ese tipo de seguro
     * @throws ServiceException si hay error en la busqueda
     */
    List<PacienteDTO> buscarPacientesPorTipoSeguro(String tipoSeguro) throws ServiceException;

    /**
     * Busca un paciente por su DNI
     *
     * @param dni documento de identificación
     * @return PacienteDTO encontrado o null si no existe
     * @throws ServiceException si hay error en la busqueda
     */
    PacienteDTO buscarPacientePorDni(String dni) throws ServiceException;

    /**
     * Obtiene todos los pacientes registrados
     *
     * @return lista de todos los pacientes
     * @throws ServiceException si hay error al listar
     */
    List<PacienteDTO> listarTodosPacientes() throws ServiceException;
}
