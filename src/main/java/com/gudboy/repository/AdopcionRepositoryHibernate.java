package com.gudboy.repository;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.gudboy.domain.animal.model.Adopcion;
import com.gudboy.infrastructure.HibernateUtil;

public class AdopcionRepositoryHibernate implements IAdopcionRepository {

    @Override
    public void guardar(Adopcion adopcion) {
        Transaction tx = null;
        try (Session session = HibernateUtil.openSession()) {
            tx = session.beginTransaction();
            session.persist(adopcion);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Error al guardar adopción", e);
        }
    }

    @Override
    public void actualizar(Adopcion adopcion) {
        Transaction tx = null;
        try (Session session = HibernateUtil.openSession()) {
            tx = session.beginTransaction();
            session.merge(adopcion);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Error al actualizar adopción", e);
        }
    }

    @Override
    public List<Adopcion> listarTodos() {
        try (Session session = HibernateUtil.openSession()) {
            return session.createQuery("from Adopcion", Adopcion.class).list();
        }
    }

    @Override
    public Optional<Adopcion> buscarPorId(int id) {
        try (Session session = HibernateUtil.openSession()) {
            return Optional.ofNullable(session.get(Adopcion.class, id));
        }
    }
}
