package ru.rz.musicat.data.facade;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.rz.musicat.data.facade.detail.DataChangeSession;

public class DataAccessFacade {

    private SessionFactory sessionFactory = null;

    private SessionFactory configureSessionFactory() throws HibernateException {

        sessionFactory = new Configuration().configure().buildSessionFactory();

        return sessionFactory;
    }

    public DataChangeSession BeginTransaction() {
        return new DataChangeSession(sessionFactory);
    }

    public DataAccessFacade() {
        configureSessionFactory();
    }

    public void Close() {
        sessionFactory.close();
    }
}
