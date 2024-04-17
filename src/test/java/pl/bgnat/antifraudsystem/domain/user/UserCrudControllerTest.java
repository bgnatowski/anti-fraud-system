package pl.bgnat.antifraudsystem.domain.user;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import pl.bgnat.antifraudsystem.dto.UserDTO;
import pl.bgnat.antifraudsystem.dto.request.AddressRegisterRequest;
import pl.bgnat.antifraudsystem.dto.request.UserRegistrationRequest;
import pl.bgnat.antifraudsystem.dto.response.UserDeleteResponse;
import pl.bgnat.antifraudsystem.domain.enums.Role;

import java.util.ArrayList;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.bgnat.antifraudsystem.dto.response.UserDeleteResponse.DELETED_SUCCESSFULLY_RESPONSE;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserCrudControllerTest.class)
public class UserCrudControllerTest {
	private final String USER_LIST_URL = "/api/auth/list";
	private final String USER_URL = "/api/auth/user";

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserFacade userFacade;

	@SneakyThrows
	@Test
	@WithUserDetails
	public void testRegisterUser() {
		// Given
		UserRegistrationRequest registrationRequest =
				UserRegistrationRequest.builder()
						.firstName("John")
						.lastName("Doe")
						.email("johndoe@gmail.com")
						.username("johndoe")
						.password("password")
						.phoneNumber("123123123")
						.build();

		String jsonUser = new ObjectMapper().writeValueAsString(registrationRequest);
		UserDTO userDTO = UserDTO.builder()
				.id(1L)
				.firstName("John")
				.lastName("Doe")
				.username("johndoe")
				.email("johndoe@gmail.com")
				.role(Role.ADMINISTRATOR)
				.isActive(true)
				.build();

//		given(userFacade.registerUser(registrationRequest, confirmationCode)).willReturn(userDTO);

		// When Then
		mockMvc.perform(post(USER_URL)
						.contentType(MediaType.APPLICATION_JSON)
						.content(jsonUser)
						.with(csrf()))
				.andExpect(status().isCreated());
	}

	@SneakyThrows
	@Test
	@WithUserDetails
	public void testRegisterUserAddress() {
		// Given
		AddressRegisterRequest addressRegisterRequest = new AddressRegisterRequest(
				"Some street",
				"Apartment 123",
				"12345",
				"Cityville",
				"State",
				"Country");

		String jsonAddress = new ObjectMapper().writeValueAsString(addressRegisterRequest);

		UserDTO userDTO = UserDTO.builder()
				.id(1L)
				.firstName("John")
				.lastName("Doe")
				.username("johndoe")
				.email("johndoe@gmail.com")
				.role(Role.ADMINISTRATOR)
				.isActive(true)
				.build();

//		given(userFacade.addUserAddress("johndoe",addressRegisterRequest)).willReturn(userDTO);

		// When Then
		mockMvc.perform(patch(USER_URL +"/johndoe/address/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(jsonAddress)
						.with(csrf()))
				.andExpect(status().isCreated());
	}

	@SneakyThrows
	@Test
	@WithUserDetails
	public void getUserDetailsFullDTO() {
		// Given
		UserDTO userDTO = UserDTO.builder()
				.id(1L)
				.firstName("John")
				.lastName("Doe")
				.username("johndoe")
				.email("johndoe@gmail.com")
				.role(Role.ADMINISTRATOR)
				.isActive(true)
				.build();

		given(userFacade.getUserByUsername("johndoe")).willReturn(userDTO);

		// When Then
		mockMvc.perform(get(USER_URL +"/johndoe")
						.contentType(MediaType.APPLICATION_JSON)
						.with(csrf()))
				.andExpect(status().isOk());
	}
	@Test
	public void testGetAllRegisteredUsersAsAnonymous() throws Exception {
		// Given
		given(userFacade.getAllRegisteredUsers()).willReturn(new ArrayList<>());
		// When Then
		mockMvc.perform(get(USER_LIST_URL))
				.andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUser(authorities = "ADMINISTRATOR")
	public void testGetAllRegisteredUsersAsAdministrator() throws Exception {
		// Given
		given(userFacade.getAllRegisteredUsers()).willReturn(new ArrayList<>());
		// When Then
		mockMvc.perform(get(USER_LIST_URL))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(authorities = "SUPPORT")
	public void testGetAllRegisteredUsersAsSupport() throws Exception {
		// Given
		given(userFacade.getAllRegisteredUsers()).willReturn(new ArrayList<>());
		// When Then
		mockMvc.perform(get(USER_LIST_URL))
				.andExpect(status().isOk());
	}

	@Test
	@WithUserDetails
	@WithMockUser(authorities = "ADMINISTRATOR")
	void testDeleteUser() throws Exception {
		// Given
		String username = "johndoe";

		given(userFacade.deleteUserByUsername(username))
				.willReturn(new UserDeleteResponse(username, DELETED_SUCCESSFULLY_RESPONSE));

		// When Then
		mockMvc.perform(delete("/api/auth/user/{username}", username)
						.with(csrf()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.username", is(username)))
				.andExpect(jsonPath("$.status", is(DELETED_SUCCESSFULLY_RESPONSE)));
	}
}