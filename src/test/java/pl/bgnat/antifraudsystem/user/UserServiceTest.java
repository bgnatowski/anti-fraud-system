package pl.bgnat.antifraudsystem.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.bgnat.antifraudsystem.exception.DuplicateResourceException;
import pl.bgnat.antifraudsystem.exception.RequestValidationException;
import pl.bgnat.antifraudsystem.exception.ResourceNotFoundException;
import pl.bgnat.antifraudsystem.exception.user.DuplicatedUserException;
import pl.bgnat.antifraudsystem.exception.user.UserNotFoundException;
import pl.bgnat.antifraudsystem.user.request.UserRegistrationRequest;
import pl.bgnat.antifraudsystem.user.request.UserRoleUpdateRequest;
import pl.bgnat.antifraudsystem.user.request.UserUnlockRequest;
import pl.bgnat.antifraudsystem.user.response.UserDeleteResponse;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static pl.bgnat.antifraudsystem.exception.user.DuplicatedUserException.*;
import static pl.bgnat.antifraudsystem.exception.user.UserNotFoundException.THERE_IS_NO_USER_WITH_USERNAME_S;
import static pl.bgnat.antifraudsystem.user.UserCreator.createAdministrator;
import static pl.bgnat.antifraudsystem.user.UserCreator.createMerchant;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

	@Mock
	private UserDao userDao;
	@Mock
	private PasswordEncoder passwordEncoder;
	private UserService serviceUnderTest;
	private final UserDTOMapper userDTOMapper = new UserDTOMapper();

	@BeforeEach
	void setUp() {
		serviceUnderTest = new UserService(
				userDao,
				passwordEncoder,
				userDTOMapper);
	}

	@Test
	void shouldGetAllRegisteredUsers(){
		// When
		serviceUnderTest.getAllRegisteredUsers();
		// Then
		verify(userDao).selectAllUsers();
	}

	@Test
	void shouldThrowRequestValidationExceptionWhenTryToRegisterWithInvalidData(){
		// Given
		UserRegistrationRequest registrationRequest =
				new UserRegistrationRequest("name", null, "password");
		// When
		// Then
		assertThatThrownBy(() -> serviceUnderTest.registerUser(registrationRequest))
				.isInstanceOf(RequestValidationException.class)
				.hasMessageContaining("Wrong json format");
		verify(userDao, never()).insertUser(any(User.class));
	}

	@Test
	void shouldThrowDuplicateResourceExceptionWhenTryToRegisterUserWithSameUsername(){
		// Given
		String username = "johndoe";
		String name = "John Doe";
		String password = "password";
		UserRegistrationRequest registrationRequest =
				new UserRegistrationRequest(name, username, password);
		// When
		given(userDao.existsUserWithUsername(username)).willReturn(true);
		// Then
		assertThatThrownBy(() -> serviceUnderTest.registerUser(registrationRequest))
				.isInstanceOf(DuplicatedUserException.class)
				.hasMessageContaining(String.format(USER_WITH_USERNAME_S_ALREADY_EXISTS, username));
		verify(userDao, never()).insertUser(any(User.class));
	}

	@Test
	void shouldAddAdministratorWhenHeIsNotExist(){
		// Given
		String name = "Administrator";
		String username = "administrator";
		String password = "password";
		UserRegistrationRequest registrationRequest =
				new UserRegistrationRequest(name, username, password);

		User administrator = createAdministrator(registrationRequest, passwordEncoder);
		// When
		given(userDao.existsUserWithUsername(username)).willReturn(false);
		given(userDao.existsUserById(1L)).willReturn(false);
		given(userDao.insertUser(administrator)).willReturn(administrator);

		serviceUnderTest.registerUser(registrationRequest);
		// Then
		ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
		verify(userDao).insertUser(userArgumentCaptor.capture());

		var capturedUser = userArgumentCaptor.getValue();
		assertThat(capturedUser).isEqualTo(administrator);
	}

	@Test
	void shouldAddMerchantWhenAdministratorExist(){
		// Given
		String name = "MerchantName";
		String username = "merchantUsername";
		String password = "merchantPassword";
		UserRegistrationRequest registrationRequest =
				new UserRegistrationRequest(name, username, password);

		User merchant = createMerchant(registrationRequest, passwordEncoder);
		// When
		given(userDao.existsUserWithUsername(username)).willReturn(false);
		given(userDao.existsUserById(1L)).willReturn(true);
		given(userDao.insertUser(merchant)).willReturn(merchant);
		serviceUnderTest.registerUser(registrationRequest);
		// Then
		ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
		verify(userDao).insertUser(userArgumentCaptor.capture());

		User capturedUser = userArgumentCaptor.getValue();
		assertThat(capturedUser).isEqualTo(merchant);
	}

	@Test
	void shouldDeleteCustomerByUsername() {
		// Given
		var username = "johndoe";

		when(userDao.existsUserWithUsername(username)).thenReturn(true);

		UserDeleteResponse expectedResult = new UserDeleteResponse(username, "Deleted successfully!");

		// When
		UserDeleteResponse actualResult = serviceUnderTest.deleteUserByUsername(username);
		// Then
		verify(userDao).deleteUserByUsername(username);
		assertThat(actualResult).isEqualTo(expectedResult);
	}

	@Test
	void shouldThrowResourceNotFoundExceptionWhenTryToDeleteCustomerThatDoesNotExist() {
		// Given
		var username = "johndoe_that_not_exist";

		when(userDao.existsUserWithUsername(username)).thenReturn(false);

		// When
		assertThatThrownBy(() -> serviceUnderTest.deleteUserByUsername(username))
				.isInstanceOf(UserNotFoundException.class)
				.hasMessageContaining(String.format(THERE_IS_NO_USER_WITH_USERNAME_S, username));
		// Then
		verify(userDao, never()).deleteUserByUsername(username);
	}

	@Test
	void shouldThrowRequestValidationExceptionWhenChangingRoleToOneThatDoesNotExist(){
		// Given
		String roleThatNotExist = "ROLE_THAT_NOT_EXIST";
		UserRoleUpdateRequest wrongRoleUpdate = new UserRoleUpdateRequest("username", roleThatNotExist);

		// When Then
		assertThatThrownBy(() -> serviceUnderTest.changeRole(wrongRoleUpdate))
				.isInstanceOf(RequestValidationException.class)
				.hasMessageContaining(String.format("Invalid role: %s", roleThatNotExist));
		verify(userDao, never()).insertUser(any(User.class));
	}

	@Test
	void shouldThrowRequestValidationExceptionWhenChangingRoleToOneThatIsPassedNull(){
		// Given
		UserRoleUpdateRequest wrongRoleUpdate = new UserRoleUpdateRequest("username", null);

		// When Then
		assertThatThrownBy(() -> serviceUnderTest.changeRole(wrongRoleUpdate))
				.isInstanceOf(RequestValidationException.class)
				.hasMessageContaining(String.format("Invalid role: %s", null));
		verify(userDao, never()).updateUser(any(User.class));
	}

	@Test
	void shouldThrowResourceNotFoundExceptionWhenChangingRoleToUserThatDoesNotExist(){
		// Given
		String usernameThatNotExist = "usernameThatNotExist";
		UserRoleUpdateRequest wrongRoleUpdate = new UserRoleUpdateRequest(usernameThatNotExist, "SUPPORT");
		// When Then
		assertThatThrownBy(() -> serviceUnderTest.changeRole(wrongRoleUpdate))
				.isInstanceOf(UserNotFoundException.class)
				.hasMessageContaining(String.format(THERE_IS_NO_USER_WITH_USERNAME_S, usernameThatNotExist));

		verify(userDao, never()).updateUser(any(User.class));
	}

	@Test
	void shouldThrowRequestValidationExceptionWhenChangeRoleIsNotSupportOrMerchant(){
		// Given
		String role = "ADMINISTRATOR";
		String username = "username";
		UserRoleUpdateRequest wrongRoleUpdate = new UserRoleUpdateRequest(username, role);

		User user = new User(
				"user",
				username,
				passwordEncoder.encode("password"),
				Role.MERCHANT,
				true);

		given(userDao.selectUserByUsername(username)).willReturn(Optional.of(user));
		// When Then
		assertThatThrownBy(() -> serviceUnderTest.changeRole(wrongRoleUpdate))
				.isInstanceOf(RequestValidationException.class)
				.hasMessageContaining("The role should be SUPPORT or MERCHANT");
		verify(userDao, never()).updateUser(any(User.class));
	}

	@Test
	void shouldThrowDuplicateResourceExceptionWhenChangeToTheSameRole(){
		// Given
		String role = "MERCHANT";
		String username = "username";
		UserRoleUpdateRequest updateRequest = new UserRoleUpdateRequest(username, role);

		User user = new User(
				"user",
				username,
				passwordEncoder.encode("password"),
				Role.MERCHANT,
				true);
		given(userDao.selectUserByUsername(username)).willReturn(Optional.of(user));
		//When Then
		assertThatThrownBy(() -> serviceUnderTest.changeRole(updateRequest))
				.isInstanceOf(DuplicateResourceException.class)
				.hasMessageContaining(String.format("Role: %s is already assigned to the user with username = %s", role, username));
		verify(userDao, never()).updateUser(any(User.class));
	}

	@Test
	void shouldUpdateUserWhenChangeRole(){
		// Given
		String role = "SUPPORT";
		String username = "username";
		UserRoleUpdateRequest updateRequest = new UserRoleUpdateRequest(username, role);

		User user = new User(
				"user",
				username,
				passwordEncoder.encode("password"),
				Role.MERCHANT,
				true);
		given(userDao.selectUserByUsername(username)).willReturn(Optional.of(user));

		//When
		serviceUnderTest.changeRole(updateRequest);

		// Then
		ArgumentCaptor<User> userArgumentCaptor =
				ArgumentCaptor.forClass(User.class);
		verify(userDao, times(1)).updateUser(userArgumentCaptor.capture());

		User capturedUser = userArgumentCaptor.getValue();
		assertThat(capturedUser.getRole()).isEqualTo(Role.SUPPORT);
	}

	@Test
	void shouldThrowResourceNotFoundExceptionWhenTryToChangeLockToUserThatNotExist(){
		// Given
		var username = "johndoe";
		UserUnlockRequest unlockRequest = new UserUnlockRequest(username, "UNLOCK");


		assertThatThrownBy(() -> serviceUnderTest.changeLock(unlockRequest))
				.isInstanceOf(UserNotFoundException.class)
				.hasMessageContaining(String.format(THERE_IS_NO_USER_WITH_USERNAME_S, username));

		verify(userDao, never()).updateUser(any(User.class));
	}

	@Test
	void shouldThrowRequestValidationExceptionWhenTryToChangeLockToAdministrator(){
		// Given
		String username = "administrator";
		User administrator = new User(
				"Administrator",
				username,
				passwordEncoder.encode("password"),
				Role.ADMINISTRATOR,
				true);
		UserUnlockRequest lockRequest = new UserUnlockRequest(username, "LOCK");
		given(userDao.selectUserByUsername(username)).willReturn(Optional.of(administrator));
		// When
		// Then
		assertThatThrownBy(() -> serviceUnderTest.changeLock(lockRequest))
				.isInstanceOf(RequestValidationException.class)
				.hasMessageContaining("Cannot block administrator!");
		verify(userDao, never()).updateUser(any());
	}

	@Test
	void shouldThrowRequestValidationExceptionWhenTryToChangeLockWithWrongOperationRequestContainingNull(){
		// Given
		var username = "johndoe";
		UserUnlockRequest unlockRequest = new UserUnlockRequest(username, null);
		// When Then
		assertThatThrownBy(() -> serviceUnderTest.changeLock(unlockRequest))
				.isInstanceOf(RequestValidationException.class)
				.hasMessageContaining("Invalid request form");

		verify(userDao, never()).updateUser(any(User.class));
	}

	@Test
	void shouldThrowRequestValidationExceptionWhenTryToChangeLockWithWrongOperation(){
		// Given
		var username = "johndoe";
		User user = new User(
				"User",
				username,
				passwordEncoder.encode("password"),
				Role.MERCHANT,
				false);

		String operation = "INVALID_OPERATION";
		UserUnlockRequest unlockRequest = new UserUnlockRequest(username, operation);
		given(userDao.selectUserByUsername(username)).willReturn(Optional.of(user));

		// When Then
		assertThatThrownBy(() -> serviceUnderTest.changeLock(unlockRequest))
				.isInstanceOf(RequestValidationException.class)
				.hasMessageContaining(String.format("Invalid operation: %s", operation));

		verify(userDao, never()).updateUser(any(User.class));
	}

	@Test
	void shouldUnlockLockedUser(){
		// Given
		var username = "johndoe";
		User user = new User(
				"User",
				username,
				passwordEncoder.encode("password"),
				Role.MERCHANT,
				false);

		String operation = "UNLOCK";
		UserUnlockRequest unlockRequest = new UserUnlockRequest(username, operation);
		given(userDao.selectUserByUsername(username)).willReturn(Optional.of(user));
		//When
		serviceUnderTest.changeLock(unlockRequest);
		// Then
		ArgumentCaptor<User> userArgumentCaptor =
				ArgumentCaptor.forClass(User.class);
		verify(userDao, times(1)).updateUser(userArgumentCaptor.capture());
		User capturedUser = userArgumentCaptor.getValue();
		assertThat(capturedUser.isAccountNonLocked()).isTrue();
	}

	@Test
	void shouldLockUnLockedUser(){
		// Given
		var username = "johndoe";
		User user = new User(
				"User",
				username,
				passwordEncoder.encode("password"),
				Role.MERCHANT,
				true);

		String operation = "LOCK";
		UserUnlockRequest unlockRequest = new UserUnlockRequest(username, operation);
		given(userDao.selectUserByUsername(username)).willReturn(Optional.of(user));
		//When
		serviceUnderTest.changeLock(unlockRequest);
		// Then
		ArgumentCaptor<User> userArgumentCaptor =
				ArgumentCaptor.forClass(User.class);
		verify(userDao, times(1)).updateUser(userArgumentCaptor.capture());
		User capturedUser = userArgumentCaptor.getValue();
		assertThat(capturedUser.isAccountNonLocked()).isFalse();
	}


}
