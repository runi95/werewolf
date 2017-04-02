package com.werewolf.data;

import org.hibernate.validator.constraints.NotEmpty;

public class UserEditForm {

	@NotEmpty
	private String username;
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
}
