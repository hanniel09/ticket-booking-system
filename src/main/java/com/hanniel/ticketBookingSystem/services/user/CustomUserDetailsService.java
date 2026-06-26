package com.hanniel.ticketBookingSystem.services.user;

import com.hanniel.ticketBookingSystem.repositories.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Attempting to load user by email/username: {}", username);
        return userRepository.findByEmail(username)
                .orElseThrow(() -> {
                    log.error("User not found with email/username: {}", username);
                    return new UsernameNotFoundException("User not found with email: " + username);
                });
    }
}
