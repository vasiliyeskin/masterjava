package ru.javaops.masterjava.view;

import org.thymeleaf.context.WebContext;
import ru.javaops.masterjava.persist.dao.UserDao;
import ru.javaops.masterjava.persist.dao.UserDaoUpload;
import ru.javaops.masterjava.persist.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ru.javaops.masterjava.common.web.ThymeleafListener.engine;

@WebServlet(urlPatterns = "/", loadOnStartup = 1)
public class UserViewServlet extends HttpServlet
{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        WebContext webContext = new WebContext(req,resp,req.getServletContext(), req.getLocale());

        UserDaoUpload userDaoUpload = new UserDaoUpload(UserDao.class);
        webContext.setVariable("users", userDaoUpload.getDao().getWithLimit(20));

        engine.process("first20Users", webContext, resp.getWriter());
    }
}