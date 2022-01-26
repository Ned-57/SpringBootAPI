package com.qa.user_app.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.qa.user_app.data.entity.User;
import com.qa.user_app.service.UserService;
//import org.springframework.boot.test.context.SpringBootTest;

//@SpringBootTest //starts full application context (not needed if just testing web layer)
@WebMvcTest(UserController.class) // Starts application context with only beans required for controller layer
									// (specified)
public class UserControllerWebIntegrationTest {

	@Autowired // field injection
	private UserController controller;
	// controller depends on userservice
	// so we create mock object of userservice
	// using mockito
	@MockBean
	private UserService userService;

	// we need some data for our tests
	private List<User> users;
	private User validUser;
	private User userToCreate;
	private User gotUser;

	@BeforeEach // junit annotation to run method before each test
	public void init() {
		users = new ArrayList<>();
		users.addAll(List.of(new User(1, "Jasper", "Huxtable", 24), new User(2, "Ned", "Telfer", 25),
				new User(3, "Bob", "Dylan", 80)));
		validUser = new User(1, "Chucky", "Chooky", 69);
		userToCreate = new User(1, "Chucky", "Chooky", 69);
		gotUser = new User(3, "Bob", "Dylan", 80);

	}

	@Test // junit annotation for test
	public void getAllUsersTest() {
		ResponseEntity<List<User>> expected = new ResponseEntity<List<User>>(users, HttpStatus.OK);

		// when this action occurs
		when(userService.getAll()).thenReturn(users);

		// then assert this happens
		ResponseEntity<List<User>> actual = controller.getUsers();

		assertThat(expected).isEqualTo(actual);

		// verify the service was called by the controller
		verify(userService, times(1)).getAll();
	}

	@Test
	public void createUserTest() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Location", "/user/" + String.valueOf(validUser.getId()));
		ResponseEntity<User> expected = new ResponseEntity<User>(validUser, headers, HttpStatus.CREATED);

		when(userService.create(userToCreate)).thenReturn(validUser);

		ResponseEntity<User> actual = controller.createUser(userToCreate);

		assertThat(expected).isEqualTo(actual);

		verify(userService).create(userToCreate);

	}

	@Test
	public void getUserByIdTest() {
		// create expected response
		ResponseEntity<User> expected = ResponseEntity.status(HttpStatus.OK).body(gotUser);
		// when we pass id of 3 through getById we expect gotUser (user with id of 3)
		when(userService.getById(3)).thenReturn(gotUser);
		// create actual test, passing 3 through getUserById.
		ResponseEntity<User> actual = controller.getUserById(3);
		// check our expected response is the same as our actual test
		assertThat(expected).isEqualTo(actual);
		// verify mockito created userService instance properly?
		verify(userService).getById(3);
	}

	@Test
	public void updateUserTest() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Location", "/user/" + String.valueOf(validUser.getId()));
		ResponseEntity<User> expected = new ResponseEntity<User>(validUser, headers, HttpStatus.CREATED);

		when(userService.update(validUser, 1)).thenReturn(validUser);

		ResponseEntity<User> actual = controller.updateUser(1, validUser);

		assertThat(expected).isEqualTo(actual);

		verify(userService).update(validUser, 1);
	}

	@Test
	public void deleteUserTest() {
		ResponseEntity<?> expected = ResponseEntity.accepted().build();
		ResponseEntity<?> actual = controller.deleteUser(1);
		assertThat(expected).isEqualTo(actual);
		verify(userService).delete(1);

	}

}
