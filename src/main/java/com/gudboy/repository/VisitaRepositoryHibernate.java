package com.gudboy.repository;

import com.gudboy.domain.seguimiento.model.Visita;
import com.gudboy.infrastructure.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class VisitaRepositoryHibernate implements IVisitaRepository {

    @Override
    public void guardar(Visita visita) {
        try (Session session = HibernateUtil.openSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(visita);
            tx.commit();
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar visita con Hibernate", e);
        }
    }

    @Override
    public Optional<Visita> buscarPorId(UUID id) {
        try (Session session = HibernateUtil.openSession()) {
            Visita v = session.get(Visita.class, id);
            if (v != null && v.getSeguimiento() != null && v.getSeguimiento().getAdopcion() != null) {
                org.hibernate.Hibernate.initialize(v.getSeguimiento().getAdopcion().getAnimales());
            }
            return Optional.ofNullable(v);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar visita por ID con Hibernate", e);
        }
    }

    @Override
    public List<Visita> listarPorSeguimiento(UUID seguimientoId) {
        try (Session session = HibernateUtil.openSession()) {
            return session.createQuery("from Visita v where v.seguimiento.id = :sid", Visita.class)
                    .setParameter("sid", seguimientoId)
                    .list();
        } catch (Exception e) {
            throw new RuntimeException("Error al listar visitas por seguimiento con Hibernate", e);
        }
    }

    @Override
    public void actualizar(Visita visita) {
        try (Session session = HibernateUtil.openSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(visita);
            tx.commit();
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar visita con Hibernate", e);
        }
    }
}
