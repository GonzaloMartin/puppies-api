package com.gudboy.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.gudboy.domain.animal.model.Animal;
import com.gudboy.infrastructure.HibernateUtil;

public class AnimalRepositoryHibernate implements IAnimalRepository {

    @Override
    public void guardar(Animal animal) {
        Transaction tx = null;
        try (Session session = HibernateUtil.openSession()) {
            tx = session.beginTransaction();
            session.persist(animal);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Error al guardar animal", e);
        }
    }

    @Override
    public void actualizar(Animal animal) {
        Transaction tx = null;
        try (Session session = HibernateUtil.openSession()) {
            tx = session.beginTransaction();
            session.merge(animal);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Error al actualizar animal", e);
        }
    }

    @Override
    public Optional<Animal> buscarPorId(UUID id) {
        try (Session session = HibernateUtil.openSession()) {
            return Optional.ofNullable(session.get(Animal.class, id));
        }
    }

    @Override
    public List<Animal> listarTodos() {
        try (Session session = HibernateUtil.openSession()) {
            return session.createQuery("from Animal", Animal.class).list();
        }
    }
}
