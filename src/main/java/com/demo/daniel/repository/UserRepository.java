package com.demo.daniel.repository;

import com.demo.daniel.model.entity.Position;
import com.demo.daniel.model.entity.Role;
import com.demo.daniel.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Optional<User> findByUsername(String username);

    Optional<List<User>> findByRolesContaining(Role role);

    Optional<List<User>> findByPositionsContaining(Position position);
}
