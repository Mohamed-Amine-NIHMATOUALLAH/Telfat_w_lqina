package com.firstproject.telfat_w_lqina.util;

import com.firstproject.telfat_w_lqina.models.User;
import org.mindrot.jbcrypt.BCrypt;

public class Security {

    public String hashPassword(String password){
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public  boolean checkPassword(String passwordInput , User userFromDB){
        return BCrypt.checkpw(passwordInput , userFromDB.getPassword());
    }
}
