package at.ac.tuwien.big.we16.ue3.controller;

import at.ac.tuwien.big.we16.ue3.model.ErrorBean;
import at.ac.tuwien.big.we16.ue3.model.User;
import at.ac.tuwien.big.we16.ue3.service.AuthService;
import at.ac.tuwien.big.we16.ue3.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class UserController {
    private final UserService userService;
    private final AuthService authService;
    private DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    public void getRegister(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (this.authService.isLoggedIn(request.getSession())) {
            response.sendRedirect("/");
            return;
        }
        request.setAttribute("errorBean", new ErrorBean());
        request.getRequestDispatcher("/views/registration.jsp").forward(request, response);
    }

    // TODO validation of user data
    public void postRegister(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String firstname = request.getParameter("firstname");
        String lastname = request.getParameter("lastname");
        String dateofBirth = request.getParameter("dateofbirth");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        ErrorBean error = null;
        if(firstname == null || firstname.isEmpty()) {
            if(error == null) {
                error = new ErrorBean();
            }
            error.setFirstnameError(" Vorname nicht vorhanden.");
        }
        if(lastname == null || lastname.isEmpty()) {
            if(error == null) {
                error = new ErrorBean();
            }
            error.setLastnameError(" Nachname nicht vorhanden.");
        }
        LocalDate date = null;
        try {
            date = LocalDate.parse(dateofBirth, format);
        } catch (DateTimeParseException parse) {
            if(error == null) {
                error = new ErrorBean();
            }
            error.setLastnameError(" Geburtsdatum muss die Form dd/mm/yyyy haben.");
        }
        if(date != null) {
            LocalDate now = LocalDate.now();
            Period p = Period.between(date, now);
            if(p.getYears() < 18) {
                if(error == null) {
                    error = new ErrorBean();
                }
                error.setDateError(" Du musst mindestens 18 Jahre alt sein.");
            }

        }
        if(email == null || email.isEmpty()) {
            if(error == null) {
                error = new ErrorBean();
            }
            error.setEmailError(" Email nicht vorhanden.");
        } else if(!email.matches("\\S+@\\S+\\.\\S+")) {
            if(error == null) {
                error = new ErrorBean();
            }
            error.setEmailError(" Emailformat falsch.");
        }

    if(password == null || password.length() < 4 || password.length() > 8 ) {
        if(error == null) {
            error = new ErrorBean();
        }
        error.setPasswordError(" Password muss zwischen 4 und 8 Zeichen lang sein.");
    }

        if(error == null) {
            User user = new User();
            this.userService.createUser(user);
            this.authService.login(request.getSession(), user);
            response.sendRedirect("/");
        } else {
            request.setAttribute("errorBean", error);
            request.getRequestDispatcher("/views/registration.jsp").forward(request, response);
        }
    }

}
