package ru.javaops.masterjava.dao;
import ru.javaops.masterjava.dao.AbstractDaoUpload;
import ru.javaops.masterjava.persist.dao.UserDao;

public class UserDaoUpload extends AbstractDaoUpload<UserDao> {

    public UserDaoUpload(Class<UserDao> userDaoClass) {
        super(userDaoClass);
    }
}
