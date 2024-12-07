package com.springboot.security.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springboot.security.database.model.Role;

@Repository
public interface RoleReposirtory extends JpaRepository<Role, Integer>
{
	
}
