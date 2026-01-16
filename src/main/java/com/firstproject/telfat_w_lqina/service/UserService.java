package com.firstproject.telfat_w_lqina.service;

import com.firstproject.telfat_w_lqina.dao.UserDAO;
import com.firstproject.telfat_w_lqina.exception.creationexception.DuplicateEmailException;
import com.firstproject.telfat_w_lqina.exception.creationexception.DuplicateUsernameException;
import com.firstproject.telfat_w_lqina.exception.validationexception.InvalidEmailException;
import com.firstproject.telfat_w_lqina.exception.validationexception.InvalidPasswordException;
import com.firstproject.telfat_w_lqina.exception.validationexception.InvalidPhoneException;
import com.firstproject.telfat_w_lqina.models.Admin;
import com.firstproject.telfat_w_lqina.models.Agent;
import com.firstproject.telfat_w_lqina.models.User;
import com.firstproject.telfat_w_lqina.util.Alerts;
import com.firstproject.telfat_w_lqina.util.CheckSyntaxe;
import com.firstproject.telfat_w_lqina.util.Security;

import java.util.List;

public class UserService {
    UserDAO userDAO = new UserDAO();
    Alerts alerts = new Alerts();
    CheckSyntaxe checkSyntaxe = new CheckSyntaxe();
    Security security = new Security();

    public void createUser(User user){
        if (!checkSyntaxe.checkSyntaxeEmail(user.getEmail())){
            throw new InvalidEmailException("Error syntaxe email");
        }
        if (!checkSyntaxe.checkSyntaxeTelephone(user.getTelephone())){
            throw new InvalidPhoneException("Error syntaxe Telephone");
        }
        if (!checkSyntaxe.checkSyntaxePassword(user.getPassword())){
           throw new InvalidPasswordException("Error syntaxe Password");
        }
        if(userDAO.findByUsername(user.getUsername())!=null){
            throw new DuplicateUsernameException("Username already exists");
        }
        if (userDAO.emailExists(user.getEmail()) != 0){
            throw new DuplicateEmailException("Email already exists");
        }
        String passwd = user.getPassword();
        user.setPassword(security.hashPassword(passwd));
        if (userDAO.save(user)){
            alerts.successAlert("Success","User created","The user has been created successfully.");
            return;
        }
    }

    public void firstUserAdmin(){
        if (userDAO.nbUsers()==0){
            String passwd = security.hashPassword("Admin");
            Admin admin = new Admin("Admin","0000000000",passwd,"Admin@Admin.com");
            userDAO.save((User) admin);
            System.out.println("first User Admin add");
        }
    }

    public List<Agent> getAllUsers(){
        return userDAO.getAllUsers();
    }

    public void updateUser(User user) {
        userDAO.update(user);
    }
}
