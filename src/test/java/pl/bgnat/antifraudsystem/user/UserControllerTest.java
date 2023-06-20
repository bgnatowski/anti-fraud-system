package pl.bgnat.antifraudsystem.user;


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
import pl.bgnat.antifraudsystem.user.dto.*;
import pl.bgnat.antifraudsystem.user.enums.Role;

import java.util.ArrayList;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.bgnat.antifraudsystem.user.UserService.DELETED_SUCCESSFULLY_RESPONSE;
@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {
	private final String userListApi = "/api/auth/list";
	private final String userApi = "/api/auth/user";
	private final String lockApi = "/api/auth/access";
	private final String roleApi = "/api/auth/role";

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;

	@MockBean
	private CreditCardService creditCardService;
	@MockBean
	private AccountService accountService;

	@SneakyThrows
	@Test
	@WithUserDetails
	public void testRegisterUser() {
		// Given
		UserRegistrationRequest registrationRequest =
				new UserRegistrationRequest(
				"John", "Doe", "johndoe@gmail.com", "johndoe", "password");
		String jsonUser = new ObjectMapper().writeValueAsString(registrationRequest);
		UserDTO userDTO = new UserDTO(1L,
				"John",
				"Doe",
				"johndoe",
				"johndoe@gmail.com",
				Role.ADMINISTRATOR,
				true,
				AddressDTO.emptyAddress(),
				PhoneNumberDTO.emptyPhone());
		given(userService.registerUser(registrationRequest)).willReturn(userDTO);

		// When Then
		mockMvc.perform(post(userApi)
						.contentType(MediaType.APPLICATION_JSON)
						.content(jsonUser)
						.with(csrf()))
				.andExpect(status().isCreated());
	}

	@SneakyThrows
	@Test
	@WithUserDetails
	public void testRegisterUserPhone() {
		// Given
		PhoneNumberRegisterRequest phoneNumberRegisterRequest = new PhoneNumberRegisterRequest("123123123");
		String jsonPhone = new ObjectMapper().writeValueAsString(phoneNumberRegisterRequest);

		UserDTO userDTO = new UserDTO(1L,
				"John",
				"Doe",
				"johndoe",
				"johndoe@gmail.com",
				Role.ADMINISTRATOR,
				true,
				AddressDTO.emptyAddress(),
				new PhoneNumberDTO("+48123123123"));
		given(userService.addUserPhone("johndoe",phoneNumberRegisterRequest)).willReturn(userDTO);

		// When Then
		mockMvc.perform(patch(userApi+"/johndoe/phone/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(jsonPhone)
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

		UserDTO userDTO = new UserDTO(1L,
				"John",
				"Doe",
				"johndoe",
				"johndoe@gmail.com",
				Role.ADMINISTRATOR,
				true,
				new AddressDTO("Some street",
						"Apartment 123",
						"12345",
						"Cityville",
						"State",
						"Country"),
				new PhoneNumberDTO("+48123123123"));
		given(userService.addUserAddress("johndoe",addressRegisterRequest)).willReturn(userDTO);

		// When Then
		mockMvc.perform(patch(userApi+"/johndoe/address/register")
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
		UserDTO userDTO = new UserDTO(1L,
				"John",
				"Doe",
				"johndoe",
				"johndoe@gmail.com",
				Role.ADMINISTRATOR,
				true,
				new AddressDTO("Some street",
						"Apartment 123",
						"12345",
						"Cityville",
						"State",
						"Country"),
				new PhoneNumberDTO("+48123123123"));

		given(userService.getUserByUsername("johndoe")).willReturn(userDTO);

		// When Then
		mockMvc.perform(get(userApi+"/johndoe")
						.contentType(MediaType.APPLICATION_JSON)
						.with(csrf()))
				.andExpect(status().isOk());
	}



	@SneakyThrows
	@Test
	@WithUserDetails
	@WithMockUser(authorities = "ADMINISTRATOR")
	public void testChangeRole() {
		// Given
		long id = 2;
		String username = "johndoe";
		String email = "johndoe@gmail.com";
		String fistName = "John";
		String lastName = "Doe";
		Role role = Role.SUPPORT;

		UserUpdateRoleRequest roleUpdateRequest = new UserUpdateRoleRequest(
				username, "SUPPORT");

		String json = new ObjectMapper().writeValueAsString(roleUpdateRequest);
		UserDTO userDTO = new UserDTO(id, fistName, lastName, username,email, role, true, AddressDTO.emptyAddress(), PhoneNumberDTO.emptyPhone());
		given(userService.changeRole(roleUpdateRequest)).willReturn(userDTO);

		// When Then
		mockMvc.perform(put(roleApi)
						.contentType(MediaType.APPLICATION_JSON)
						.content(json)
						.with(csrf()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(2)))
				.andExpect(jsonPath("$.firstName", is(fistName)))
				.andExpect(jsonPath("$.lastName", is(lastName)))
				.andExpect(jsonPath("$.username", is(username)))
				.andExpect(jsonPath("$.role", is(role.name())))
				.andExpect(jsonPath("$.email", is(email)));
	}

	@SneakyThrows
	@Test
	@WithUserDetails
	@WithMockUser(authorities = "ADMINISTRATOR")
	public void testChangeAccess() {
		// Given
		String username = "johndoe";

		UserUnlockRequest unlockRequest = new UserUnlockRequest(
				username, "UNLOCK");

		String json = new ObjectMapper().writeValueAsString(unlockRequest);

		String result = String.format("User %s %s", username, "unlocked!");
		given(userService.changeLock(unlockRequest)).willReturn(new UserUnlockResponse(result));

		// When Then
		mockMvc.perform(put(lockApi)
						.contentType(MediaType.APPLICATION_JSON)
						.content(json)
						.with(csrf()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status", is(result)));
	}
//
	@Test
	public void testGetAllRegisteredUsersAsAnonymous() throws Exception {
		// Given
		given(userService.getAllRegisteredUsers()).willReturn(new ArrayList<>());
		// When Then
		mockMvc.perform(get(userListApi))
				.andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUser(authorities = "ADMINISTRATOR")
	public void testGetAllRegisteredUsersAsAdministrator() throws Exception {
		// Given
		given(userService.getAllRegisteredUsers()).willReturn(new ArrayList<>());
		// When Then
		mockMvc.perform(get(userListApi))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(authorities = "SUPPORT")
	public void testGetAllRegisteredUsersAsSupport() throws Exception {
		// Given
		given(userService.getAllRegisteredUsers()).willReturn(new ArrayList<>());
		// When Then
		mockMvc.perform(get(userListApi))
				.andExpect(status().isOk());
	}

	@Test
	@WithUserDetails
	@WithMockUser(authorities = "ADMINISTRATOR")
	void testDeleteUser() throws Exception {
		// Given
		String username = "johndoe";

		given(userService.deleteUserByUsername(username))
				.willReturn(new UserDeleteResponse(username, DELETED_SUCCESSFULLY_RESPONSE));

		// When Then
		mockMvc.perform(delete("/api/auth/user/{username}", username)
						.with(csrf()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.username", is(username)))
				.andExpect(jsonPath("$.status", is(DELETED_SUCCESSFULLY_RESPONSE)));

	}

}

