package ru.javaops.masterjava.service.mail.dao;

import com.bertoncelj.jdbi.entitymapper.EntityMapperFactory;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import ru.javaops.masterjava.service.mail.model.SentMail;

@RegisterMapperFactory(EntityMapperFactory.class)
public abstract class SentMailDao implements AbstractDao {

    public SentMail insert(SentMail mail) {
        if (mail.isNew()) {
            int id = insertGeneratedId(mail);
            mail.setId(id);
        }
        return mail;
    }

    @SqlUpdate("INSERT INTO sent_mail (from_mail, to_mail, result) VALUES (:from_mail, :to_mail, :result) ")
    @GetGeneratedKeys
    abstract int insertGeneratedId(@BindBean SentMail sentMail);

    //   http://stackoverflow.com/questions/13223820/postgresql-delete-all-content
    @SqlUpdate("TRUNCATE users CASCADE")
    @Override
    public abstract void clean();
}
