package com.qa.user_app.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qa.user_app.data.entity.User;
import com.qa.user_app.service.UserService;

@RestController // signifies controller (to be stored as a bean), "Rest" is a shortcut for
				// @ResponseBody
@RequestMapping(path = "/user") // access this controller at localhost:8080/user - specifies path for
								// @GetMapping
public class UserController {

	// UserController has-a JpaRepository
	// - How do we get this repository?
	// - To get the repository, we use dependency injection
	private UserService userService;

	@Autowired // indicates that the repository must be injected via dependency injection
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping // localhost:8080/user
	public ResponseEntity<List<User>> getUsers() {
		ResponseEntity<List<User>> users = ResponseEntity.ok(userService.getAll());
		return users;
	}

	// {id} is a path variable
	// we send requests to: localhost:8080/user/{id}
	// @RequestMapping(path = "/{id}", method = { RequestMethod.GET }) // from path,
	// we want id variable
	@GetMapping(path = "/{id}") // - shortcut for above
	public ResponseEntity<User> getUserById(@PathVariable("id") long id) {
		User savedUser = userService.getById(id);
		ResponseEntity<User> response = ResponseEntity.status(HttpStatus.OK).body(savedUser);
		return response;
	}

	// RequestMapping(method = { RequestMethod.POST })
	@PostMapping // accepts requests to: localhost:8080/user using POST
	public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
		// user object above is passed in the http request body using @RequestBody
		User savedUser = userService.create(user);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Location", "/user/" + String.valueOf(savedUser.getId()));

		// (body, httpHeaders, responseStatusCode)
		ResponseEntity<User> response = new ResponseEntity<User>(savedUser, headers, HttpStatus.CREATED);
		return response;
	}

	// UPDATE
	@PutMapping("/{id}") // localhost:8080/user/1
	public ResponseEntity<User> updateUser(@PathVariable("id") long id, @Valid @RequestBody User user) {
		User updatedUser = userService.update(user, id);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Location", "/user/" + String.valueOf(updatedUser.getId()));
		ResponseEntity<User> response = new ResponseEntity<User>(updatedUser, headers, HttpStatus.CREATED);
		return response;

	}

	// DELETE
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable("id") long id) {
		userService.delete(id);
		return ResponseEntity.accepted().build();

	}

}
