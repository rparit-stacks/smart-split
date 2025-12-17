package com.rps.smartsplit.config;

import com.rps.smartsplit.model.User;
import com.rps.smartsplit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    private CustomUserDetail customUserDetail;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       User user = userRepository.findByEmail(username)
               .orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + username));
       return new CustomUserDetail(user);
    }
}
