package com.visa.shopnow.userservice.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Represents a User entity in the database.
 * This class also implements Spring Security's UserDetails interface
 * to integrate with Spring Security for authentication and authorization.
 */
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
})
@Data // Lombok: getters , setters and more
@NoArgsConstructor
@AllArgsConstructor
@Builder // Lombok:builder pattern for object creation
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Username cannot be empty")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    @Column(nullable = false, unique = true)
    private String username;

    @NotBlank(message = "Name cannot be empty")
    @Size(min = 2, max = 30, message = "Name must be between 2 and 30 characters")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email should be valid")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Password cannot be empty")
    // stored as a hashed value
    // Length validation applies to the hashed password
    @Column(nullable = false)
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    @Builder.Default // Lombok: Ensures default value is used when using builder if not explicitly set
    private Set<String> roles = new HashSet<>(); //  "ROLE_CUSTOMER", "ROLE_SELLER", "ROLE_ADMIN"

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Return the authorities granted to the user.
     * This method maps the string roles to Spring Security's GrantedAuthority objects.
     * @return A collection of GrantedAuthority objects.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    /**
     * Indicates whether the user's account has expired.
     * @return true if the user's account is valid (i.e., non-expired), false otherwise.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true; // accounts never expire
    }

    /**
     * Indicates whether the user is locked or unlocked.
     * @return true if the user is not locked, false otherwise.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true; // accounts are never locked
    }

    /**
     * Indicates whether the user's credentials (password) have expired.
     * @return true if the user's credentials are valid (i.e., non-expired), false otherwise.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true; // For simplicity, assume credentials do not expire
    }

    /**
     * Indicates whether the user is enabled or disabled.
     * @return true if the user is enabled, false otherwise.
     */
    @Override
    public boolean isEnabled() {
        return true; // For simplicity, assume users are enabled by default
    }

    // --- JPA Lifecycle Callbacks ---

    /**
     * Sets the createdAt timestamp before the entity is persisted.
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Sets the updatedAt timestamp before the entity is updated.
     */
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
