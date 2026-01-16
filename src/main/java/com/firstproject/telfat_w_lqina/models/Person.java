package com.firstproject.telfat_w_lqina.models;

import jakarta.persistence.*;

@Entity
@Table(name = "persons")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "firstName", nullable = false)
    private String firstName;

    @Column(name = "lastName", nullable = false)
    private String lastName;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY) // AJOUTER cascade
    @JoinColumn(name = "document_id", nullable = false)
    private IdentityDocument identityDocument;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "telephone", nullable = false)
    private String telephone;
    // Constructeurs

    public Person(Long id, String firstName, String lastName, IdentityDocument identityDocument, String email, String telephone) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.identityDocument = identityDocument;
        this.email = email;
        this.telephone = telephone;
    }

    public Person() {
    }

    // Getters et Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public IdentityDocument getIdentityDocument() {
        return identityDocument;
    }

    public void setIdentityDocument(IdentityDocument identityDocument) {
        this.identityDocument = identityDocument;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}

