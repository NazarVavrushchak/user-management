package com.nazar.usermanagement.repository;

import com.nazar.usermanagement.entity.Role;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class RoleHibernate {
    private static SessionFactory sessionFactory;

    public Role findById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Role.class, id);
    }

    public void save(Role role) {
        Session session = sessionFactory.getCurrentSession();
        session.save(role);
    }

    public List<Role> findAll() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from Role", Role.class).getResultList();
    }

    public void addRole(Role role) {
        Session session = sessionFactory.getCurrentSession();
        session.save(role);
    }
}