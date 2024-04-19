package pl.bgnat.antifraudsystem.domain.user;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Disabled;
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
import pl.bgnat.antifraudsystem.domain.enums.Role;
import pl.bgnat.antifraudsystem.domain.request.AddressRegisterRequest;
import pl.bgnat.antifraudsystem.domain.request.UserRegistrationRequest;
import pl.bgnat.antifraudsystem.domain.request.UserUnlockRequest;
import pl.bgnat.antifraudsystem.domain.request.UserUpdateRoleRequest;
import pl.bgnat.antifraudsystem.domain.response.UserDeleteResponse;
import pl.bgnat.antifraudsystem.domain.response.UserUnlockResponse;
import pl.bgnat.antifraudsystem.utils.date.DateTimeUtils;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.bgnat.antifraudsystem.domain.response.UserDeleteResponse.DELETED_SUCCESSFULLY_RESPONSE;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserControllerTest.class)
@Disabled // old endpoints and models
public class UserControllerTest {
    private final String USER_LIST_URL = "/api/auth/list";
    private final String USER_URL = "/api/auth/user";
    private final String LOCK_URL = "/api/auth/access";
    private final String ROLE_URL = "/api/auth/role";

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
                        .phoneNumber("+48123123123")
                        .dateOfBirth(DateTimeUtils.parseLocalDate("2000-02-11"))
                        .build();

        String jsonUser = new ObjectMapper().writeValueAsString(registrationRequest);
        UserDTO userDTO = UserDTO.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .username("johndoe")
                .email("johndoe@gmail.com")
                .dateOfBirth(DateTimeUtils.parseLocalDate("2000-02-11"))
                .role(Role.ADMINISTRATOR)
                .isActive(true)
                .build();

		given(userFacade.registerUser(registrationRequest)).willReturn(userDTO);

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

		given(userFacade.addUserAddress("johndoe", addressRegisterRequest)).willReturn(userDTO);

        // When Then
        mockMvc.perform(patch(USER_URL + "/johndoe/address/register")
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
        mockMvc.perform(get(USER_URL + "/johndoe")
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
        UserDTO userDTO = UserDTO.builder()
                .id(id)
                .firstName(fistName)
                .lastName(lastName)
                .email(email)
                .role(role)
                .isActive(true)
                .build();

        given(userFacade.changeRole(roleUpdateRequest)).willReturn(userDTO);

        // When Then
        mockMvc.perform(put(ROLE_URL)
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
        given(userFacade.changeLock(unlockRequest)).willReturn(new UserUnlockResponse(result));

        // When Then
        mockMvc.perform(put(LOCK_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(result)));
    }
}