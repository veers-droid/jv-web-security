package taxi.controller.authentication;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import taxi.exception.AuthenticationException;
import taxi.lib.Injector;
import taxi.model.Driver;
import taxi.service.AuthenticationService;

@WebServlet(urlPatterns = "/login")
public class LoginController extends HttpServlet {
    private AuthenticationService authenticationService;

    @Override
    public void init() throws ServletException {
        Injector injector = Injector.getInstance("taxi");
        authenticationService =
                (AuthenticationService) injector.getInstance(AuthenticationService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        try {
            Driver driver = authenticationService.login(login, password);
            req.getSession().setAttribute("driver_id", driver.getId());
            resp.sendRedirect("/index");
        } catch (AuthenticationException e) {
            req.setAttribute("errorMessage", e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
        }
    }

}