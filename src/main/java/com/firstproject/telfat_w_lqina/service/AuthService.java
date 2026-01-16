package com.firstproject.telfat_w_lqina.service;

import com.firstproject.telfat_w_lqina.dao.UserDAO;
import com.firstproject.telfat_w_lqina.exception.validationexception.IncorrectPasswordException;
import com.firstproject.telfat_w_lqina.exception.validationexception.InvalidInputLoginException;
import com.firstproject.telfat_w_lqina.exception.validationexception.InvalidUsernameException;
import com.firstproject.telfat_w_lqina.models.User;
import com.firstproject.telfat_w_lqina.util.Alerts;
import com.firstproject.telfat_w_lqina.util.Security;

public class AuthService {
    UserDAO userDAO = new UserDAO();
    Alerts alerts = new Alerts();
    Security security = new Security();

    public boolean login (String userName , String passewd){
        userName = userName.trim();
        boolean isVlideLogin = false;
        if (userName.isEmpty() || passewd.isEmpty()){
            throw new InvalidInputLoginException("Please enter both username and password !");
        }
        User user = userDAO.findByUsername(userName);
        if (user != null && security.checkPassword(passewd , user)){
            isVlideLogin = true;
        }
        if (user == null){
            throw new InvalidUsernameException("The username you entered does not exist !");
        }
        if (user != null && !(security.checkPassword(passewd , user))) {
            throw new IncorrectPasswordException("Incorrect Password");
        }
        return isVlideLogin;
    }



    public User commonUser(String userName){
        userName = userName.trim();
        User user = userDAO.findByUsername(userName);
        return user;
    }

}
