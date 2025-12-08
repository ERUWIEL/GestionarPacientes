package com.mycompany.gestionarpacientes.service.impl;

import com.mycompany.gestionarpacientes.dto.PacienteDTO;
import com.mycompany.gestionarpacientes.entitys.Paciente;
import com.mycompany.gestionarpacientes.exceptions.DuplicateEntityException;
import com.mycompany.gestionarpacientes.exceptions.EntityNotFoundException;
import com.mycompany.gestionarpacientes.exceptions.RepositoryException;
import com.mycompany.gestionarpacientes.exceptions.ServiceException;
import com.mycompany.gestionarpacientes.repository.IPacienteRepository;
import com.mycompany.gestionarpacientes.service.IPacienteService;
import com.mycompany.gestionarpacientes.util.JpaUtil;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de gestión de pacientes Contiene la lógica de
 * negocio y validaciones
 *
 * @author gatog
 */
public class PacienteService implements IPacienteService {

    private final IPacienteRepository pacienteRepository;

    public PacienteService(IPacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    @Override
    public PacienteDTO registrarPaciente(PacienteDTO pacienteDTO) throws ServiceException {
        try {
            validarDatosPaciente(pacienteDTO);

            PacienteDTO existente = buscarPacientePorDni(pacienteDTO.getDni());
            if (existente != null) {
                throw new DuplicateEntityException("Ya existe un paciente con el DNI: " + pacienteDTO.getDni());
            }

            // Iniciar transacción
            JpaUtil.beginTransaction();

            // Convertir DTO a entidad
            Paciente paciente = new Paciente();
            paciente.setNombre(pacienteDTO.getNombre());
            paciente.setApellido(pacienteDTO.getApellido());
            paciente.setDni(pacienteDTO.getDni());
            paciente.setEmail(pacienteDTO.getEmail());
            paciente.setFechaNacimiento(pacienteDTO.getFechaNacimiento());
            paciente.setTipoSangre(pacienteDTO.getTipoSangre());
            paciente.setSeguroMedico(pacienteDTO.getSeguroMedico());

            // Guardar en el repositorio
            Paciente pacienteGuardado = pacienteRepository.agregar(paciente);

            // Commit transacción
            JpaUtil.commitTransaction();

            // Convertir entidad a DTO y retornar
            return convertirADTO(pacienteGuardado);

        } catch (DuplicateEntityException e) {
            JpaUtil.rollbackTransaction();
            throw new ServiceException("registrar paciente", "DNI duplicado", e);
        } catch (RepositoryException e) {
            JpaUtil.rollbackTransaction();
            throw new ServiceException("registrar paciente", "error en la persistencia", e);
        } catch (Exception e) {
            JpaUtil.rollbackTransaction();
            throw new ServiceException("registrar paciente", "error inesperado", e);
        } finally {
            JpaUtil.closeEntityManager();
        }
    }

    @Override
    public PacienteDTO actualizarPaciente(PacienteDTO pacienteDTO) throws ServiceException {
        try {
            // Validar que el ID no sea nulo
            if (pacienteDTO.getId() == null) {
                throw new ServiceException("El ID del paciente no puede ser nulo");
            }

            // Validar datos del paciente
            validarDatosPaciente(pacienteDTO);

            // Iniciar transacción
            JpaUtil.beginTransaction();

            // Buscar el paciente existente
            Paciente pacienteExistente = pacienteRepository.buscarPorId(pacienteDTO.getId());
            if (pacienteExistente == null) {
                throw new EntityNotFoundException("Paciente", pacienteDTO.getId());
            }

            // Actualizar los datos
            pacienteExistente.setNombre(pacienteDTO.getNombre());
            pacienteExistente.setApellido(pacienteDTO.getApellido());
            pacienteExistente.setDni(pacienteDTO.getDni());
            pacienteExistente.setEmail(pacienteDTO.getEmail());
            pacienteExistente.setFechaNacimiento(pacienteDTO.getFechaNacimiento());
            pacienteExistente.setTipoSangre(pacienteDTO.getTipoSangre());
            pacienteExistente.setSeguroMedico(pacienteDTO.getSeguroMedico());

            // Actualizar en el repositorio
            Paciente pacienteActualizado = pacienteRepository.actualizar(pacienteExistente);

            // Commit transacción
            JpaUtil.commitTransaction();

            // Convertir y retornar
            return convertirADTO(pacienteActualizado);

        } catch (EntityNotFoundException e) {
            JpaUtil.rollbackTransaction();
            throw new ServiceException("actualizar paciente", "paciente no encontrado", e);
        } catch (RepositoryException e) {
            JpaUtil.rollbackTransaction();
            throw new ServiceException("actualizar paciente", "error en la persistencia", e);
        } catch (Exception e) {
            JpaUtil.rollbackTransaction();
            throw new ServiceException("actualizar paciente", "error inesperado", e);
        } finally {
            JpaUtil.closeEntityManager();
        }
    }

    @Override
    public PacienteDTO eliminarPaciente(Long id) throws ServiceException {
        try {
            // Validar ID
            if (id == null) {
                throw new ServiceException("El ID del paciente no puede ser nulo");
            }

            // Iniciar transacción
            JpaUtil.beginTransaction();

            // Eliminar del repositorio
            Paciente pacienteEliminado = pacienteRepository.eliminar(id);

            // Commit transacción
            JpaUtil.commitTransaction();

            // Convertir y retornar
            return convertirADTO(pacienteEliminado);

        } catch (EntityNotFoundException e) {
            JpaUtil.rollbackTransaction();
            throw new ServiceException("eliminar paciente", "paciente no encontrado", e);
        } catch (RepositoryException e) {
            JpaUtil.rollbackTransaction();
            throw new ServiceException("eliminar paciente", "error en la persistencia", e);
        } catch (Exception e) {
            JpaUtil.rollbackTransaction();
            throw new ServiceException("eliminar paciente", "error inesperado", e);
        } finally {
            JpaUtil.closeEntityManager();
        }
    }

    @Override
    public PacienteDTO buscarPacientePorId(Long id) throws ServiceException {
        try {
            if (id == null) {
                throw new ServiceException("El ID del paciente no puede ser nulo");
            }

            Paciente paciente = pacienteRepository.buscarPorId(id);
            return convertirADTO(paciente);

        } catch (RepositoryException e) {
            throw new ServiceException("buscar paciente por ID", "error en la búsqueda", e);
        } finally {
            JpaUtil.closeEntityManager();
        }
    }

    @Override
    public PacienteDTO buscarPacientePorDni(String dni) throws ServiceException {
        try {
            if (dni == null || dni.trim().isEmpty()) {
                throw new ServiceException("El DNI no puede estar vacío");
            }

            // Buscar todos los pacientes y filtrar por DNI exacto
            List<Paciente> pacientes = pacienteRepository.listarPorNombre(dni, 100, 0);

            // Filtrar por DNI exacto
            Paciente paciente = pacientes.stream()
                    .filter(p -> p.getDni().equals(dni))
                    .findFirst()
                    .orElse(null);

            return convertirADTO(paciente);

        } catch (RepositoryException e) {
            throw new ServiceException("buscar paciente por DNI", "error en la búsqueda", e);
        } finally {
            JpaUtil.closeEntityManager();
        }
    }

    @Override
    public List<PacienteDTO> buscarPacientesPorNombre(String nombre, int limit, int offset) throws ServiceException {
        try {
            if (nombre == null || nombre.trim().isEmpty()) {
                throw new ServiceException("El nombre no puede estar vacío");
            }

            validarPaginacion(limit, offset);

            List<Paciente> pacientes = pacienteRepository.listarPorNombre(nombre, limit, offset);
            return pacientes.stream()
                    .map(this::convertirADTO)
                    .collect(Collectors.toList());

        } catch (RepositoryException e) {
            throw new ServiceException("buscar pacientes por nombre", "error en la búsqueda", e);
        } finally {
            JpaUtil.closeEntityManager();
        }
    }

    @Override
    public List<PacienteDTO> buscarPacientesPorTipoSeguro(String tipoSeguro, int limit, int offset) throws ServiceException {
        try {
            if (tipoSeguro == null || tipoSeguro.trim().isEmpty()) {
                throw new ServiceException("El tipo de seguro no puede estar vacío");
            }

            validarPaginacion(limit, offset);

            List<Paciente> pacientes = pacienteRepository.listarPorTipoDeSeguro(tipoSeguro, limit, offset);
            return pacientes.stream()
                    .map(this::convertirADTO)
                    .collect(Collectors.toList());

        } catch (RepositoryException e) {
            throw new ServiceException("buscar pacientes por tipo de seguro", "error en la búsqueda", e);
        } finally {
            JpaUtil.closeEntityManager();
        }
    }

    @Override
    public List<PacienteDTO> listarTodosPacientes(int limit, int offset) throws ServiceException {
        try {
            validarPaginacion(limit, offset);

            List<Paciente> pacientes = pacienteRepository.listarTodos(limit, offset);
            return pacientes.stream()
                    .map(this::convertirADTO)
                    .collect(Collectors.toList());

        } catch (RepositoryException e) {
            throw new ServiceException("listar todos los pacientes", "error en la búsqueda", e);
        } finally {
            JpaUtil.closeEntityManager();
        }
    }

    /**
     * Convierte una entidad Paciente a PacienteDTO
     *
     * @param paciente entidad a convertir
     * @return PacienteDTO o null si paciente es null
     */
    private PacienteDTO convertirADTO(Paciente paciente) {
        if (paciente == null) {
            return null;
        }

        return new PacienteDTO(
                paciente.getId(),
                paciente.getNombre(),
                paciente.getApellido(),
                paciente.getDni(),
                paciente.getEmail(),
                paciente.getFechaNacimiento(),
                paciente.getTipoSangre(),
                paciente.getSeguroMedico()
        );
    }

    /**
     * Valida los datos básicos de un paciente
     *
     * @param pacienteDTO DTO a validar
     * @throws ServiceException si algún dato es inválido
     */
    private void validarDatosPaciente(PacienteDTO pacienteDTO) throws ServiceException {
        if (pacienteDTO == null) {
            throw new ServiceException("Los datos del paciente no pueden ser nulos");
        }

        if (pacienteDTO.getNombre() == null || pacienteDTO.getNombre().trim().isEmpty()) {
            throw new ServiceException("El nombre del paciente es obligatorio");
        }

        if (pacienteDTO.getApellido() == null || pacienteDTO.getApellido().trim().isEmpty()) {
            throw new ServiceException("El apellido del paciente es obligatorio");
        }

        if (pacienteDTO.getDni() == null || pacienteDTO.getDni().trim().isEmpty()) {
            throw new ServiceException("El DNI del paciente es obligatorio");
        }

        if (pacienteDTO.getFechaNacimiento() == null) {
            throw new ServiceException("La fecha de nacimiento es obligatoria");
        }

        // Validar formato de email si está presente
        if (pacienteDTO.getEmail() != null && !pacienteDTO.getEmail().isEmpty()) {
            if (!pacienteDTO.getEmail().matches(
                    "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
                throw new ServiceException("El formato del email no es válido");
            }
        }
    }

    /**
     * Valida los parámetros de paginación
     *
     * @param limit número máximo de registros
     * @param offset número de registros a saltar
     * @throws ServiceException si los parámetros son inválidos
     */
    private void validarPaginacion(int limit, int offset) throws ServiceException {
        if (limit <= 0) {
            throw new ServiceException("El límite debe ser mayor a 0");
        }

        if (offset < 0) {
            throw new ServiceException("El offset no puede ser negativo");
        }

        if (limit > 1000) {
            throw new ServiceException("El límite no puede ser mayor a 1000");
        }
    }
}
