package com.gudboy.repository;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import com.gudboy.domain.alarma.model.Alarma;
import com.gudboy.infrastructure.HibernateUtil;

public class AlarmaRepositoryHibernate implements IAlarmaRepository {

    @Override
    public void add(Alarma alarma) {
        Transaction tx = null;
        try (Session session = HibernateUtil.openSession()) {
            tx = session.beginTransaction();
            session.persist(alarma);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Error al guardar la alarma", e);
        }
    }

    @Override
    public void remove(Alarma alarma) {
        Transaction tx = null;
        try (Session session = HibernateUtil.openSession()) {
            tx = session.beginTransaction();
            // Asegurar que el objeto esté gestionado por la sesión antes de borrarlo
            Alarma aBorrar = session.get(Alarma.class, alarma.getId());
            if (aBorrar != null) {
                session.remove(aBorrar);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Error al eliminar la alarma", e);
        }
    }

    @Override
    public Alarma getById(int id) {
        try (Session session = HibernateUtil.openSession()) {
            return session.get(Alarma.class, id);
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener la alarma por ID", e);
        }
    }

    @Override
    public List<Alarma> getAll() {
        try (Session session = HibernateUtil.openSession()) {
            return session.createQuery("from Alarma", Alarma.class).list();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener todas las alarmas", e);
        }
    }

    @Override
    public void update(Alarma alarma) {
        Transaction tx = null;
        try (Session session = HibernateUtil.openSession()) {
            tx = session.beginTransaction();
            session.merge(alarma);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Error al actualizar la alarma", e);
        }
    }
}