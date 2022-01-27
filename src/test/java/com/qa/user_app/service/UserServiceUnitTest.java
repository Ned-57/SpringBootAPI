package com.qa.user_app.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.qa.user_app.data.entity.User;
import com.qa.user_app.data.repository.UserRepository;

@ExtendWith(MockitoExtension.class) // JUnit Test runner, for mocks?
public class UserServiceUnitTest {

	@Mock
	private UserRepository userRepository;

	@InjectMocks // equivalent to @Autowired
	private UserService userService;

	private List<User> users;
	private User expectedUserWithId;
	private User expectedUserWithoutId;
	private long userId;

	@BeforeEach // junit5 (jupiter) annotation to run this method before every test
	public void init() {
		users = new ArrayList<>();
		users.addAll(List.of(new User(1, "bob", "lee", 22), new User(2, "fred", "see", 25),
				new User(3, "sarah", "fee", 28)));
		expectedUserWithoutId = new User("bob", "lee", 22);
		expectedUserWithId = new User(1, "bob", "lee", 22);
		userId = 1;
	}

	@Test
	public void getAllUsersTest() {
		when(userRepository.findAll()).thenReturn(users);
		assertThat(userService.getAll()).isEqualTo(users);
		verify(userRepository).findAll();
	}

	@Test
	public void createUserTest() {
		when(userRepository.save(expectedUserWithoutId)).thenReturn(expectedUserWithId);

		assertThat(userService.create(expectedUserWithoutId)).isEqualTo(expectedUserWithId);
		verify(userRepository).save(expectedUserWithoutId);
	}

	@Test
	public void getUserByIdTest() {
		when(userRepository.existsById(userId)).thenReturn(true);
		// telling mock userRepository what to return when userId passed
		when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUserWithId));

		assertThat(userService.getById(userId)).isEqualTo(expectedUserWithId);
		verify(userRepository).findById(userId);
		verify(userRepository).existsById(userId);
	}

	@Test
	public void updateUserTest() {
		when(userRepository.existsById(userId)).thenReturn(true);
		when(userRepository.getById(userId)).thenReturn(expectedUserWithId);
		when(userRepository.save(expectedUserWithId)).thenReturn(expectedUserWithId);

		assertThat(userService.update(expectedUserWithoutId, userId)).isEqualTo(expectedUserWithId);

		verify(userRepository).existsById(userId);
		verify(userRepository).getById(userId);
		verify(userRepository).save(expectedUserWithId);
	}

	@Test
	public void deleteUserTest() {
		when(userRepository.existsById(userId)).thenReturn(true);
		when(userRepository.getById(userId)).thenReturn(expectedUserWithId);

		assertThat(userService.delete(userId)).isEqualTo(expectedUserWithoutId);

		verify(userRepository).existsById(userId);
		verify(userRepository).getById(userId);

//		boolean expected = true;
//		boolean actual = userRepository.existsById(userId);
//		assertThat(expected).isEqualTo(actual);
//		verify(userService).delete(1);
	}

}
