package com.werewolf.services;

import com.werewolf.data.AccountRegisterForm;
import com.werewolf.data.UserEditForm;
import com.werewolf.entities.User;

public interface AccountService {
	
	void create(AccountRegisterForm accountRegisterForm);

	User findByUsername(String username);
	User findById(long id);
	
	void update(User user, UserEditForm userEditForm);
	
	/**
     * Get an edit form DTO for the given user
     * @param user the user to generate edit form from
     * @return editform for the given user
     */
    UserEditForm getEditForm(User user);
	
}
