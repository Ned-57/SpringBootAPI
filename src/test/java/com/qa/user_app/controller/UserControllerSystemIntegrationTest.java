package com.qa.user_app.controller;

import static org.assertj.core.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qa.user_app.data.entity.User;
import com.qa.user_app.data.repository.UserRepository;

//test environment should be close to real environment as poss in system tests
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional // after every test, state rolled back - allows us to insert test data
public class UserControllerSystemIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private UserRepository userRepository;

	private List<User> usersInDb;
	private long nextNewElementsId;
	private User gotUser;

	// initialise the db for our tests
	@BeforeEach
	public void init() {
		List<User> users = List.of(new User(1, "Jasper", "Huxtable", 24), new User(2, "Ned", "Telfer", 25),
				new User(3, "Bob", "Dylan", 80));
		usersInDb = new ArrayList<>();
		usersInDb.addAll(userRepository.saveAll(users));
		int size = usersInDb.size();
		nextNewElementsId = usersInDb.get(size - 1).getId() + 1;
		gotUser = new User(1, "Jasper", "Huxtable", 24);
	}

	@Test
	public void getAllUsersTest() throws Exception {
		// Create a mock http request builder
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.GET, "/user");

		// Specify the Accept header for the expected returned content type (MediaType)
		mockRequest.accept(MediaType.APPLICATION_JSON); // Accept: application/json

		// Create expected JSON String from usersInDatabase using the ObjectMapper
		// instance
		String users = objectMapper.writeValueAsString(usersInDb);

		// Setup ResultMatchers
		// - used for comparing the result of the mockRequest with our own specified
		// values
		ResultMatcher statusMatcher = MockMvcResultMatchers.status().isOk();
		ResultMatcher contentMatcher = MockMvcResultMatchers.content().json(users);

		// Send the request and assert the results where as expected
		mockMvc.perform(mockRequest).andExpect(statusMatcher).andExpect(contentMatcher);
	}

	@Test
	public void createUserTest() throws Exception {
		User userToSave = new User("Malcolm", "Telfer", 62);
		User expectedUser = new User(nextNewElementsId, userToSave.getForename(), userToSave.getSurname(),
				userToSave.getAge());

		// Configure mock request
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.POST, "/user");

		mockRequest.contentType(MediaType.APPLICATION_JSON); // Content-Type: application/json (type of the data in the
																// body of the request)
		mockRequest.content(objectMapper.writeValueAsString(userToSave)); // set the body of the request to a JSON
																			// string
		// .content() adds { "forename": "Janet", "surname": "Carlisle", "age": 32 } to
		// the body
		mockRequest.accept(MediaType.APPLICATION_JSON);

		// Configure ResultMatchers
		ResultMatcher statusMatcher = MockMvcResultMatchers.status().isCreated();
		ResultMatcher contentMatcher = MockMvcResultMatchers.content()
				.json(objectMapper.writeValueAsString(expectedUser));

		// Send the request and assert the results where as expected
		mockMvc.perform(mockRequest).andExpect(statusMatcher).andExpect(contentMatcher);
	}

	@Test
	public void getUserByIdTest() throws Exception {
		// mock http request
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.GET, "/user/1");
		// set content type of header for request
		mockRequest.contentType(MediaType.APPLICATION_JSON);
		// create expected json string from user by id
		String user = objectMapper.writeValueAsString(gotUser);
		// result matchers (comparing mockRequest JSON with gotUser JSON)
		ResultMatcher statusMatcher = MockMvcResultMatchers.status().isOk();
		ResultMatcher contentMatcher = MockMvcResultMatchers.content().json(user);

		mockMvc.perform(mockRequest).andExpect(statusMatcher).andExpect(contentMatcher);

	}

	@Test
	public void updateUserTest() throws Exception {
		User userToUpdate = new User(1, "Leeroy", "Jenkins", 43);
		User expectedUser = new User(1, userToUpdate.getForename(), userToUpdate.getSurname(), userToUpdate.getAge());

		// mock request
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.PUT, "/user/1");

		mockRequest.contentType(MediaType.APPLICATION_JSON);
		mockRequest.content(objectMapper.writeValueAsString(userToUpdate));
		mockRequest.accept(MediaType.APPLICATION_JSON);

		ResultMatcher statusMatcher = MockMvcResultMatchers.status().isCreated();
		ResultMatcher contentMatcher = MockMvcResultMatchers.content()
				.json(objectMapper.writeValueAsString(expectedUser));

		mockMvc.perform(mockRequest).andExpect(statusMatcher).andExpect(contentMatcher);

	}

	@Test
	public void deleteUserTest() {
		// TODO: Implement me
		fail("Implement me");
	}
}
