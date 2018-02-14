package ru.javaops.masterjava.service.mail;

import com.typesafe.config.Config;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.mail.*;
import ru.javaops.masterjava.config.Configs;
import ru.javaops.masterjava.service.mail.dao.SentMailDao;
import ru.javaops.masterjava.service.mail.model.SentMail;

import java.util.List;


@Slf4j
public class MailSender {
    static void sendMail(List<Addressee> to, List<Addressee> cc, String subject, String body) {
        log.info("Send mail to \'" + to + "\' cc \'" + cc + "\' subject \'" + subject + (log.isDebugEnabled() ? "\nbody=" + body : ""));

        SentMailDao sentMailDao = DBIProvider.getDao(SentMailDao.class);

        Config mailconfig = Configs.getConfig("mail.conf","mail");

        try {
            Email email = new SimpleEmail();
            email.setHostName(mailconfig.getString("host"));
            email.setSmtpPort(Integer.parseInt(mailconfig.getString("port")));
            email.setAuthenticator(new DefaultAuthenticator(
                    mailconfig.getString("username"),
                    mailconfig.getString("password")));
            email.setSSLOnConnect(new Boolean(mailconfig.getString("useSSL")));
            email.setDebug(new Boolean(mailconfig.getString("debug")));
            email.setFrom(
                    mailconfig.getString("username"),
                    mailconfig.getString("fromName"));
            email.setSubject("TestMail");
            email.setMsg("This is a test mail ... :-)");
            email.addTo("vasiliyeskin@yandex.ru");
            email.send();

            sentMailDao.insert(new SentMail(mailconfig.getString("username"), mailconfig.getString("username"), true));
        } catch (EmailException e) {
             e.printStackTrace();
        }

    }
}
