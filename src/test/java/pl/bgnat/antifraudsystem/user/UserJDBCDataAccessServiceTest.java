package pl.bgnat.antifraudsystem.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import pl.bgnat.antifraudsystem.AbstractTestcontainers;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled("Disabled - startup have to be fixed")
public class UserJDBCDataAccessServiceTest extends AbstractTestcontainers {

	private UserJDBCDataAccessService serviceUnderTest;
	private final UserRowMapper userRowMapper = new UserRowMapper();

	@BeforeEach
	void setUp(){
		serviceUnderTest = new UserJDBCDataAccessService(
				getJdbcTemplate(),
				userRowMapper
		);
	}

	@Test
	void shouldSelectAllCustomer(){
		// Given
		User user = User.builder()
				.name("johndoe")
				.username("johndoe")
				.password("password")
				.role(Role.ADMINISTRATOR)
				.accountNonLocked(true)
				.build();
		serviceUnderTest.insertUser(user);

		// When
		List<User> actual = serviceUnderTest.selectAllUsers();
		//Then
		assertThat(actual).isNotEmpty();
	}

	@Test
	void shouldSelectUserById(){
		//Given
		String username = "username";
		User user = User.builder()
				.name("User")
				.username(username)
				.password("password")
				.role(Role.ADMINISTRATOR)
				.accountNonLocked(true)
				.build();
		serviceUnderTest.insertUser(user);

		long id = serviceUnderTest.selectAllUsers()
				.stream()
				.filter(u-> u.getUsername().equals(username))
				.findFirst()
				.map(User::getId)
				.orElseThrow();
		// When
		Optional<User> actual = serviceUnderTest.selectUserById(id);
		// Then
		assertThat(actual).isPresent().hasValueSatisfying(u->{
			assertThat(u.getId()).isEqualTo(id);
			assertThat(u.getName()).isEqualTo(user.getName());
			assertThat(u.getUsername()).isEqualTo(user.getUsername());
			assertThat(u.getPassword()).isEqualTo(user.getPassword());
			assertThat(u.getRole()).isEqualTo(user.getRole());
			assertThat(u.isAccountNonLocked()).isEqualTo(user.isAccountNonLocked());
		});
	}

	@Test
	void shouldReturnEmptyOptionalWhenSelectUserByIdThatNonExist() {
		// Given
		long id = -1;

		// When
		Optional<User> actual = serviceUnderTest.selectUserById(id);

		// Then
		assertThat(actual).isEmpty();
	}

	@Test
	void shouldExistUserWithUsername(){
		//Given
		String username = "username";
		User user = User.builder()
				.name("User")
				.username(username)
				.password("password")
				.role(Role.ADMINISTRATOR)
				.accountNonLocked(true)
				.build();
		serviceUnderTest.insertUser(user);
		// When
		boolean actual = serviceUnderTest.existsUserWithUsername(username);
		// Then
		assertThat(actual).isTrue();
	}

	@Test
	void shouldReturnFalseWhenUserWithUsernameNonExist(){
		//Given
		String username = "username";
		// When
		boolean actual = serviceUnderTest.existsUserWithUsername(username);
		// Then
		assertThat(actual).isFalse();
	}

	@Test
	void shouldExistUserWithId(){
		//Given
		String username = "username";
		User user = User.builder()
				.name("User")
				.username(username)
				.password("password")
				.role(Role.ADMINISTRATOR)
				.accountNonLocked(true)
				.build();
		serviceUnderTest.insertUser(user);

		long id = serviceUnderTest.selectAllUsers()
				.stream()
				.filter(u -> u.getUsername().equals(username))
				.map(User::getId)
				.findFirst()
				.orElseThrow();
		// When
		boolean actual = serviceUnderTest.existsUserById(id);
		// Then
		assertThat(actual).isTrue();
	}

	@Test
	void shouldReturnFalseWhenUserWithIdNonExist() {
		// Given
		long id = -1L;

		// When
		boolean actual = serviceUnderTest.existsUserById(id);

		// Then
		assertThat(actual).isFalse();
	}

	@Test
	void shouldDeleteUserById(){
		// Given
		String username = "username";
		User user = User.builder()
				.name("User")
				.username(username)
				.password("password")
				.role(Role.ADMINISTRATOR)
				.accountNonLocked(true)
				.build();
		serviceUnderTest.insertUser(user);

		long id = serviceUnderTest.selectAllUsers()
				.stream()
				.filter(u -> u.getUsername().equals(username))
				.map(User::getId)
				.findFirst()
				.orElseThrow();

		// When
		serviceUnderTest.deleteUserById(id);
		// Then
		Optional<User> actual = serviceUnderTest.selectUserById(id);
		assertThat(actual).isNotPresent();
	}

	@Test
	void shouldDeleteUserByUsername(){
		// Given
		String username = "username";
		User user = User.builder()
				.name("User")
				.username(username)
				.password("password")
				.role(Role.ADMINISTRATOR)
				.accountNonLocked(true)
				.build();
		serviceUnderTest.insertUser(user);

		// When
		serviceUnderTest.deleteUserByUsername(username);
		// Then
		Optional<User> actual = serviceUnderTest.selectUserByUsername(username);
		assertThat(actual).isNotPresent();
	}

