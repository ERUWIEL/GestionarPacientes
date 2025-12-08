package com.mycompany.gestionarpacientes.repository.impl;

import com.mycompany.gestionarpacientes.entitys.Paciente;
import com.mycompany.gestionarpacientes.exceptions.EntityNotFoundException;
import com.mycompany.gestionarpacientes.exceptions.RepositoryException;
import com.mycompany.gestionarpacientes.repository.IPacienteRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 * clase de acceso de datos
 *
 * @author gatog
 */
public class PacienteRepository implements IPacienteRepository {

    private final EntityManager entityManager;

    public PacienteRepository(EntityManager em) {
        this.entityManager = em;
    }

    @Override
    public Paciente agregar(Paciente paciente) throws RepositoryException {
        try {
            entityManager.persist(paciente);
            entityManager.flush();
            return paciente;
        } catch (Exception e) {
            throw new RepositoryException("insertar", "no se pudo insertar el paciente", e);
        }
    }

    @Override
    public Paciente actualizar(Paciente paciente) throws RepositoryException {
        try {
            return entityManager.merge(paciente);
        } catch (Exception e) {
            throw new RepositoryException("actualizar", "no se pudo actualizar el paciente", e);
        }
    }

    @Override
    public Paciente eliminar(Long id) throws RepositoryException {
        try {
            Paciente paciente = entityManager.find(Paciente.class, id);
            if (paciente == null) {
                throw new EntityNotFoundException("no se encontro el paciente indicado");
            }
            entityManager.remove(paciente);
            return paciente;
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RepositoryException("eliminar", "no se pudo eliminar el paciente", e);
        }
    }

    @Override
    public Paciente buscarPorId(Long id) throws RepositoryException {
        try {
            return entityManager.find(Paciente.class, id);
        } catch (Exception e) {
            throw new RepositoryException("buscar", "no se pudo buscar el paciente", e);
        }
    }

    @Override
    public List<Paciente> listarPorNombre(String nombre, int limit, int offset) throws RepositoryException {
        try {
            String jpql = "SELECT p FROM Paciente p "
                    + "WHERE LOWER(p.nombre) LIKE LOWER(:nombre) "
                    + "ORDER BY p.nombre ASC";

            TypedQuery<Paciente> query = entityManager.createQuery(jpql, Paciente.class);
            query.setParameter("nombre", "%" + nombre + "%");
            query.setMaxResults(limit);
            query.setFirstResult(offset);

            return query.getResultList();
        } catch (Exception ex) {
            throw new RepositoryException("listarPorNombre", "no fue posible listar los pacientes", ex);
        }
    }

    @Override
    public List<Paciente> listarPorTipoDeSeguro(String tipoSeguro, int limit, int offset) throws RepositoryException {
        try {
            String jpql = "SELECT p FROM Paciente p "
                    + "WHERE LOWER(p.seguroMedico) LIKE LOWER(:tipoSeguro) "
                    + "ORDER BY p.nombre ASC";

            TypedQuery<Paciente> query = entityManager.createQuery(jpql, Paciente.class);
            query.setParameter("tipoSeguro", "%" + tipoSeguro + "%");
            query.setMaxResults(limit);
            query.setFirstResult(offset);

            return query.getResultList();
        } catch (Exception ex) {
            throw new RepositoryException("listarPorTipoDeSeguro", "no fue posible listar los pacientes", ex);
        }
    }

    @Override
    public List<Paciente> listarTodos(int limit, int offset) throws RepositoryException {
        try {
            String jpql = "SELECT p FROM Paciente p "
                    + "ORDER BY p.nombre ASC";

            TypedQuery<Paciente> query = entityManager.createQuery(jpql, Paciente.class);
            query.setMaxResults(limit);
            query.setFirstResult(offset);

            return query.getResultList();
        } catch (Exception ex) {
            throw new RepositoryException("listarTodos", "no fue posible listar los pacientes", ex);
        }
    }
}
