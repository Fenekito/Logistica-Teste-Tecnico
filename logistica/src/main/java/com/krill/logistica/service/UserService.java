package com.krill.logistica.service;

import com.krill.logistica.model.Role;
import com.krill.logistica.model.User;
import com.krill.logistica.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository repo;
    private final PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repo.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
    }

    public Optional<User> byEmail(String email) { return repo.findByEmail(email); }

    public User saveAdminIfAbsent(String nome, String email, String rawPassword) {
        return repo.findByEmail(email).orElseGet(() ->
                repo.save(User.builder()
                        .nome(nome)
                        .email(email)
                        .password(encoder.encode(rawPassword))
                        .role(Role.ROLE_ADMIN)
                        .build())
        );
    }

    public User saveUserIfAbsent(String nome, String email, String rawPassword) {
        return repo.findByEmail(email).orElseGet(() ->
                repo.save(User.builder()
                        .nome(nome)
                        .email(email)
                        .password(encoder.encode(rawPassword))
                        .role(Role.ROLE_USER)
                        .build())
        );
    }
}
