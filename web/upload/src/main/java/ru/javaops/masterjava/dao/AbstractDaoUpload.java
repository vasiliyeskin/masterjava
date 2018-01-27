package ru.javaops.masterjava.dao;

import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.dao.AbstractDao;

import java.sql.DriverManager;

public abstract class AbstractDaoUpload<DAO extends AbstractDao> {

        static {
            DBIProvider.init(() -> {
                try {
                    Class.forName("org.postgresql.Driver");
                } catch (ClassNotFoundException e) {
                    throw new IllegalStateException("PostgreSQL driver not found", e);
                }
                return DriverManager.getConnection("jdbc:postgresql://localhost:5432/masterjava", "user", "password");
            });
        }

        protected DAO dao;

        protected AbstractDaoUpload(Class<DAO> daoClass) {
            this.dao = DBIProvider.getDao(daoClass);
        }

    public DAO getDao() {
        return dao;
    }
}