	@Test
	void shouldUpdateUserName(){
		// Given
		String username = "username";
		User user = User.builder()
				.name("User")
				.username(username)
				.password("password")
				.role(Role.ADMINISTRATOR)
				.accountNonLocked(true)
				.build();
		serviceUnderTest.insertUser(user);

		long id = serviceUnderTest.selectAllUsers()
				.stream()
				.filter(u -> u.getUsername().equals(username))
				.map(User::getId)
				.findFirst()
				.orElseThrow();

		// When
		var newName = "newName";
		User update = User.builder()
				.id(id)
				.name(newName)
				.build();

		serviceUnderTest.updateUser(update);
		//Then
		Optional<User> actual = serviceUnderTest.selectUserById(id);
		assertThat(actual).isPresent().hasValueSatisfying(
				u ->{
					assertThat(u.getId()).isEqualTo(id);
					assertThat(u.getName()).isEqualTo(newName);
					assertThat(u.getUsername()).isEqualTo(user.getUsername());
					assertThat(u.getPassword()).isEqualTo(user.getPassword());
					assertThat(u.getPassword()).isEqualTo(user.getPassword());
					assertThat(u.getRole()).isEqualTo(user.getRole());
					assertThat(u.isAccountNonLocked()).isEqualTo(user.isAccountNonLocked());
				}
		);
	}

	@Test
	void shouldUpdateUserUsername(){
		// Given
		String username = "username";
		User user = User.builder()
				.name("User")
				.username(username)
				.password("password")
				.role(Role.ADMINISTRATOR)
				.accountNonLocked(true)
				.build();
		serviceUnderTest.insertUser(user);

		long id = serviceUnderTest.selectAllUsers()
				.stream()
				.filter(u -> u.getUsername().equals(username))
				.map(User::getId)
				.findFirst()
				.orElseThrow();

		// When
		var newUsername = "newUsername";
		User update = User.builder()
				.id(id)
				.username(newUsername)
				.build();

		serviceUnderTest.updateUser(update);
		//Then
		Optional<User> actual = serviceUnderTest.selectUserById(id);
		assertThat(actual).isPresent().hasValueSatisfying(
				u ->{
					assertThat(u.getId()).isEqualTo(id);
					assertThat(u.getName()).isEqualTo(user.getName());
					assertThat(u.getUsername()).isEqualTo(newUsername);
					assertThat(u.getPassword()).isEqualTo(user.getPassword());
					assertThat(u.getPassword()).isEqualTo(user.getPassword());
					assertThat(u.getRole()).isEqualTo(user.getRole());
					assertThat(u.isAccountNonLocked()).isEqualTo(user.isAccountNonLocked());
				}
		);
	}

	@Test
	void shouldUpdateUserRole(){
		// Given
		String username = "username";
		User user = User.builder()
				.name("User")
				.username(username)
				.password("password")
				.role(Role.MERCHANT)
				.accountNonLocked(false)
				.build();
		serviceUnderTest.insertUser(user);

		long id = serviceUnderTest.selectAllUsers()
				.stream()
				.filter(u -> u.getUsername().equals(username))
				.map(User::getId)
				.findFirst()
				.orElseThrow();

		// When
		var newRole = Role.SUPPORT;
		User update = User.builder()
				.id(id)
				.role(newRole)
				.build();

		serviceUnderTest.updateUser(update);
		//Then
		Optional<User> actual = serviceUnderTest.selectUserById(id);
		assertThat(actual).isPresent().hasValueSatisfying(
				u ->{
					assertThat(u.getId()).isEqualTo(id);
					assertThat(u.getName()).isEqualTo(user.getName());
					assertThat(u.getUsername()).isEqualTo(user.getUsername());
					assertThat(u.getPassword()).isEqualTo(user.getPassword());
					assertThat(u.getPassword()).isEqualTo(user.getPassword());
					assertThat(u.getRole()).isEqualTo(newRole);
					assertThat(u.isAccountNonLocked()).isEqualTo(user.isAccountNonLocked());
				}
		);
	}

	@Test
	void shouldUpdateUserAccountNonLocked(){
		// Given
		String username = "username";
		User user = User.builder()
				.name("User")
				.username(username)
				.password("password")
				.role(Role.MERCHANT)
				.accountNonLocked(false)
				.build();
		serviceUnderTest.insertUser(user);

		long id = serviceUnderTest.selectAllUsers()
				.stream()
				.filter(u -> u.getUsername().equals(username))
				.map(User::getId)
				.findFirst()
				.orElseThrow();

		// When
		User update = User.builder()
				.id(id)
				.accountNonLocked(true)
				.build();

		serviceUnderTest.updateUser(update);
		//Then
		Optional<User> actual = serviceUnderTest.selectUserById(id);
		assertThat(actual).isPresent().hasValueSatisfying(
				u ->{
					assertThat(u.getId()).isEqualTo(id);
					assertThat(u.getName()).isEqualTo(user.getName());
					assertThat(u.getUsername()).isEqualTo(user.getUsername());
					assertThat(u.getPassword()).isEqualTo(user.getPassword());
					assertThat(u.getPassword()).isEqualTo(user.getPassword());
					assertThat(u.getRole()).isEqualTo(user.getRole());
					assertThat(u.isAccountNonLocked()).isEqualTo(true);
				}
		);
	}



}
