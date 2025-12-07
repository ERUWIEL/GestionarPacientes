package com.mycompany.gestionarpacientes.repository.impl;

import com.mycompany.gestionarpacientes.entitys.Doctor;
import com.mycompany.gestionarpacientes.exceptions.EntityNotFoundException;
import com.mycompany.gestionarpacientes.exceptions.RepositoryException;
import com.mycompany.gestionarpacientes.repository.IDoctorRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 *
 * @author gatog
 */
public class DoctorRepository implements IDoctorRepository {

    private final EntityManager entityManager;

    public DoctorRepository(EntityManager em) {
        this.entityManager = em;
    }

    @Override
    public Doctor agregar(Doctor doctor) throws RepositoryException {
        try {
            entityManager.persist(doctor);
            entityManager.flush();
            return doctor;
        } catch (Exception e) {
            throw new RepositoryException("insertar", "no se pudo insertar el doctor", e);
        }
    }

    @Override
    public Doctor actualizar(Doctor doctor) throws RepositoryException {
        try {
            return entityManager.merge(doctor);
        } catch (Exception e) {
            throw new RepositoryException("actualizar", "no se pudo actualizar el doctor", e);
        }
    }

    @Override
    public Doctor eliminar(Long id) throws RepositoryException {
        try {
            Doctor doctor = entityManager.find(Doctor.class, id);
            if (doctor == null) {
                throw new EntityNotFoundException("no se encontró el doctor indicado");
            }
            entityManager.remove(doctor);
            return doctor;
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RepositoryException("eliminar", "no se pudo eliminar el doctor", e);
        }
    }

    @Override
    public Doctor buscarPorId(Long id) throws RepositoryException {
        try {
            return entityManager.find(Doctor.class, id);
        } catch (Exception e) {
            throw new RepositoryException("buscar", "no se pudo buscar el doctor", e);
        }
    }

    @Override
    public List<Doctor> listarPorNombre(String nombre) throws RepositoryException {
        try {
            String jpql = "SELECT d FROM Doctor d "
                    + "WHERE LOWER(d.nombre) LIKE LOWER(:nombre) "
                    + "ORDER BY d.nombre ASC";

            TypedQuery<Doctor> query = entityManager.createQuery(jpql, Doctor.class);
            query.setParameter("nombre", "%" + nombre + "%");

            return query.getResultList();
        } catch (Exception ex) {
            throw new RepositoryException("listarPorNombre", "no fue posible listar los doctores", ex);
        }
    }

    @Override
    public List<Doctor> listarPorEspecialidad(String especialidad) throws RepositoryException {
        try {
            String jpql = "SELECT d FROM Doctor d "
                    + "WHERE LOWER(d.especialidad) LIKE LOWER(:especialidad) "
                    + "ORDER BY d.nombre ASC";

            TypedQuery<Doctor> query = entityManager.createQuery(jpql, Doctor.class);
            query.setParameter("especialidad", "%" + especialidad + "%");

            return query.getResultList();
        } catch (Exception ex) {
            throw new RepositoryException("listarPorEspecialidad", "no fue posible listar los doctores", ex);
        }
    }
}
