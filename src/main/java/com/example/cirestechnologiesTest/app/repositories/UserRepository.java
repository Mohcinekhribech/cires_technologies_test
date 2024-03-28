package com.example.cirestechnologiesTest.app.repositories;

import com.example.cirestechnologiesTest.app.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsernameOrEmail(String username, String email);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
}