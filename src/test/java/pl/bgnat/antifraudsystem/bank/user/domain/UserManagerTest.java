package pl.bgnat.antifraudsystem.bank.user.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.bgnat.antifraudsystem.bank.user.dto.PhoneNumberDTO;
import pl.bgnat.antifraudsystem.bank.user.dto.UserDTO;
import pl.bgnat.antifraudsystem.bank.user.dto.request.UserRegistrationRequest;
import pl.bgnat.antifraudsystem.bank.user.enums.Role;

import java.time.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserManagerTest {
	private UserManager userManager;
	@Mock private UserService userService;
	@Mock private EmailService emailService;
	@Mock private CreditCardService creditCardService;
	@Mock private AccountService accountService;
	@Mock private TemporaryAuthorizationService temporaryAuthorizationService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		userManager = new UserManager(
				userService,
				emailService,
				creditCardService,
				accountService,
				temporaryAuthorizationService
		);
	}

	private static User lockedUserAfterRegistration = User.builder()
			.id(2L)
			.username("johndoe")
			.firstName("John")
			.lastName("Doe")
			.password("password")
			.email("johndoe@gmail.com")
			.role(Role.MERCHANT)
			.accountNonLocked(false)
			.hasAnyCreditCard(false)
			.hasAccount(false)
			.dateOfBirth(LocalDate.of(2000, 11, 2))
			.numberOfCreditCards(0)
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

	@Test
	void shouldRegisterUser() {
		// given
		UserRegistrationRequest givenRegistrationRequest = UserRegistrationRequest.builder()
				.firstName("John")
				.lastName("Doe")
				.email("johndoe@gmail.com")
				.username("johndoe")
				.password("password")
				.phoneNumber("123123123")
				.dateOfBirth(LocalDate.of(2000, 11, 2))
				.build();

		UserDTO expectedUserDTO = UserDTO.builder()
				.id(2L)
				.username("johndoe")
				.firstName("John")
				.lastName("Doe")
				.email("johndoe@gmail.com")
				.role(Role.MERCHANT)
				.isActive(false)
				.hasAnyCreditCard(false)
				.hasAccount(false)
				.dateOfBirth(LocalDate.of(2000, 11, 2))
				.numberOfCreditCards(0)
				.phoneNumber(PhoneNumberDTO.builder()
						.number("123123123")
						.areaCode("+48")
						.build())
				.build();
//

		when(userService.registerUser(givenRegistrationRequest)).thenReturn(lockedUserAfterRegistration);
		when(userService.mapToDto(lockedUserAfterRegistration)).thenReturn(expectedUserDTO);
//		//when
		UserDTO userDTO = userManager.registerUser(givenRegistrationRequest);
//
		assertThat(userDTO).isEqualTo(expectedUserDTO);
	}


}
