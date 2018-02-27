package ru.javaops.masterjava.service.mail;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.MalformedURLException;
import java.net.URL;

public class MailServiceClient {

    public static void main(String[] args) throws MalformedURLException {
        Service service = Service.create(
                new URL("http://localhost:8080/mail/mailService?wsdl"),
                new QName("http://mail.javaops.ru/", "MailServiceImplService"));

        MailService mailService = service.getPort(MailService.class);
        mailService.sendToGroup(ImmutableSet.of(new Addressee("vasiliyeskin@yandex.ru", "Vasiliy")), null, "Subject", "Body");
        mailService.sendToGroup(ImmutableSet.of(
                new Addressee("vasiliyeskin@yandex.ru", null),
                new Addressee("Bad Email <bad_email.ru>")), null, "Subject", "Body");
    }
}
