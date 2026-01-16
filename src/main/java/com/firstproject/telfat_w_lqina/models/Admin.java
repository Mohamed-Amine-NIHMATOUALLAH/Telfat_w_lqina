package com.firstproject.telfat_w_lqina.models;

import com.firstproject.telfat_w_lqina.Enum.UserType;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "admin")
@PrimaryKeyJoinColumn(name = "admin_id")
public class Admin extends User{

    public Admin() {
    super();
    super.setUserType(UserType.ADMIN);

    }

    public Admin( long id,String username, String telephone, String password, String email) {
        super( id,username, telephone, password, email);
        super.setUserType(UserType.ADMIN);

    }
    public Admin(String username, String telephone, String password,String email) {
        super(username, telephone, password, email);
        super.setUserType(UserType.ADMIN);

    }



}
