package com.werewolf.data;

import java.util.HashSet;

import org.springframework.security.core.GrantedAuthority;

import com.werewolf.entities.User;

public class LoggedInUser extends org.springframework.security.core.userdetails.User{
	
	private static final long serialVersionUID = 4844420936118662103L;
	long id;
    String name;

    public LoggedInUser(User user, HashSet<GrantedAuthority> grantedAuthorityHashSet) {
        super(user.getUsername(), user.getPasswordHash(), true, true, true, true, grantedAuthorityHashSet);
        this.name = user.getUsername();
        this.id = user.getId();
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
