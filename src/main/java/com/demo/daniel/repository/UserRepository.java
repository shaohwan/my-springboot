package com.demo.daniel.repository;

import com.demo.daniel.entity.Role;
import com.demo.daniel.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    List<User> findByRolesContaining(Role role);
}
