package pl.bgnat.antifraudsystem.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.bgnat.antifraudsystem.exception.DuplicateResourceException;
import pl.bgnat.antifraudsystem.exception.RequestValidationException;
import pl.bgnat.antifraudsystem.user.request.UserRegistrationRequest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MyUserDetailsServiceTest {

	@Mock
	private UserDao userDao;
	@Mock
	private PasswordEncoder passwordEncoder;
	private MyUserDetailService serviceUnderTest;
	private final UserDTOMapper userDTOMapper = new UserDTOMapper();

	@BeforeEach
	void setUp() {
		serviceUnderTest = new MyUserDetailService(
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
				.isInstanceOf(DuplicateResourceException.class)
				.hasMessageContaining(String.format("User with username = %s already exists", username));
		verify(userDao, never()).insertUser(any(User.class));
	}

	@Test
	void shouldAddAdministratorWhenHeIsNotExist(){
		// Given
		String name = "Administrator";
		String username = "administrator";
		String password = "adminPassword";
		UserRegistrationRequest registrationRequest =
				new UserRegistrationRequest(name, username, password);

		User administrator = new User(
				registrationRequest.name(),
				registrationRequest.username(),
				passwordEncoder.encode(registrationRequest.password()),
				Role.ADMINISTRATOR,
				true);
		// When
		given(userDao.existsUserWithUsername(username)).willReturn(false);
		given(userDao.existsUserById(1L)).willReturn(false);
		given(userDao.insertUser(administrator)).willReturn(administrator);
		serviceUnderTest.registerUser(registrationRequest);
		// Then
		ArgumentCaptor<User> userArgumentCaptor =
				ArgumentCaptor.forClass(User.class);
		verify(userDao).insertUser(userArgumentCaptor.capture());
		verify(userDao, times(1)).insertUser(administrator);
	}




}
