package com.mycompany.gestionarpacientes.service;

import com.mycompany.gestionarpacientes.dto.DoctorDTO;
import com.mycompany.gestionarpacientes.exceptions.ServiceException;
import java.util.List;

/**
 * Interfaz de servicio para la gestión de doctores Define las operaciones de
 * negocio para administrar doctores
 *
 * @author gatog
 */
public interface IDoctorService {

    /**
     * Registra un nuevo doctor en el sistema
     *
     * @param doctorDTO datos del doctor a registrar
     * @return DoctorDTO con el ID asignado
     * @throws ServiceException si hay error en la validacion o persistencia
     */
    DoctorDTO registrarDoctor(DoctorDTO doctorDTO) throws ServiceException;

    /**
     * Actualiza los datos de un doctor existente
     *
     * @param doctorDTO datos actualizados del doctor
     * @return DoctorDTO actualizado
     * @throws ServiceException si el doctor no existe o hay error en validacion
     */
    DoctorDTO actualizarDoctor(DoctorDTO doctorDTO) throws ServiceException;

    /**
     * Elimina un doctor del sistema por su ID
     *
     * @param id identificador del doctor
     * @return DoctorDTO del doctor eliminado
     * @throws ServiceException si el doctor no existe o no puede eliminarse
     */
    DoctorDTO eliminarDoctor(Long id) throws ServiceException;

    /**
     * Busca un doctor por su ID
     *
     * @param id identificador del doctor
     * @return DoctorDTO encontrado o null si no existe
     * @throws ServiceException si hay error en la busqueda
     */
    DoctorDTO buscarDoctorPorId(Long id) throws ServiceException;

    /**
     * Busca doctores por nombre
     *
     * @param nombre nombre o parte del nombre a buscar
     * @return lista de doctores que coinciden
     * @throws ServiceException si hay error en la busqueda
     */
    List<DoctorDTO> buscarDoctoresPorNombre(String nombre) throws ServiceException;

    /**
     * Busca doctores por especialidad medica
     *
     * @param especialidad especialidad a buscar
     * @return lista de doctores con esa especialidad
     * @throws ServiceException si hay error en la busqueda
     */
    List<DoctorDTO> buscarDoctoresPorEspecialidad(String especialidad) throws ServiceException;

    /**
     * Busca un doctor por su cedula profesional
     *
     * @param cedulaProfesional numero de cedula profesional
     * @return DoctorDTO encontrado o null si no existe
     * @throws ServiceException si hay error en la busqueda
     */
    DoctorDTO buscarDoctorPorCedula(String cedulaProfesional) throws ServiceException;

    /**
     * Obtiene todos los doctores registrados
     *
     * @return lista de todos los doctores
     * @throws ServiceException si hay error al listar
     */
    List<DoctorDTO> listarTodosDoctores() throws ServiceException;
}
