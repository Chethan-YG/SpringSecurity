package com.springboot.security.database.Service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.springboot.security.database.model.User;
import com.springboot.security.database.repository.UserRepository;

@Service
public class CustomUserService implements UserDetailsService {
	
	 @Autowired
	  private UserRepository userRepository;

	    @Override
	    public UserDetails loadUserByUsername(String UsernameOrEmail) throws UsernameNotFoundException {
	        User user = userRepository.findByUsernameOrEmail(UsernameOrEmail, UsernameOrEmail)
	                .orElseThrow(() -> new UsernameNotFoundException("Username or email not found in database"));

	        return new CustomDetails(user);
    }



	
}
