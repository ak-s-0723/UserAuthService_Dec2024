package org.example.userauthenticationservice_dec2025.repos;

import org.example.userauthenticationservice_dec2025.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User,Long> {
    User save(User user);

    Optional<User> findByEmail(String email);
}
