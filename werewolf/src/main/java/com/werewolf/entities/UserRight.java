package com.werewolf.entities;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class UserRight {

    //The name of the right is unique and therefore we use it as the primary key
    @Id
    @Column(name = "name", nullable = false, updatable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "rights")
    private Set<User> users;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

}