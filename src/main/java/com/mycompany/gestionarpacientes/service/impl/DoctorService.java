package com.mycompany.gestionarpacientes.service.impl;

import com.mycompany.gestionarpacientes.dto.DoctorDTO;
import com.mycompany.gestionarpacientes.entitys.Doctor;
import com.mycompany.gestionarpacientes.exceptions.DuplicateEntityException;
import com.mycompany.gestionarpacientes.exceptions.EntityNotFoundException;
import com.mycompany.gestionarpacientes.exceptions.RepositoryException;
import com.mycompany.gestionarpacientes.exceptions.ServiceException;
import com.mycompany.gestionarpacientes.repository.IDoctorRepository;
import com.mycompany.gestionarpacientes.repository.impl.DoctorRepository;
import com.mycompany.gestionarpacientes.service.IDoctorService;
import com.mycompany.gestionarpacientes.util.JpaUtil;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de gestión de doctores
 * 
 * @author gatog
 */
public class DoctorService implements IDoctorService {

    @Override
    public DoctorDTO registrarDoctor(DoctorDTO doctorDTO) throws ServiceException {
        try {
            validarDatosDoctor(doctorDTO);
            
            DoctorDTO existente = buscarDoctorPorCedula(doctorDTO.getCedulaProfesional());
            if (existente != null) {
                throw new DuplicateEntityException("Ya existe un doctor con la cédula: " + doctorDTO.getCedulaProfesional());
            }
            
            JpaUtil.beginTransaction();
            
            IDoctorRepository repository = new DoctorRepository(JpaUtil.getEntityManager());
            
            Doctor doctor = new Doctor();
            doctor.setNombre(doctorDTO.getNombre());
            doctor.setApellido(doctorDTO.getApellido());
            doctor.setDni(doctorDTO.getDni());
            doctor.setEmail(doctorDTO.getEmail());
            doctor.setEspecialidad(doctorDTO.getEspecialidad());
            doctor.setCedulaProfesional(doctorDTO.getCedulaProfesional());
            
            Doctor doctorGuardado = repository.agregar(doctor);
            
            JpaUtil.commitTransaction();
            
            return convertirADTO(doctorGuardado);
            
        } catch (DuplicateEntityException e) {
            JpaUtil.rollbackTransaction();
            throw new ServiceException("registrar doctor", "cédula duplicada", e);
        } catch (RepositoryException e) {
            JpaUtil.rollbackTransaction();
            throw new ServiceException("registrar doctor", "error en la persistencia", e);
        } catch (Exception e) {
            JpaUtil.rollbackTransaction();
            throw new ServiceException("registrar doctor", "error inesperado: " + e.getMessage(), e);
        } finally {
            JpaUtil.closeEntityManager();
        }
    }

    @Override
    public DoctorDTO actualizarDoctor(DoctorDTO doctorDTO) throws ServiceException {
        try {
            if (doctorDTO.getId() == null) {
                throw new ServiceException("El ID del doctor no puede ser nulo");
            }
            
            validarDatosDoctor(doctorDTO);
            
            JpaUtil.beginTransaction();
            
            IDoctorRepository repository = new DoctorRepository(JpaUtil.getEntityManager());
            
            Doctor doctorExistente = repository.buscarPorId(doctorDTO.getId());
            if (doctorExistente == null) {
                throw new EntityNotFoundException("Doctor", doctorDTO.getId());
            }
            
            doctorExistente.setNombre(doctorDTO.getNombre());
            doctorExistente.setApellido(doctorDTO.getApellido());
            doctorExistente.setDni(doctorDTO.getDni());
            doctorExistente.setEmail(doctorDTO.getEmail());
            doctorExistente.setEspecialidad(doctorDTO.getEspecialidad());
            doctorExistente.setCedulaProfesional(doctorDTO.getCedulaProfesional());
            
            Doctor doctorActualizado = repository.actualizar(doctorExistente);
            
            JpaUtil.commitTransaction();
            
            return convertirADTO(doctorActualizado);
            
        } catch (EntityNotFoundException e) {
            JpaUtil.rollbackTransaction();
            throw new ServiceException("actualizar doctor", "doctor no encontrado", e);
        } catch (RepositoryException e) {
            JpaUtil.rollbackTransaction();
            throw new ServiceException("actualizar doctor", "error en la persistencia", e);
        } catch (Exception e) {
            JpaUtil.rollbackTransaction();
            throw new ServiceException("actualizar doctor", "error inesperado: " + e.getMessage(), e);
        } finally {
            JpaUtil.closeEntityManager();
        }
    }

