package com.firstproject.telfat_w_lqina.models;
import com.firstproject.telfat_w_lqina.Enum.UserType;
import jakarta.persistence.*;

@Entity
@Table(name = "user")
@Inheritance(strategy = InheritanceType.JOINED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "telephone")
    private String telephone;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", nullable = false)
    private UserType userType; // ADMIN, AGENT

    @Column(name = "first_login", nullable = false)
    private boolean firstLogin = true; // true par défaut pour les nouveaux agents

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public User(long id, String username, String telephone, String password, String email) {
        this.username = username;
        this.telephone = telephone;
        this.password = password;
        this.id = id;
        this.email = email;
    }
    public User(String username, String telephone, String password, String email) {
        this.username = username;
        this.telephone = telephone;
        this.password = password;
        this.email = email;
    }

    public User() {
    }


    //Setters
    public String getEmail() {
        return email;
    }

    public long getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getUsername() {
        return username;
    }

    //Setters
    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isFirstLogin() {
        return firstLogin;
    }

    public void setFirstLogin(boolean firstLogin) {
        this.firstLogin = firstLogin;
    }
}
