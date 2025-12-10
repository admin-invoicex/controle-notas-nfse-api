package io.invoicextech.controlenotas.nfse.api.auth.infrastructure.security;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import io.invoicextech.controlenotas.nfse.api.auth.domain.model.User;
import io.invoicextech.controlenotas.nfse.api.auth.domain.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String normalized = Optional.ofNullable(username).orElse("").trim().toLowerCase();
        Optional<User> byEmail = userRepository.findByEmail(normalized);
        User user =
                byEmail.orElseGet(
                        () ->
                                userRepository
                                        .findByDocument(normalized.replaceAll("\\D", ""))
                                        .orElse(null));
        if (user == null) throw new UsernameNotFoundException("Usuário não encontrado");
        return new UserPrincipal(user);
    }
}
