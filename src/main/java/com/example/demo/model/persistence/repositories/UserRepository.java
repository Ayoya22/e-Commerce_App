package com.example.demo.model.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.persistence.appUser;

public interface UserRepository extends JpaRepository<appUser, Long> {
	appUser findByUsername(String username);
}
