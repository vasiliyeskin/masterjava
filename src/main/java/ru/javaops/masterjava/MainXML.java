package ru.javaops.masterjava;

import com.google.common.io.Resources;
import ru.javaops.masterjava.xml.schema.*;
import ru.javaops.masterjava.xml.util.JaxbParser;
import ru.javaops.masterjava.xml.util.Schemas;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

public class MainXML {
    protected String xmlName;

    private static final JaxbParser JAXB_PARSER = new JaxbParser(ObjectFactory.class);

    static {
        JAXB_PARSER.setSchema(Schemas.ofClasspath("payload.xsd"));
    }

    public MainXML(String xmlName) {
        this.xmlName = xmlName;
    }

    public static void main(String[] args) {
        MainXML mainXML = new MainXML( "payload.xml");

        try {
            mainXML.getUserOfProject("topjava")
            .forEach(x-> System.out.println(((UserType)x.getUser()).getFullName()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<GroupUser> getUserOfProject(String projectName) throws Exception
    {

        Payload payload = JAXB_PARSER.unmarshal(
                Resources.getResource(this.xmlName).openStream());

        Project project = payload.getProjects().getProject()
                .stream()
                .filter(x->{return x.getName().equals(projectName);})
                .findAny()
                .orElse(new Project());

        List<GroupUser> listUsers = new ArrayList<>();

        project.getGroups().getGroup()
                .forEach(x-> x.getUsers().getGroupUser()
                        .forEach(user->listUsers.add(user)));

        Collections.sort(listUsers, new Comparator<GroupUser>() {
            @Override
            public int compare(GroupUser o1, GroupUser o2) {
                return ((UserType)o1.getUser()).getFullName()
                        .compareTo(((UserType)o2.getUser()).getFullName());
            }
        });

        return listUsers;
    }


}
