package com.demo.Blog.repository;

import com.demo.Blog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserName(String userName);
    Optional<User> findByEmail(String email);

    boolean existsUserByUserName(String userName);

    boolean existsUserByEmail(String email);
}
