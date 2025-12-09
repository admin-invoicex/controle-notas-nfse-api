package io.invoicextech.controlenotas.nfse.api.auth.domain.model;

import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Aggregate root for User. Pure domain model, no framework dependencies.
 */
public class User {
    private Long id;
    private String name;
    private String email; // unique
    private String passwordHash; // hashed
    private String document; // normalized digits only
    private DocumentType documentType;
    private final Set<RoleName> roles = new HashSet<>();
    private boolean active = true;
    private Instant createdAt;
    private Instant updatedAt;

    private User() {}

    public static User newUser(String name,
                               String email,
                               String passwordHash,
                               String document,
                               DocumentType documentType,
                               Set<RoleName> roles) {
        User u = new User();
        u.name = Objects.requireNonNull(name, "name").trim();
        u.email = Objects.requireNonNull(email, "email").trim().toLowerCase();
        u.passwordHash = Objects.requireNonNull(passwordHash, "passwordHash");
        u.document = Objects.requireNonNull(document, "document");
        u.documentType = Objects.requireNonNull(documentType, "documentType");
        if (roles != null) u.roles.addAll(roles);
        u.active = true;
        u.createdAt = Instant.now();
        u.updatedAt = u.createdAt;
        return u;
    }

    public void addRole(RoleName role) { this.roles.add(role); }
    public void removeRole(RoleName role) { this.roles.remove(role); }
    public boolean hasRole(RoleName role) { return this.roles.contains(role); }

    // Getters and setters for persistence mapping in infra
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public String getDocument() { return document; }
    public void setDocument(String document) { this.document = document; }
    public DocumentType getDocumentType() { return documentType; }
    public void setDocumentType(DocumentType documentType) { this.documentType = documentType; }
    public Set<RoleName> getRoles() { return Collections.unmodifiableSet(roles); }
    public void setRoles(Set<RoleName> newRoles) {
        this.roles.clear();
        if (newRoles != null) this.roles.addAll(newRoles);
    }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
