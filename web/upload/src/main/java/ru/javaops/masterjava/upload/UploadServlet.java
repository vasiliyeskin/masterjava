package ru.javaops.masterjava.upload;

import org.thymeleaf.context.WebContext;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.dao.AbstractDao;
import ru.javaops.masterjava.persist.dao.UserDao;
import ru.javaops.masterjava.persist.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.sql.DriverManager;
import java.util.List;

import static ru.javaops.masterjava.common.web.ThymeleafListener.engine;

@WebServlet(urlPatterns = "/", loadOnStartup = 1)
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 10) //10 MB in memory limit
public class UploadServlet extends HttpServlet {

    private final UserProcessor userProcessor = new UserProcessor();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final WebContext webContext = new WebContext(req, resp, req.getServletContext(), req.getLocale());
        engine.process("upload", webContext, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final WebContext webContext = new WebContext(req, resp, req.getServletContext(), req.getLocale());

        try {
//            http://docs.oracle.com/javaee/6/tutorial/doc/glraq.html
            Part filePart = req.getPart("fileToUpload");
            if (filePart.getSize() == 0) {
                throw new IllegalStateException("Upload file have not been selected");
            }
            try (InputStream is = filePart.getInputStream()) {
                List<User> users = userProcessor.process(is);
                webContext.setVariable("users", users);
                engine.process("result", webContext, resp.getWriter());
            }
        } catch (Exception e) {
            webContext.setVariable("exception", e);
            engine.process("exception", webContext, resp.getWriter());
        }


        DBIProvider.init(() -> {
            try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException("PostgreSQL driver not found", e);
            }
            return DriverManager.getConnection("jdbc:postgresql://localhost:5432/masterjava", "user", "password");
        });

 /*       <DAO extends AbstractDao> dao = DBIProvider.getDao(UserDao.class);

        DBI dbi = new DBI("jdbc:h2:mem:test");
        Handle h = dbi.open();

        BatchExample b = h.attach(BatchExample.class);
        b.createSomethingTable();

        List<Integer> ids = asList(1, 2, 3, 4, 5);
        Iterator<String> first_names = asList("Tip", "Jane", "Brian", "Keith", "Eric").iterator();

        b.insertFamily(ids, first_names, "McCallister");

        assertThat(b.findNameById(1), equalTo("Tip McCallister"));
        assertThat(b.findNameById(2), equalTo("Jane McCallister"));
        assertThat(b.findNameById(3), equalTo("Brian McCallister"));
        assertThat(b.findNameById(4), equalTo("Keith McCallister"));
        assertThat(b.findNameById(5), equalTo("Eric McCallister"));

        h.close();*/
    }


}
