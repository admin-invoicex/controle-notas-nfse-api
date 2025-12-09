package io.invoicextech.controlenotas.nfse.api.auth.infrastructure.persistence.adapter;

import io.invoicextech.controlenotas.nfse.api.auth.domain.model.RoleName;
import io.invoicextech.controlenotas.nfse.api.auth.domain.model.User;
import io.invoicextech.controlenotas.nfse.api.auth.domain.repository.UserRepository;
import io.invoicextech.controlenotas.nfse.api.auth.infrastructure.persistence.entity.RoleEntity;
import io.invoicextech.controlenotas.nfse.api.auth.infrastructure.persistence.entity.UserEntity;
import io.invoicextech.controlenotas.nfse.api.auth.infrastructure.persistence.mapper.UserMapper;
import io.invoicextech.controlenotas.nfse.api.auth.infrastructure.persistence.repository.RoleJpaRepository;
import io.invoicextech.controlenotas.nfse.api.auth.infrastructure.persistence.repository.UserJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class UserRepositoryAdapter implements UserRepository {
    private final UserJpaRepository userJpaRepository;
    private final RoleJpaRepository roleJpaRepository;

    public UserRepositoryAdapter(UserJpaRepository userJpaRepository, RoleJpaRepository roleJpaRepository) {
        this.userJpaRepository = userJpaRepository;
        this.roleJpaRepository = roleJpaRepository;
    }

    @Override
    public User save(User user) {
        Set<RoleEntity> roles = user.getRoles().stream()
                .map(RoleName::name)
                .map(name -> roleJpaRepository.findByName(name).orElseThrow())
                .collect(Collectors.toSet());
        UserEntity entity = UserMapper.toEntity(user, roles);
        UserEntity saved = userJpaRepository.save(entity);
        return UserMapper.toDomain(saved);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email).map(UserMapper::toDomain);
    }

    @Override
    public Optional<User> findByDocument(String document) {
        return userJpaRepository.findByDocument(document).map(UserMapper::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userJpaRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByDocument(String document) {
        return userJpaRepository.existsByDocument(document);
    }
}
