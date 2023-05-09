package pl.bgnat.antifraudsystem.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserJPADataAccessServiceTest {
	private UserJPADataAccessService serviceUnderTest;
	private AutoCloseable autoCloseable;
	@Mock
	private UserRepository userRepository;


	@BeforeEach
	void setUp() {
		autoCloseable = MockitoAnnotations.openMocks(this);
		serviceUnderTest = new UserJPADataAccessService(userRepository);
	}

	@AfterEach
	void tearDown() throws Exception {
		autoCloseable.close();
	}

	@Test
	void shouldSelectAllUsers(){
		Page<User> page = mock(Page.class);
		List<User> users = List.of(new User());

		when(page.getContent()).thenReturn(users);
		when(userRepository.findAll(any(Pageable.class))).thenReturn(page);

		// When
		List<User> expected = serviceUnderTest.selectAllUsers();

		// Then
		assertThat(expected).isEqualTo(users);
		ArgumentCaptor<Pageable> pageableArgumentCaptor = ArgumentCaptor.forClass(Pageable.class);
		verify(userRepository).findAll(pageableArgumentCaptor.capture());
		Pageable capturedPageable = pageableArgumentCaptor.getValue();
		assertThat(capturedPageable).isEqualTo(Pageable.ofSize(100));
	}

	@Test
	void shouldSelectUserById(){
		// Given
		long id = 1;
		// When
		serviceUnderTest.selectUserById(id);
		// Then
		verify(userRepository).findById(id);
	}
	@Test
	void shouldSelectUserByUsername(){
		// Given
		String username = "username";

		// When
		serviceUnderTest.selectUserByUsername(username);
		// Then
		verify(userRepository).findUserByUsername(username);
	}

	@Test
	void shouldInsertUser(){
		// Given
		User user = User.builder()
				.name("User")
				.username("username")
				.password("password")
				.role(Role.ADMINISTRATOR)
				.accountNonLocked(true)
				.build();

		// When
		serviceUnderTest.insertUser(user);

		// Then
		verify(userRepository).save(user);
	}

	@Test
	void shouldExistStudentWithUsername(){
		// Given
		String username = "username";

		// When
		serviceUnderTest.existsUserWithUsername(username);

		// Then
		verify(userRepository).existsUserByUsername(username);
	}

	@Test
	void shouldExistStudentWithId(){
		// Given
		long id = 1L;
		// When
		serviceUnderTest.existsUserById(id);
		// Then
		verify(userRepository).existsById(id);
	}

	@Test
	void shouldDeleteUserById() {
		// Given
		long id = 1L;

		// When
		serviceUnderTest.deleteUserById(id);

		// Then
		verify(userRepository).deleteById(id);
	}

	@Test
	void shouldDeleteUserByUsername() {
		// Given
		String username = "username";

		// When
		serviceUnderTest.deleteUserByUsername(username);

		// Then
		verify(userRepository).deleteUserByUsername(username);
	}

	@Test
	void shouldUpdateUser(){
		// Given
		User user = User.builder()
				.name("User")
				.username("username")
				.password("password")
				.role(Role.ADMINISTRATOR)
				.accountNonLocked(true)
				.build();
		// When
		serviceUnderTest.updateUser(user);
		// Then
		verify(userRepository).save(user);
	}


}
