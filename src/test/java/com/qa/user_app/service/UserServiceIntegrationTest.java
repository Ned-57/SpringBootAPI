package com.qa.user_app.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.qa.user_app.data.entity.User;
import com.qa.user_app.data.repository.UserRepository;

@SpringBootTest
@Transactional
public class UserServiceIntegrationTest {

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	private List<User> usersInDatabase;
	private long nextNewElementsId;
	private User gotUser;

	// initialise the database for each test as it is rolled back afterwards
	@BeforeEach
	public void init() {
		List<User> users = List.of(new User(1, "bob", "lee", 22), new User(2, "fred", "see", 25),
				new User(3, "sarah", "fee", 28));
		usersInDatabase = new ArrayList<>();
		usersInDatabase.addAll(userRepository.saveAll(users));
		int size = usersInDatabase.size();
		nextNewElementsId = usersInDatabase.get(size - 1).getId() + 1;
		gotUser = new User(1, "bob", "lee", 22);
	}

	@Test
	public void getAllUsersTest() {
		assertThat(usersInDatabase).isEqualTo(userService.getAll());
	}

	@Test
	public void createUserTest() {
		User userToSave = new User("Janet", "Carlisle", 32);
		User expectedUser = new User(nextNewElementsId, userToSave.getForename(), userToSave.getSurname(),
				userToSave.getAge());

		assertThat(expectedUser).isEqualTo(userService.create(userToSave));
	}

	@Test
	public void getUserByIdTest() {
		assertThat(gotUser).isEqualTo(userService.getById(1));
	}

	@Test
	public void updateUserTest() {
		User updatedUser = new User(2, "Ned", "Telfer", 24);
		assertThat(updatedUser).isEqualTo(userService.update(updatedUser, 2));
	}

	@Test
	public void deleteUserTest() {
		User user = usersInDatabase.get(1);
		User userWithoutId = new User(user.getForename(), user.getSurname(), user.getAge());

		assertThat(userService.delete(user.getId())).isEqualTo(userWithoutId);
	}
}
