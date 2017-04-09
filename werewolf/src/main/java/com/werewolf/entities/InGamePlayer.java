package com.werewolf.entities;

import javax.persistence.*;

@Entity
public class InGamePlayer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nickname", nullable = false, updatable = true)
    private String nickname;

    @Column(name = "alive", nullable = false)
    private boolean alive;

    @OneToOne
    @JoinTable(
            name = "ingameplayer_role",
            joinColumns = @JoinColumn(name = "nickname", referencedColumnName = "nickname"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private Roles role;

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public void setRole(Roles role) {
        this.role = role;
    }

    public String getNickname() {
        return nickname;
    }

    public boolean getAlive() {
        return alive;
    }

    public Roles getRole() {
        return role;
    }
}
