package com.werewolf.services;

import java.util.HashSet;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.werewolf.data.AccountRepository;
import com.werewolf.data.LoggedInUser;
import com.werewolf.entities.User;

@Service
public class AccountDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
    AccountRepository accountRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = accountRepository.findByUsername(s).orElseThrow(() -> new UsernameNotFoundException(String.format("User with username=%s was not found",s)));

        HashSet<GrantedAuthority> grantedAuthoritySet = user.getRights().stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toCollection(HashSet::new));
        
        return new LoggedInUser(user, grantedAuthoritySet);
    }
}
