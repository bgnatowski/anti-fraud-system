package pl.bgnat.antifraudsystem.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.bgnat.antifraudsystem.exception.DuplicateResourceException;
import pl.bgnat.antifraudsystem.exception.RequestValidationException;
import pl.bgnat.antifraudsystem.user.dto.*;
import pl.bgnat.antifraudsystem.user.enums.Country;
import pl.bgnat.antifraudsystem.user.enums.Role;
import pl.bgnat.antifraudsystem.user.exceptions.*;
import pl.bgnat.antifraudsystem.utils.validator.PhoneNumberValidator;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static pl.bgnat.antifraudsystem.user.exceptions.DuplicatedUserEmailException.USER_WITH_EMAIL_S_ALREADY_EXISTS;
import static pl.bgnat.antifraudsystem.user.exceptions.DuplicatedUsernameException.USER_WITH_USERNAME_S_ALREADY_EXISTS;
import static pl.bgnat.antifraudsystem.user.exceptions.InvalidAddressFormatException.INVALID_ADDRESS_FORMAT_S;
import static pl.bgnat.antifraudsystem.user.exceptions.InvalidPhoneFormatException.INVALID_PHONE_NUMBER_FORMAT_S;
import static pl.bgnat.antifraudsystem.user.exceptions.UserNotFoundException.THERE_IS_NO_USER_WITH_USERNAME_S;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
	@Mock
	private UserRepository userRepository;
	@Mock
	private UserCreator userCreator;
	@Mock
	private EmailService emailService;
	@Mock
	private Clock clock;

	private UserService serviceUnderTest;

	private final UserDTOMapper userDTOMapper = new UserDTOMapper(
			new AddressDTOMapper(),
			new PhoneNumberDTOMapper());

	private static ZonedDateTime NOW = ZonedDateTime.of(
			2023,
			6,
			20,
			12,
			30,
			30,
			0,
			ZoneId.of("GMT")
	);
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		when(clock.getZone()).thenReturn(NOW.getZone());
		when(clock.instant()).thenReturn(NOW.toInstant());
		serviceUnderTest = new UserService(
				userRepository,
				userDTOMapper,
				emailService,
				userCreator,
				clock);
	}

	private static final User fullAdmin = User.builder()
			.id(1L)
			.username("johndoe")
			.firstName("John")
			.lastName("Doe")
			.password("password")
			.email("johndoe@gmail.com")
			.role(Role.ADMINISTRATOR)
			.accountNonLocked(true)
			.phone(PhoneNumber.builder().number("123123123").build())
			.address(Address.builder()
					.addressLine1("Some street")
					.addressLine2("Apartment 123")
					.postalCode("12345")
					.city("Cityville")
					.state("State")
					.country(Country.POLAND)
					.build())
			.build();


	private static final User fullUser = User.builder()
			.id(2L)
			.username("johndoe")
			.firstName("John")
			.lastName("Doe")
			.password("password")
			.email("johndoe@gmail.com")
			.role(Role.MERCHANT)
			.accountNonLocked(true)
			.phone(PhoneNumber.builder().number("123123123").build())
			.address(Address.builder()
					.addressLine1("Some street")
					.addressLine2("Apartment 123")
					.postalCode("12345")
					.city("Cityville")
					.state("State")
					.country(Country.POLAND)
					.build())
			.build();

	private static final User lockedUserAfterRegistration = User.builder()
			.id(2L)
			.username("johndoe")
			.firstName("John")
			.lastName("Doe")
			.password("password")
			.email("johndoe@gmail.com")
			.role(Role.MERCHANT)
			.accountNonLocked(false)
			.temporaryAuthorization(TemporaryAuthorization.builder()
					.code("12345")
					.expirationDate(LocalDateTime.of(2023,
							6,
							20,
							12,
							30,
							29,
							0))
					.build())
			.phone(PhoneNumber.builder().number("123123123").build())
			.build();

	private static final User adminAfterRegistration = User.builder()
			.id(2L)
			.username("johndoe")
			.firstName("John")
			.lastName("Doe")
			.password("password")
			.email("johndoe@gmail.com")
			.role(Role.ADMINISTRATOR)
			.accountNonLocked(true)
			.phone(PhoneNumber.builder().number("123123123").build())
			.build();

	private static final UserRegistrationRequest fullRegistrationRequest =
			UserRegistrationRequest.builder()
					.firstName("John")
					.lastName("Doe")
					.username("johndoe")
					.password("password")
					.email("johndoe@gmail.com")
					.phoneNumber("123123123")
					.build();



	@Test
	void shouldGetAllRegisteredUsers() {
		// Given
		Page<User> page = mock(Page.class);
		List<User> users = List.of(new User());
		when(page.getContent()).thenReturn(users);
		given(userRepository.findAll(any(Pageable.class))).willReturn(page);
		// When
		List<UserDTO> actual = serviceUnderTest.getAllRegisteredUsers();
		// Then
		ArgumentCaptor<Pageable> pageableArgumentCaptor = ArgumentCaptor.forClass(Pageable.class);
		verify(userRepository).findAll(pageableArgumentCaptor.capture());
		Pageable capturedPageable = pageableArgumentCaptor.getValue();
		assertThat(capturedPageable).isEqualTo(Pageable.ofSize(100));
		assertThat(actual).isEqualTo(users.stream().map(userDTOMapper).collect(Collectors.toList()));
	}

	@Test
	void shouldThrowRequestValidationExceptionWhenTryToRegisterWithInvalidData() {
		// Given
		UserRegistrationRequest registrationRequest =
				new UserRegistrationRequest("firstName", null, null, "password", null, null);
		// When
		// Then
		assertThatThrownBy(() -> serviceUnderTest.registerUser(registrationRequest))
				.isInstanceOf(RequestValidationException.class)
				.hasMessageContaining("Wrong json format");
		verify(userRepository, never()).save(any(User.class));
	}

	@Test
	void shouldThrowDuplicatedUsernameExceptionWhenTryToRegisterUserWithSameUsername() {
		// Given
		String username = fullRegistrationRequest.username();
		given(userRepository.existsUserByUsername(username)).willReturn(true);
		// When
		// Then
		assertThatThrownBy(() -> serviceUnderTest.registerUser(fullRegistrationRequest))
				.isInstanceOf(DuplicatedUsernameException.class)
				.hasMessageContaining(String.format(USER_WITH_USERNAME_S_ALREADY_EXISTS, username));
		verify(userRepository, never()).save(any(User.class));
	}

	@Test
	void shouldThrowDuplicatedUserEmailExceptionWhenTryToRegisterUserWithSameEmail() {
		// Given
		String email = fullRegistrationRequest.email();
		given(userRepository.existsUserByEmail(email)).willReturn(true);
		// When
		// Then
		assertThatThrownBy(() -> serviceUnderTest.registerUser(fullRegistrationRequest))
				.isInstanceOf(DuplicatedUserEmailException.class)
				.hasMessageContaining(String.format(USER_WITH_EMAIL_S_ALREADY_EXISTS, email));
		verify(userRepository, never()).save(any(User.class));
	}

	@Test
	void shouldThrowDuplicatedUserPhoneNumberExceptionWhenTryToRegisterUserWithSamePhoneNumber() {
		// Given
		String number = PhoneNumberValidator.extractDigits(fullRegistrationRequest.phoneNumber());
		given(userRepository.existsUserByPhoneNumer(number)).willReturn(true);
		// When
		// Then
		assertThatThrownBy(() -> serviceUnderTest.registerUser(fullRegistrationRequest))
				.isInstanceOf(DuplicatedUserPhoneNumberException.class)
				.hasMessageContaining(String.format("Phone number: %s is already assigned to the different user!", number));
		verify(userRepository, never()).save(any(User.class));
	}
	@Test
	void shouldAddAdministratorWhenHeIsNotExist() {
		// Given
		String username = fullRegistrationRequest.username();
		// When
		given(userRepository.existsUserByUsername(username)).willReturn(false);
		given(userRepository.existsById(1L)).willReturn(false);
		given(userRepository.save(adminAfterRegistration)).willReturn(adminAfterRegistration);
		given(userCreator.createAdministrator(fullRegistrationRequest)).willReturn(adminAfterRegistration);
		serviceUnderTest.registerUser(fullRegistrationRequest);
		// Then
		ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
		verify(userRepository).save(userArgumentCaptor.capture());

		var capturedUser = userArgumentCaptor.getValue();
		assertThat(capturedUser).isEqualTo(adminAfterRegistration);
	}

	@Test
	void shouldThrowInvalidPhoneFormatExceptionWhenAddInvalidPhoneRequest() {
		// Given
		UserRegistrationRequest requestWithBadPhone = UserRegistrationRequest.builder()
				.firstName("John")
				.lastName("Doe")
				.username("johndoe")
				.password("password")
				.email("johndoe@gmail.com")
				.phoneNumber("")
				.build();

		// When
		// Then
		assertThatThrownBy(() -> serviceUnderTest.registerUser(requestWithBadPhone))
				.isInstanceOf(InvalidPhoneFormatException.class)
				.hasMessageContaining(String.format(INVALID_PHONE_NUMBER_FORMAT_S, ""));
		verify(userRepository, never()).save(any(User.class));
	}

	@Test
	void shouldThrowInvalidPhoneFormatExceptionWhenAddInvalidPhoneRequest2() {
		// Given
		UserRegistrationRequest requestWithBadPhone = UserRegistrationRequest.builder()
				.firstName("John")
				.lastName("Doe")
				.username("johndoe")
				.password("password")
				.email("johndoe@gmail.com")
				.phoneNumber("12312312300")
				.build();
		// When
		// Then
		assertThatThrownBy(() -> serviceUnderTest.registerUser(requestWithBadPhone))
				.isInstanceOf(InvalidPhoneFormatException.class)
				.hasMessageContaining(String.format(INVALID_PHONE_NUMBER_FORMAT_S, "12312312300"));
		verify(userRepository, never()).save(any(User.class));
	}

	@Test
	void shouldThrowInvalidPhoneFormatExceptionWhenAddInvalidPhoneRequest3() {
		// Given
		UserRegistrationRequest requestWithBadPhone = UserRegistrationRequest.builder()
				.firstName("John")
				.lastName("Doe")
				.username("johndoe")
				.password("password")
				.email("johndoe@gmail.com")
				.phoneNumber("asd123")
				.build();
		// When
		// Then
		assertThatThrownBy(() -> serviceUnderTest.registerUser(requestWithBadPhone))
				.isInstanceOf(InvalidPhoneFormatException.class)
				.hasMessageContaining(String.format(INVALID_PHONE_NUMBER_FORMAT_S, "asd123"));
		verify(userRepository, never()).save(any(User.class));
	}


	@Test
	void shouldAddMerchantWhenAdministratorExist() {
		// Given
		String username = fullRegistrationRequest.username();
		// When
		given(userRepository.existsUserByUsername(username)).willReturn(false);
		given(userRepository.existsById(1L)).willReturn(true);
		given(userCreator.createMerchant(fullRegistrationRequest)).willReturn(lockedUserAfterRegistration);
		given(userRepository.save(lockedUserAfterRegistration)).willReturn(lockedUserAfterRegistration);
		serviceUnderTest.registerUser(fullRegistrationRequest);
		// Then
		ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
		verify(userRepository).save(userArgumentCaptor.capture());

		User capturedUser = userArgumentCaptor.getValue();
		assertThat(capturedUser).isEqualTo(lockedUserAfterRegistration);
	}

	@Test
	void shouldDeleteCustomerByUsername() {
		// Given
		var username = "johndoe";

		when(userRepository.existsUserByUsername(username)).thenReturn(true);

		UserDeleteResponse expectedResult = new UserDeleteResponse(username, "Deleted successfully!");

		// When
		UserDeleteResponse actualResult = serviceUnderTest.deleteUserByUsername(username);
		// Then
		verify(userRepository).deleteUserByUsername(username);
		assertThat(actualResult).isEqualTo(expectedResult);
	}

	@Test
	void shouldThrowResourceNotFoundExceptionWhenTryToDeleteCustomerThatDoesNotExist() {
		// Given
		var username = "johndoe_that_not_exist";

		when(userRepository.existsUserByUsername(username)).thenReturn(false);

		// When
		assertThatThrownBy(() -> serviceUnderTest.deleteUserByUsername(username))
				.isInstanceOf(UserNotFoundException.class)
				.hasMessageContaining(String.format(THERE_IS_NO_USER_WITH_USERNAME_S, username));
		// Then
		verify(userRepository, never()).deleteUserByUsername(username);
	}

	@Test
	void shouldThrowRequestValidationExceptionWhenChangingRoleToOneThatDoesNotExist() {
		// Given
		String roleThatNotExist = "ROLE_THAT_NOT_EXIST";
		UserUpdateRoleRequest wrongRoleUpdate = new UserUpdateRoleRequest("username", roleThatNotExist);

		// When Then
		assertThatThrownBy(() -> serviceUnderTest.changeRole(wrongRoleUpdate))
				.isInstanceOf(RequestValidationException.class)
				.hasMessageContaining(String.format("Invalid role: %s", roleThatNotExist));
		verify(userRepository, never()).save(any(User.class));
	}

	@Test
	void shouldThrowRequestValidationExceptionWhenChangingRoleToOneThatIsPassedNull() {
		// Given
		UserUpdateRoleRequest wrongRoleUpdate = new UserUpdateRoleRequest("username", null);

		// When Then
		assertThatThrownBy(() -> serviceUnderTest.changeRole(wrongRoleUpdate))
				.isInstanceOf(RequestValidationException.class)
				.hasMessageContaining(String.format("Invalid role: %s", (Object) null));
		verify(userRepository, never()).save(any(User.class));
	}

	@Test
	void shouldThrowResourceNotFoundExceptionWhenChangingRoleToUserThatDoesNotExist() {
		// Given
		String usernameThatNotExist = "usernameThatNotExist";
		UserUpdateRoleRequest wrongRoleUpdate = new UserUpdateRoleRequest(usernameThatNotExist, "SUPPORT");
		// When Then
		assertThatThrownBy(() -> serviceUnderTest.changeRole(wrongRoleUpdate))
				.isInstanceOf(UserNotFoundException.class)
				.hasMessageContaining(String.format(THERE_IS_NO_USER_WITH_USERNAME_S, usernameThatNotExist));

		verify(userRepository, never()).save(any(User.class));
	}

	@Test
	void shouldThrowRequestValidationExceptionWhenChangeRoleIsNotSupportOrMerchant() {
		// Given
		String role = "ADMINISTRATOR";
		String username = "username";
		UserUpdateRoleRequest wrongRoleUpdate = new UserUpdateRoleRequest(username, role);

		User user = User.builder()
				.username(username)
				.role(Role.ADMINISTRATOR)
				.build();

		given(userRepository.findUserByUsername(username)).willReturn(Optional.of(user));
		// When Then
		assertThatThrownBy(() -> serviceUnderTest.changeRole(wrongRoleUpdate))
				.isInstanceOf(RequestValidationException.class)
				.hasMessageContaining("The role should be SUPPORT or MERCHANT");
		verify(userRepository, never()).save(any(User.class));
	}

	//
	@Test
	void shouldThrowDuplicateResourceExceptionWhenChangeToTheSameRole() {
		// Given
		String role = "MERCHANT";
		String username = "username";
		UserUpdateRoleRequest updateRequest = new UserUpdateRoleRequest(username, role);

		User user = User.builder()
				.username(username)
				.role(Role.MERCHANT)
				.build();
		given(userRepository.findUserByUsername(username)).willReturn(Optional.of(user));
		//When Then
		assertThatThrownBy(() -> serviceUnderTest.changeRole(updateRequest))
				.isInstanceOf(DuplicateResourceException.class)
				.hasMessageContaining(String.format("Role: %s is already assigned to the user with username = %s", role, username));
		verify(userRepository, never()).save(any(User.class));
	}

	@Test
	void shouldUpdateUserWhenChangeRole() {
		// Given
		String role = Role.SUPPORT.toString();
		String username = "username";
		UserUpdateRoleRequest updateRequest = new UserUpdateRoleRequest(username, role);


		User user = User.builder()
				.username(username)
				.role(Role.MERCHANT)
				.build();
		given(userRepository.findUserByUsername(username)).willReturn(Optional.of(user));

		//When
		serviceUnderTest.changeRole(updateRequest);

		// Then
		ArgumentCaptor<User> userArgumentCaptor =
				ArgumentCaptor.forClass(User.class);
		verify(userRepository, times(1)).save(userArgumentCaptor.capture());

		User capturedUser = userArgumentCaptor.getValue();
		assertThat(capturedUser.getRole()).isEqualTo(Role.SUPPORT);
	}

	@Test
	void shouldThrowResourceNotFoundExceptionWhenTryToChangeLockToUserThatNotExist() {
		// Given
		var username = "johndoe";
		UserUnlockRequest unlockRequest = new UserUnlockRequest(username, "UNLOCK");


		assertThatThrownBy(() -> serviceUnderTest.changeLock(unlockRequest))
				.isInstanceOf(UserNotFoundException.class)
				.hasMessageContaining(String.format(THERE_IS_NO_USER_WITH_USERNAME_S, username));

		verify(userRepository, never()).save(any(User.class));
	}

	@Test
	void shouldThrowRequestValidationExceptionWhenTryToChangeLockToAdministrator() {
		// Given
		String username = "administrator";

		User administrator = User.builder()
				.username(username)
				.role(Role.ADMINISTRATOR)
				.build();

		UserUnlockRequest lockRequest = new UserUnlockRequest(username, "LOCK");
		given(userRepository.findUserByUsername(username)).willReturn(Optional.of(administrator));
		// When
		// Then
		assertThatThrownBy(() -> serviceUnderTest.changeLock(lockRequest))
				.isInstanceOf(RequestValidationException.class)
				.hasMessageContaining("Cannot block administrator!");
		verify(userRepository, never()).save(any());
	}

	@Test
	void shouldThrowRequestValidationExceptionWhenTryToChangeLockWithWrongOperationRequestContainingNull() {
		// Given
		var username = "johndoe";
		UserUnlockRequest unlockRequest = new UserUnlockRequest(username, null);
		// When Then
		assertThatThrownBy(() -> serviceUnderTest.changeLock(unlockRequest))
				.isInstanceOf(RequestValidationException.class)
				.hasMessageContaining("Invalid request form");

		verify(userRepository, never()).save(any(User.class));
	}

	@Test
	void shouldThrowRequestValidationExceptionWhenTryToChangeLockWithWrongOperation() {
		// Given
		var username = fullUser.getUsername();

		String operation = "INVALID_OPERATION";
		UserUnlockRequest unlockRequest = new UserUnlockRequest(username, operation);
		given(userRepository.findUserByUsername(username)).willReturn(Optional.of(fullUser));

		// When Then
		assertThatThrownBy(() -> serviceUnderTest.changeLock(unlockRequest))
				.isInstanceOf(RequestValidationException.class)
				.hasMessageContaining(String.format("Invalid operation: %s", operation));

		verify(userRepository, never()).save(any(User.class));
	}

	@Test
	void shouldUnlockLockedUser() {
		// Given
		var username = "johndoe";

		User user = User.builder()
				.username(username)
				.role(Role.MERCHANT)
				.build();

		String operation = "UNLOCK";
		UserUnlockRequest unlockRequest = new UserUnlockRequest(username, operation);
		given(userRepository.findUserByUsername(username)).willReturn(Optional.of(user));
		//When
		serviceUnderTest.changeLock(unlockRequest);
		// Then
		ArgumentCaptor<User> userArgumentCaptor =
				ArgumentCaptor.forClass(User.class);
		verify(userRepository, times(1)).save(userArgumentCaptor.capture());
		User capturedUser = userArgumentCaptor.getValue();
		assertThat(capturedUser.isAccountNonLocked()).isTrue();
	}

	@Test
	void shouldLockUnLockedUser() {
		// Given
		var username = lockedUserAfterRegistration.getUsername();

		String operation = "LOCK";
		UserUnlockRequest unlockRequest = new UserUnlockRequest(username, operation);
		given(userRepository.findUserByUsername(username)).willReturn(Optional.of(lockedUserAfterRegistration));
		//When
		serviceUnderTest.changeLock(unlockRequest);
		// Then
		ArgumentCaptor<User> userArgumentCaptor =
				ArgumentCaptor.forClass(User.class);
		verify(userRepository, times(1)).save(userArgumentCaptor.capture());
		User capturedUser = userArgumentCaptor.getValue();
		assertThat(capturedUser.isAccountNonLocked()).isFalse();
	}

	@Test
	void shouldReturnUserByUsername() {
		// Given
		var username = "johndoe";
		given(userRepository.findUserByUsername(username)).willReturn(Optional.of(fullUser));
		//When
		UserDTO actualUserDTO = serviceUnderTest.getUserByUsername(username);
		UserDTO expectedUserDTO = userDTOMapper.apply(fullUser);
		// Then
		verify(userRepository, times(1)).findUserByUsername(username);
		assertThat(actualUserDTO).isEqualTo(expectedUserDTO);
	}

	@Test
	void shouldThrowInvalidAddressFormatExceptionWhenAddInvalidAddressRequest() {
		// Given
		String username = "johndoe";
		AddressRegisterRequest addressRegisterRequest = null;
		// When
		given(userRepository.findUserByUsername(username)).willReturn(Optional.of(new User()));
		// Then
		assertThatThrownBy(() -> serviceUnderTest.addUserAddress(username, addressRegisterRequest))
				.isInstanceOf(InvalidAddressFormatException.class)
				.hasMessageContaining("Address request is null");
		verify(userRepository, never()).save(any(User.class));
	}

	@Test
	void shouldThrowInvalidAddressFormatExceptionWhenAddInvalidAddressRequest2() {
		// Given
		String username = "johndoe";
		AddressRegisterRequest addressRegisterRequest = new AddressRegisterRequest(
				"", "", "", "", "", "");
		// When
		given(userRepository.findUserByUsername(username)).willReturn(Optional.of(new User()));
		// Then
		assertThatThrownBy(() -> serviceUnderTest.addUserAddress(username, addressRegisterRequest))
				.isInstanceOf(InvalidAddressFormatException.class)
				.hasMessageContaining(String.format(INVALID_ADDRESS_FORMAT_S, ""));
		verify(userRepository, never()).save(any(User.class));
	}

	@Test
	void shouldThrowInvalidAddressFormatExceptionWhenAddInvalidAddressRequest3() {
		// Given
		String username = "johndoe";
		AddressRegisterRequest addressRegisterRequest = new AddressRegisterRequest(
				"Some street", "some address", "12345", "city", "state", "non-found-country");
		// When
		given(userRepository.findUserByUsername(username)).willReturn(Optional.of(new User()));
		// Then
		assertThatThrownBy(() -> serviceUnderTest.addUserAddress(username, addressRegisterRequest))
				.isInstanceOf(InvalidAddressFormatException.class)
				.hasMessageContaining(String.format(INVALID_ADDRESS_FORMAT_S, "non-found-country not supported"));
		verify(userRepository, never()).save(any(User.class));
	}

	@Test
	void shouldAddAddressToUser1() {
		// Given
		String username = "johndoe";
		User givenUser = User.builder()
				.id(2L)
				.username("johndoe")
				.firstName("John")
				.lastName("Doe")
				.password("password")
				.email("johndoe@gmail.com")
				.role(Role.MERCHANT)
				.accountNonLocked(true)
				.build();
		AddressRegisterRequest addressRegisterRequest = new AddressRegisterRequest(
				"Some street",
				"Apartment 123",
				"12345",
				"Cityville",
				"State",
				"Poland");
		// When
		when(userRepository.findUserByUsername(username)).thenReturn(Optional.ofNullable(givenUser));
		// Then
		UserDTO actualUserDTO = serviceUnderTest.addUserAddress(username, addressRegisterRequest);

		Objects.requireNonNull(givenUser).setAddress(
				Address.builder()
						.user(givenUser)
						.addressLine1("Some street")
						.addressLine2("Apartment 123")
						.postalCode("12345")
						.city("Cityville")
						.state("State")
						.country(Country.POLAND)
						.build()
		);

		UserDTO expectedUserDTO = userDTOMapper.apply(givenUser);

		assertThat(actualUserDTO).isEqualTo(expectedUserDTO);
		verify(userRepository, times(1)).save(any(User.class));
	}

	@Test
	void shouldThrowUserIsAlreadyUnlockExceptionWhenEmailAlreadyConfirmed(){
		// Given
		String code = "12345";
		String username = fullUser.getUsername();
		String email = fullUser.getEmail();
		// When
		when(userRepository.findUserByUsername(username)).thenReturn(Optional.ofNullable(fullUser));
		// Then
		assertThatThrownBy(() -> serviceUnderTest.confirmUserEmail(username, code))
				.isInstanceOf(UserIsAlreadyUnlockException.class)
				.hasMessageContaining(String.format("User: %s with email: %s is already confirmed", username, email));
		verify(userRepository, never()).save(any(User.class));
	}

	@Test
	void shouldThrowTemporaryAuthorizationExceptionWhenCodeIsExpired(){
		// Given
		String code = "12345";
		String username = lockedUserAfterRegistration.getUsername();
		// When
		when(userRepository.findUserByUsername(username)).thenReturn(Optional.ofNullable(lockedUserAfterRegistration));
		// Then
		assertThatThrownBy(() -> serviceUnderTest.confirmUserEmail(username, code))
				.isInstanceOf(TemporaryAuthorizationException.class)
				.hasMessageContaining(String.format(
						"Verification code is expired. " +
								"If you want to confirm user email generate new code on: " +
								"http://localhost:4200/user/%s/auth/generatecode", username
				));
		verify(userRepository, never()).save(any(User.class));
	}



}
