package com.qa.user_app.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qa.user_app.data.entity.User;

@Repository // this signifies its a bean
//User = type of entity stored,
//Long = type of User entity's id field
public interface UserRepository extends JpaRepository<User, Long> {
	// Repositories MUST be interfaces
	// Spring and hibernate will generate the implementations
}
