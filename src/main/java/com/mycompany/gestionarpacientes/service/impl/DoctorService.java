package com.mycompany.gestionarpacientes.service.impl;

import com.mycompany.gestionarpacientes.dto.DoctorDTO;
import com.mycompany.gestionarpacientes.entitys.Doctor;
import com.mycompany.gestionarpacientes.exceptions.DuplicateEntityException;
import com.mycompany.gestionarpacientes.exceptions.EntityNotFoundException;
import com.mycompany.gestionarpacientes.exceptions.RepositoryException;
import com.mycompany.gestionarpacientes.exceptions.ServiceException;
import com.mycompany.gestionarpacientes.repository.IDoctorRepository;
import com.mycompany.gestionarpacientes.service.IDoctorService;
import com.mycompany.gestionarpacientes.util.JpaUtil;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de gestión de doctores Contiene la lógica de
 * negocio y validaciones
 *
 * @author gatog
 */
public class DoctorService implements IDoctorService {

    private final IDoctorRepository doctorRepository;

    public DoctorService(IDoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @Override
    public DoctorDTO registrarDoctor(DoctorDTO doctorDTO) throws ServiceException {
        try {
            // Validar datos del doctor
            validarDatosDoctor(doctorDTO);

            // Verificar que no exista un doctor con la misma cédula
            DoctorDTO existente = buscarDoctorPorCedula(doctorDTO.getCedulaProfesional());
            if (existente != null) {
                throw new DuplicateEntityException("Ya existe un doctor con la cédula: " + doctorDTO.getCedulaProfesional());
            }

            // Iniciar transacción
            JpaUtil.beginTransaction();

            // Convertir DTO a entidad
            Doctor doctor = new Doctor();
            doctor.setNombre(doctorDTO.getNombre());
            doctor.setApellido(doctorDTO.getApellido());
            doctor.setDni(doctorDTO.getDni());
            doctor.setEmail(doctorDTO.getEmail());
            doctor.setEspecialidad(doctorDTO.getEspecialidad());
            doctor.setCedulaProfesional(doctorDTO.getCedulaProfesional());

            // Guardar en el repositorio
            Doctor doctorGuardado = doctorRepository.agregar(doctor);

            // Commit transacción
            JpaUtil.commitTransaction();

            // Convertir entidad a DTO y retornar
            return convertirADTO(doctorGuardado);

        } catch (DuplicateEntityException e) {
            JpaUtil.rollbackTransaction();
            throw new ServiceException("registrar doctor", "cédula duplicada", e);
        } catch (RepositoryException e) {
            JpaUtil.rollbackTransaction();
            throw new ServiceException("registrar doctor", "error en la persistencia", e);
        } catch (Exception e) {
            JpaUtil.rollbackTransaction();
            throw new ServiceException("registrar doctor", "error inesperado", e);
        } finally {
            JpaUtil.closeEntityManager();
        }
    }

    @Override
    public DoctorDTO actualizarDoctor(DoctorDTO doctorDTO) throws ServiceException {
        try {
            // Validar que el ID no sea nulo
            if (doctorDTO.getId() == null) {
                throw new ServiceException("El ID del doctor no puede ser nulo");
            }

            // Validar datos del doctor
            validarDatosDoctor(doctorDTO);

            // Iniciar transacción
            JpaUtil.beginTransaction();

            // Buscar el doctor existente
            Doctor doctorExistente = doctorRepository.buscarPorId(doctorDTO.getId());
            if (doctorExistente == null) {
                throw new EntityNotFoundException("Doctor", doctorDTO.getId());
            }

            // Actualizar los datos
            doctorExistente.setNombre(doctorDTO.getNombre());
            doctorExistente.setApellido(doctorDTO.getApellido());
            doctorExistente.setDni(doctorDTO.getDni());
            doctorExistente.setEmail(doctorDTO.getEmail());
            doctorExistente.setEspecialidad(doctorDTO.getEspecialidad());
            doctorExistente.setCedulaProfesional(doctorDTO.getCedulaProfesional());

            // Actualizar en el repositorio
            Doctor doctorActualizado = doctorRepository.actualizar(doctorExistente);

            // Commit transacción
            JpaUtil.commitTransaction();

            // Convertir y retornar
            return convertirADTO(doctorActualizado);

        } catch (EntityNotFoundException e) {
            JpaUtil.rollbackTransaction();
            throw new ServiceException("actualizar doctor", "doctor no encontrado", e);
        } catch (RepositoryException e) {
            JpaUtil.rollbackTransaction();
            throw new ServiceException("actualizar doctor", "error en la persistencia", e);
        } catch (Exception e) {
            JpaUtil.rollbackTransaction();
            throw new ServiceException("actualizar doctor", "error inesperado", e);
        } finally {
            JpaUtil.closeEntityManager();
        }
    }

    @Override
    public DoctorDTO eliminarDoctor(Long id) throws ServiceException {
        try {
            // Validar ID
            if (id == null) {
                throw new ServiceException("El ID del doctor no puede ser nulo");
            }

            // Iniciar transacción
            JpaUtil.beginTransaction();

            // Eliminar del repositorio
            Doctor doctorEliminado = doctorRepository.eliminar(id);

            // Commit transacción
            JpaUtil.commitTransaction();

            // Convertir y retornar
            return convertirADTO(doctorEliminado);

        } catch (EntityNotFoundException e) {
            JpaUtil.rollbackTransaction();
            throw new ServiceException("eliminar doctor", "doctor no encontrado", e);
        } catch (RepositoryException e) {
            JpaUtil.rollbackTransaction();
            throw new ServiceException("eliminar doctor", "error en la persistencia", e);
        } catch (Exception e) {
            JpaUtil.rollbackTransaction();
            throw new ServiceException("eliminar doctor", "error inesperado", e);
        } finally {
            JpaUtil.closeEntityManager();
        }
    }

    @Override
    public DoctorDTO buscarDoctorPorId(Long id) throws ServiceException {
        try {
            if (id == null) {
                throw new ServiceException("El ID del doctor no puede ser nulo");
            }

            Doctor doctor = doctorRepository.buscarPorId(id);
            return convertirADTO(doctor);

        } catch (RepositoryException e) {
            throw new ServiceException("buscar doctor por ID", "error en la búsqueda", e);
        } finally {
            JpaUtil.closeEntityManager();
        }
    }

    @Override
    public DoctorDTO buscarDoctorPorCedula(String cedulaProfesional) throws ServiceException {
        try {
            if (cedulaProfesional == null || cedulaProfesional.trim().isEmpty()) {
                throw new ServiceException("La cédula profesional no puede estar vacía");
            }

            // Buscar todos los doctores y filtrar por cédula exacta
            List<Doctor> doctores = doctorRepository.listarPorNombre(cedulaProfesional, 100, 0);

            // Filtrar por cédula exacta
            Doctor doctor = doctores.stream()
                    .filter(d -> d.getCedulaProfesional().equals(cedulaProfesional))
                    .findFirst()
                    .orElse(null);

            return convertirADTO(doctor);

        } catch (RepositoryException e) {
            throw new ServiceException("buscar doctor por cédula", "error en la búsqueda", e);
        } finally {
            JpaUtil.closeEntityManager();
        }
    }

    @Override
    public List<DoctorDTO> buscarDoctoresPorNombre(String nombre, int limit, int offset) throws ServiceException {
        try {
            if (nombre == null || nombre.trim().isEmpty()) {
                throw new ServiceException("El nombre no puede estar vacío");
            }

            validarPaginacion(limit, offset);

            List<Doctor> doctores = doctorRepository.listarPorNombre(nombre, limit, offset);
            return doctores.stream()
                    .map(this::convertirADTO)
                    .collect(Collectors.toList());

        } catch (RepositoryException e) {
            throw new ServiceException("buscar doctores por nombre", "error en la búsqueda", e);
        } finally {
            JpaUtil.closeEntityManager();
        }
    }

    @Override
    public List<DoctorDTO> buscarDoctoresPorEspecialidad(String especialidad, int limit, int offset) throws ServiceException {
        try {
            if (especialidad == null || especialidad.trim().isEmpty()) {
                throw new ServiceException("La especialidad no puede estar vacía");
            }

            validarPaginacion(limit, offset);

            List<Doctor> doctores = doctorRepository.listarPorEspecialidad(especialidad, limit, offset);
            return doctores.stream()
                    .map(this::convertirADTO)
                    .collect(Collectors.toList());

        } catch (RepositoryException e) {
            throw new ServiceException("buscar doctores por especialidad", "error en la búsqueda", e);
        } finally {
            JpaUtil.closeEntityManager();
        }
    }

    @Override
    public List<DoctorDTO> listarTodosDoctores(int limit, int offset) throws ServiceException {
        try {
            validarPaginacion(limit, offset);

            List<Doctor> doctores = doctorRepository.listarTodos(limit, offset);
            return doctores.stream()
                    .map(this::convertirADTO)
                    .collect(Collectors.toList());

        } catch (RepositoryException e) {
            throw new ServiceException("listar todos los doctores", "error en la búsqueda", e);
        } finally {
            JpaUtil.closeEntityManager();
        }
    }

    /**
     * Convierte una entidad Doctor a DoctorDTO
     *
     * @param doctor entidad a convertir
     * @return DoctorDTO o null si doctor es null
     */
    private DoctorDTO convertirADTO(Doctor doctor) {
        if (doctor == null) {
            return null;
        }

        return new DoctorDTO(
                doctor.getId(),
                doctor.getNombre(),
                doctor.getApellido(),
                doctor.getDni(),
                doctor.getEmail(),
                doctor.getEspecialidad(),
                doctor.getCedulaProfesional()
        );
    }

    /**
     * Valida los datos básicos de un doctor
     *
     * @param doctorDTO DTO a validar
     * @throws ServiceException si algún dato es inválido
     */
    private void validarDatosDoctor(DoctorDTO doctorDTO) throws ServiceException {
        if (doctorDTO == null) {
            throw new ServiceException("Los datos del doctor no pueden ser nulos");
        }

        if (doctorDTO.getNombre() == null || doctorDTO.getNombre().trim().isEmpty()) {
            throw new ServiceException("El nombre del doctor es obligatorio");
        }

        if (doctorDTO.getApellido() == null || doctorDTO.getApellido().trim().isEmpty()) {
            throw new ServiceException("El apellido del doctor es obligatorio");
        }

        if (doctorDTO.getDni() == null || doctorDTO.getDni().trim().isEmpty()) {
            throw new ServiceException("El DNI del doctor es obligatorio");
        }

        if (doctorDTO.getEspecialidad() == null || doctorDTO.getEspecialidad().trim().isEmpty()) {
            throw new ServiceException("La especialidad es obligatoria");
        }

        if (doctorDTO.getCedulaProfesional() == null || doctorDTO.getCedulaProfesional().trim().isEmpty()) {
            throw new ServiceException("La cédula profesional es obligatoria");
        }

        // Validar formato de email si está presente
        if (doctorDTO.getEmail() != null && !doctorDTO.getEmail().isEmpty()) {
            if (!doctorDTO.getEmail().matches(
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
