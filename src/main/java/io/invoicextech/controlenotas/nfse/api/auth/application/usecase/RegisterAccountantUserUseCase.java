package io.invoicextech.controlenotas.nfse.api.auth.application.usecase;

import io.invoicextech.controlenotas.nfse.api.auth.application.dto.RegisterAccountantInput;
import io.invoicextech.controlenotas.nfse.api.auth.application.dto.UserOutput;
import io.invoicextech.controlenotas.nfse.api.auth.application.mapper.UserOutputMapper;
import io.invoicextech.controlenotas.nfse.api.auth.application.port.PasswordHasher;
import io.invoicextech.controlenotas.nfse.api.auth.domain.model.DocumentType;
import io.invoicextech.controlenotas.nfse.api.auth.domain.model.RoleName;
import io.invoicextech.controlenotas.nfse.api.auth.domain.model.User;
import io.invoicextech.controlenotas.nfse.api.auth.domain.repository.UserRepository;
import io.invoicextech.controlenotas.nfse.api.auth.domain.vo.Cpf;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RegisterAccountantUserUseCase {
    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    public RegisterAccountantUserUseCase(UserRepository userRepository, PasswordHasher passwordHasher) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
    }

    public UserOutput execute(RegisterAccountantInput input) {
        if (!input.password().equals(input.confirmPassword())) {
            throw new IllegalArgumentException("Senha e confirmação não conferem");
        }
        validatePasswordPolicy(input.password());

        String email = input.email().trim().toLowerCase();
        String cpf = Cpf.of(input.cpf()).value();

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email já cadastrado");
        }
        if (userRepository.existsByDocument(cpf)) {
            throw new IllegalArgumentException("Documento já cadastrado");
        }

        String hash = passwordHasher.hash(input.password());
        User user = User.newUser(input.name(), email, hash, cpf, DocumentType.CPF, Set.of(RoleName.ACCOUNTANT));
        User saved = userRepository.save(user);
        return UserOutputMapper.from(saved);
    }

    private void validatePasswordPolicy(String password) {
        if (password == null || password.length() < 8 || !password.matches(".*[A-Za-z].*") || !password.matches(".*\\d.*")) {
            throw new IllegalArgumentException("Senha deve ter ao menos 8 caracteres, letras e números");
        }
    }
}
