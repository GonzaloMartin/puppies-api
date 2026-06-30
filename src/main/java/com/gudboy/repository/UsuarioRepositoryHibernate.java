package com.gudboy.repository;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.gudboy.domain.Usuario.Usuario;
import com.gudboy.infrastructure.HibernateUtil;

public class UsuarioRepositoryHibernate implements IUsuarioRepository {

    @Override
    public void guardar(Usuario usuario) {
        Transaction tx = null;
        try (Session session = HibernateUtil.openSession()) {
            tx = session.beginTransaction();
            session.persist(usuario);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Error al guardar usuario", e);
        }
    }

    @Override
    public List<Usuario> listarTodos() {
        try (Session session = HibernateUtil.openSession()) {
            return session.createQuery("from Usuario", Usuario.class).list();
        }
    }
}
