package com.werewolf.components;

import com.werewolf.data.AccountRepository;
import com.werewolf.data.NameDictionaryEnum;
import com.werewolf.data.NameDictionaryRepository;
import com.werewolf.entities.NameDictionary;
import com.werewolf.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DevUserCreator implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    AccountRepository userRepository;
    
    @Autowired
    NameDictionaryRepository nameDictionaryRepository;
    
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if(!userRepository.findByUsername("admin").isPresent()) {
            User user = new User();
            user.setUsername("admin");
            user.setPasswordHash(new BCryptPasswordEncoder().encode("password"));
            userRepository.save(user);
        }
        
        if(!(nameDictionaryRepository.count() > 0)) {
        	NameDictionaryEnum[] enumValues = NameDictionaryEnum.values();
        	for(int i = 0; i < enumValues.length; i++) {
        		NameDictionary nameDictionary = new NameDictionary();
        		nameDictionary.setName(enumValues[i].toString());
        		nameDictionaryRepository.save(nameDictionary);
        	}
        }
    }
}
