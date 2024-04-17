package pl.bgnat.antifraudsystem.bank.user.domain;


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
import pl.bgnat.antifraudsystem.bank.user.dto.AddressDTO;
import pl.bgnat.antifraudsystem.bank.user.dto.PhoneNumberDTO;
import pl.bgnat.antifraudsystem.bank.user.dto.UserDTO;
import pl.bgnat.antifraudsystem.bank.user.dto.request.UserUnlockRequest;
import pl.bgnat.antifraudsystem.bank.user.dto.request.UserUpdateRoleRequest;
import pl.bgnat.antifraudsystem.bank.user.dto.response.UserUnlockResponse;
import pl.bgnat.antifraudsystem.bank.user.enums.Role;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserActionControllerTest.class)
public class UserActionControllerTest {
    private final String LOCK_URL = "/api/auth/access";
    private final String ROLE_URL = "/api/auth/role";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserFacade userFacade;

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
                .address(AddressDTO.emptyDto())
                .phoneNumber(PhoneNumberDTO.emptyDto())
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