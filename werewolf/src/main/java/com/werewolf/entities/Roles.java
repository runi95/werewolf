package com.werewolf.entities;

import javax.persistence.*;

@Entity
public class Roles {

    // Id to be used by JPA for the most part.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = true)
    private long id;

    // Name of the role
    @Column(name = "name", nullable = false)
    private String name;

    // The alignment the role is fighting for
    @Column(name = "alliance", nullable = false)
    private String alliance;

    // A short descriptive requirement to win the game
    @Column(name = "goal", nullable = false)
    private String goal;

    // Description of the role
    @Column(name = "description", nullable = false)
    private String description;

    public void setName(String name) {
        this.name = name;
    }

    public void setAlliance(String alliance) {
        this.alliance = alliance;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getAlliance() {
        return alliance;
    }

    public String getGoal() {
        return goal;
    }

    public String getDescription() {
        return description;
    }
}
