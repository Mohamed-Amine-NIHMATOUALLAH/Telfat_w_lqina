package com.firstproject.telfat_w_lqina.models;

import com.firstproject.telfat_w_lqina.Enum.PresenceProofType;
import jakarta.persistence.*;

@Entity
@Table(name = "proof")
public class Proof {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "proof_type", nullable = false)
    private PresenceProofType presenceProofType;

    @Lob
    @Column(name = "proof_image", nullable = false , columnDefinition = "LONGBLOB")
    private byte[] proofImage;

    // Constructeurs
    public Proof() {}

    public Proof(PresenceProofType presenceProofType, byte[] proofImage) {
        this.presenceProofType = presenceProofType;
        this.proofImage = proofImage;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PresenceProofType getPresenceProofType() {
        return presenceProofType;
    }

    public void setPresenceProofType(PresenceProofType presenceProofType) {
        this.presenceProofType = presenceProofType;
    }

    public byte[] getProofImage() {
        return proofImage;
    }

    public void setProofImage(byte[] proofImage) {
        this.proofImage = proofImage;
    }
}
