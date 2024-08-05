package com.nazar.usermanagement.repository;

import com.nazar.usermanagement.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class UserHibernate {
    private static SessionFactory sessionFactory;

    public void createUser(User user) {
        Session session = sessionFactory.getCurrentSession();
        session.save(user);
    }

    public User findById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(User.class, id);
    }

    public void removeUser(Long id) {
        Session session = sessionFactory.getCurrentSession();
        User user = session.get(User.class, id);
        if (user != null) {
            session.delete(user);
        }
    }

    public List<User> getAllUsers() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from User", User.class).getResultList();
    }
}
