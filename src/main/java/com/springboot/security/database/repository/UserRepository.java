package com.springboot.security.database.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springboot.security.database.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>
{

	Optional<User> findByUsernameOrEmail(String username, String email);
	
	Optional<User> findByEmail(String email);
}