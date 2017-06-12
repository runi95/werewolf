package com.werewolf.services;

import com.werewolf.data.AccountRegisterForm;
import com.werewolf.data.AccountRepository;
import com.werewolf.data.UserEditForm;
import com.werewolf.data.UserRightRepository;
import com.werewolf.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

@Service
public class AccountServiceImpl implements AccountService {
	
	@Autowired
	AccountRepository accountRepository;
	
	@Autowired
    UserRightRepository userRightRepository;
	
	@Override
    public void create(AccountRegisterForm accountRegisterForm) {
        User user = new User();
        user.setUsername(accountRegisterForm.getUsername());
        user.setPasswordHash(new BCryptPasswordEncoder().encode(accountRegisterForm.getPassword()));
        user.setRights(new HashSet<>(userRightRepository.findAll()));
        accountRepository.save(user);
    }
	
	@Override
	public User findByUsername(String username) {
		return accountRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("A user with that username does not exist"));
	}
	
	@Override
    @Transactional
    public User findById(long id) {
        User user = accountRepository.findOne(id);
        if(user != null)
            return user;
        else {
            throw new IllegalArgumentException("A user with that id does not exist");
        }
    }
	
	@Override
	public void update(User user, UserEditForm userEditForm) {
		user.setUsername(userEditForm.getUsername());
		accountRepository.save(user);
	}
	
	@Override
	public UserEditForm getEditForm(User user) {
		UserEditForm userEditForm = new UserEditForm();
		userEditForm.setUsername(user.getUsername());
		return userEditForm;
	}
}
