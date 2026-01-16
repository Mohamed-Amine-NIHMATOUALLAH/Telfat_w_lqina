package com.firstproject.telfat_w_lqina.models;

import com.firstproject.telfat_w_lqina.Enum.StatusComplaint;
import jakarta.persistence.*;

@Entity
@Table(name = "complaint")
public class Complaint {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY) // AJOUTER cascade
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "proof_id", nullable = false)
    private Proof proof;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY) // AJOUTER cascade
    @JoinColumn(name = "object_id", nullable = false)
    private BaseObject baseObject;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusComplaint status;

    // Constructeurs


    public Complaint() {
    }

    public Complaint(long id, Person person, Proof proof, BaseObject baseObject, StatusComplaint status) {
        this.id = id;
        this.person = person;
        this.proof = proof;
        this.baseObject = baseObject;
        this.status = status;
    }
    // Getters et Setters

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Proof getProof() {
        return proof;
    }

    public void setProof(Proof proof) {
        this.proof = proof;
    }

    public BaseObject getObject() {
        return baseObject;
    }

    public void setObject(BaseObject baseObject) {
        this.baseObject = baseObject;
    }

    public StatusComplaint getStatus() {
        return status;
    }

    public void setStatus(StatusComplaint status) {
        this.status = status;
    }
}