    @Override
    public DoctorDTO eliminarDoctor(Long id) throws ServiceException {
        try {
            if (id == null) {
                throw new ServiceException("El ID del doctor no puede ser nulo");
            }
            
            JpaUtil.beginTransaction();
            
            IDoctorRepository repository = new DoctorRepository(JpaUtil.getEntityManager());
            
            Doctor doctorEliminado = repository.eliminar(id);
            
            JpaUtil.commitTransaction();
            
            return convertirADTO(doctorEliminado);
            
        } catch (EntityNotFoundException e) {
            JpaUtil.rollbackTransaction();
            throw new ServiceException("eliminar doctor", "doctor no encontrado", e);
        } catch (RepositoryException e) {
            JpaUtil.rollbackTransaction();
            throw new ServiceException("eliminar doctor", "error en la persistencia", e);
        } catch (Exception e) {
            JpaUtil.rollbackTransaction();
            throw new ServiceException("eliminar doctor", "error inesperado: " + e.getMessage(), e);
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
            
            IDoctorRepository repository = new DoctorRepository(JpaUtil.getEntityManager());
            Doctor doctor = repository.buscarPorId(id);
            return convertirADTO(doctor);
            
        } catch (RepositoryException e) {
            throw new ServiceException("buscar doctor por ID", "error en la búsqueda", e);
        } catch (Exception e) {
            throw new ServiceException("buscar doctor por ID", "error inesperado: " + e.getMessage(), e);
        } finally {
            JpaUtil.closeEntityManager();
        }
    }

    @Override
    public DoctorDTO buscarDoctorPorCedula(String cedulaProfesional) throws ServiceException {
        try {
            if (cedulaProfesional == null || cedulaProfesional.trim().isEmpty()) {
                return null;
            }
            
            IDoctorRepository repository = new DoctorRepository(JpaUtil.getEntityManager());
            List<Doctor> doctores = repository.listarPorNombre(cedulaProfesional, 100, 0);
            
            Doctor doctor = doctores.stream()
                    .filter(d -> d.getCedulaProfesional().equals(cedulaProfesional))
                    .findFirst()
                    .orElse(null);
                    
            return convertirADTO(doctor);
            
        } catch (RepositoryException e) {
            throw new ServiceException("buscar doctor por cédula", "error en la búsqueda", e);
        } catch (Exception e) {
            throw new ServiceException("buscar doctor por cédula", "error inesperado: " + e.getMessage(), e);
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
            
            IDoctorRepository repository = new DoctorRepository(JpaUtil.getEntityManager());
            List<Doctor> doctores = repository.listarPorNombre(nombre, limit, offset);
            return doctores.stream()
                    .map(this::convertirADTO)
                    .collect(Collectors.toList());
                    
        } catch (RepositoryException e) {
            throw new ServiceException("buscar doctores por nombre", "error en la búsqueda", e);
        } catch (Exception e) {
            throw new ServiceException("buscar doctores por nombre", "error inesperado: " + e.getMessage(), e);
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
            
            IDoctorRepository repository = new DoctorRepository(JpaUtil.getEntityManager());
            List<Doctor> doctores = repository.listarPorEspecialidad(especialidad, limit, offset);
            return doctores.stream()
                    .map(this::convertirADTO)
                    .collect(Collectors.toList());
                    
        } catch (RepositoryException e) {
            throw new ServiceException("buscar doctores por especialidad", "error en la búsqueda", e);
        } catch (Exception e) {
            throw new ServiceException("buscar doctores por especialidad", "error inesperado: " + e.getMessage(), e);
        } finally {
            JpaUtil.closeEntityManager();
        }
    }

    @Override
    public List<DoctorDTO> listarTodosDoctores(int limit, int offset) throws ServiceException {
        try {
            validarPaginacion(limit, offset);
            
            IDoctorRepository repository = new DoctorRepository(JpaUtil.getEntityManager());
            List<Doctor> doctores = repository.listarTodos(limit, offset);
            return doctores.stream()
                    .map(this::convertirADTO)
                    .collect(Collectors.toList());
                    
        } catch (RepositoryException e) {
            throw new ServiceException("listar todos los doctores", "error en la búsqueda", e);
        } catch (Exception e) {
            throw new ServiceException("listar todos los doctores", "error inesperado: " + e.getMessage(), e);
        } finally {
            JpaUtil.closeEntityManager();
        }
    }
    
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
        
        if (doctorDTO.getEmail() != null && !doctorDTO.getEmail().isEmpty()) {
            if (!doctorDTO.getEmail().matches(
                    "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
                throw new ServiceException("El formato del email no es válido");
            }
        }
    }
    
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