package com.qa.user_app.service;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qa.user_app.data.entity.User;
import com.qa.user_app.data.repository.UserRepository;

@Service // indicates a service bean/component - domain driven design
public class UserService {

	private UserRepository userRepository;

	@Autowired // dependency (constructor) injection
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public List<User> getAll() {
		// returns all users in message body
		return userRepository.findAll();
	}

	public User getById(long id) {
		if (userRepository.existsById(id)) {
			return userRepository.findById(id).get();
		}
		throw new EntityNotFoundException("User with id " + id + " not found");
	}

	public User create(User user) {
		User savedUser = userRepository.save(user);
		return savedUser;
	}

	public User update(User user, long id) {
		// 1. Check if the user exists
		if (userRepository.existsById(id)) {
			// 2. get user in db
			User userInDb = userRepository.getById(id);

			// 3. Update users fields
			userInDb.setAge(user.getAge());
			userInDb.setForename(user.getForename());
			userInDb.setSurname(user.getSurname());

			// 4. Save the updated user
			return userRepository.save(userInDb);
		} else {
			// else if the user doesn't exist, save them to the database
			return userRepository.save(user);
		}
	}

	public User delete(long id) {
		if (userRepository.existsById(id)) {
			User user = userRepository.getById(id);
			userRepository.deleteById(id);
			return new User(user.getForename(), user.getSurname(), user.getAge());
		}
		throw new EntityNotFoundException("User with id " + id + " not found");
	}
}
