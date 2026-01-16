package com.firstproject.telfat_w_lqina.models;

import com.firstproject.telfat_w_lqina.Enum.DocumentType;
import jakarta.persistence.*;

@Entity
@Table(name = "identity_documents")
public class IdentityDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocumentType type;

    @Column(nullable = false, unique = true, length = 20)
    private String documentNumber;

    // Constructors

    public IdentityDocument(Long id, DocumentType type, String documentNumber) {
        this.id = id;
        this.type = type;
        this.documentNumber = documentNumber;
    }

    public IdentityDocument() {
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DocumentType getType() {
        return type;
    }

    public void setType(DocumentType type) {
        this.type = type;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }
}
