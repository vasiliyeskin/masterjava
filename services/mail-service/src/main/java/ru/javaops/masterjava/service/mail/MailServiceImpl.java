package ru.javaops.masterjava.service.mail;

import com.sun.xml.ws.developer.StreamingAttachment;
import ru.javaops.web.WebStateException;

import javax.activation.DataHandler;
import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.soap.MTOM;
import java.util.Set;

@StreamingAttachment(parseEagerly=true, memoryThreshold=40000L)
@MTOM
@WebService(endpointInterface = "ru.javaops.masterjava.service.mail.MailService", targetNamespace = "http://mail.javaops.ru/"
//          , wsdlLocation = "WEB-INF/wsdl/mailService.wsdl"
)

public class MailServiceImpl implements MailService {
    public String sendToGroup(Set<Addressee> to, Set<Addressee> cc, String subject, String body) throws WebStateException {
        return MailSender.sendToGroup(to, cc, subject, body);
    }

    @Override
    public String sendToGroupWithAttach(Set<Addressee> to, Set<Addressee> cc, String subject, String body, String attachedFile) throws WebStateException {
        return MailSender.sendToGroupWithAttach(to, cc, subject, body, attachedFile);
    }

    @Override
    public String sendToGroupWithAttachment(Set<Addressee> to, Set<Addressee> cc, String subject, String body, String fileName, DataHandler data) throws WebStateException {
        return MailSender.sendToGroupWithAttachment(to, cc, subject, body, fileName, data);
    }

    @Override
    public GroupResult sendBulk(Set<Addressee> to, String subject, String body) throws WebStateException {
        return MailServiceExecutor.sendBulk(to, subject, body);
    }
}