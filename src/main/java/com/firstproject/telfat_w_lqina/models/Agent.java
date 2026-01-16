package com.firstproject.telfat_w_lqina.models;
import com.firstproject.telfat_w_lqina.Enum.UserType;
import jakarta.persistence.*;

@Entity
@Table(name = "agent")
@PrimaryKeyJoinColumn(name = "agent_id")
public class Agent extends User{

    @ManyToOne
    @JoinColumn(name = "agent_stadium_id", nullable = true)
    private Stadium stadium;

    public Agent() {
        super.setUserType(UserType.AGENT);
    }

    public Agent(long id, String username, String telephone, String password, String email) {
        super( id,username, telephone, password, email);
        super.setUserType(UserType.AGENT);
    }
    public Agent( String username, String telephone, String password, String email) {
        super(username, telephone, password, email);
        super.setUserType(UserType.AGENT);
    }

    // Getters et Setters pour Stadium
    public Stadium getStadium() {
        return stadium;
    }

    public void setStadium(Stadium stadium) {
        this.stadium = stadium;
    }
}
