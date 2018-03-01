package ru.javaops.masterjava.service.mail;

import com.google.common.collect.ImmutableSet;
import com.sun.xml.ws.developer.JAXWSProperties;
import ru.javaops.web.WebStateException;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.soap.MTOMFeature;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class MailServiceClient {

    public static void main(String[] args) throws MalformedURLException, WebStateException {
        Service service = Service.create(
                new URL("http://localhost:8080/mail/mailService?wsdl"),
                new QName("http://mail.javaops.ru/", "MailServiceImplService"));

        MailService mailService = service.getPort(MailService.class);
        String state = mailService.sendToGroup(ImmutableSet.of(new Addressee("vasiliyeskin@yandex.ru", null)), null, "Group mail subject", "Group mail body");
        System.out.println("Group mail state: " + state);

        String stateAttarcedFile = mailService.sendToGroupWithAttach(ImmutableSet.of(new Addressee("vasiliyeskin@yandex.ru", null)), null, "Group mail subject", "Group mail body", "city.xml");
        System.out.println("Group mail with attached file state: " + stateAttarcedFile);


        DataHandler dh = new DataHandler(new
                FileDataSource(MailConfig.getTempDir() + "city.xml"));
        String stateAttarced = mailService.sendToGroupWithAttachment(
                ImmutableSet.of(new Addressee("vasiliyeskin@yandex.ru", null)),
                null,
                "Group mail subject",
                "Group mail body",
                "city.xml",
                dh);
        System.out.println("Group mail with attached file state: " + stateAttarced);

        GroupResult groupResult = mailService.sendBulk(ImmutableSet.of(
                new Addressee("vasiliyeskin@yandex.ru", null),
                new Addressee("Bad Email <bad_email.ru>")), "Bulk mail subject", "Bulk mail body");
        System.out.println("\nBulk mail groupResult:\n" + groupResult);
    }
}
