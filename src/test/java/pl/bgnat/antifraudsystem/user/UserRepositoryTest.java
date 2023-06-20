package pl.bgnat.antifraudsystem.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import pl.bgnat.antifraudsystem.AbstractTestcontainers;
import pl.bgnat.antifraudsystem.TestConfig;
import pl.bgnat.antifraudsystem.user.enums.Role;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({TestConfig.class})
public class UserRepositoryTest extends AbstractTestcontainers {
	@Autowired
	private UserRepository repositoryUnderTest;

	@Autowired
	private ApplicationContext applicationContext;

	private User user;

	@BeforeEach
	void setUp() {
		user = User.builder()
				.firstName("User")
				.lastName("LastName")
				.username("username")
				.password("password")
				.email("user@gmail.com")
				.role(Role.ADMINISTRATOR)
				.build();

		repositoryUnderTest.deleteAll();
		System.out.println(applicationContext.getBeanDefinitionCount());
	}

	@Test
	void existUserByUsername(){
		// Given
		String username = "username";

		repositoryUnderTest.save(user);

		// When
		var actual = repositoryUnderTest.existsUserByUsername(username);
		// Then
		assertThat(actual).isTrue();
	}

	@Test
	void existUserByUsernameShouldFailWhenUsernameIsNotFound(){
		// Given
		String username = "not_existing_username";

		// When
		var actual = repositoryUnderTest.existsUserByUsername(username);

		// Then
		assertThat(actual).isFalse();
	}

	@Test
	void existUserById(){
		// Given
		String username = "username";

		repositoryUnderTest.save(user);

		Long id = repositoryUnderTest.findAll()
				.stream()
				.filter(u -> u.getUsername().equals(username))
				.map(User::getId)
				.findFirst()
				.orElseThrow();
		// When
		var actual = repositoryUnderTest.existsById(id);
		// Then
		assertThat(actual).isTrue();
	}

	@Test
	void existsCustomerByIdFailsWhenIdNotFound() {
		// Given
		long id = -1L;

		// When
		var actual = repositoryUnderTest.existsById(id);

		// Then
		assertThat(actual).isFalse();
	}

	@Test
	void canFindUserByUsername(){
		String username = "username";

		repositoryUnderTest.save(user);

		// When
		var actual = repositoryUnderTest.findUserByUsername(username);
		// Then
		assertThat(actual).isNotEmpty();
		assertThat(actual.get()).isEqualTo(user);
	}

	@Test
	void cannotFindUserByUsername(){
		String username = "username";
		// When
		var actual = repositoryUnderTest.findUserByUsername(username);
		// Then
		assertThat(actual).isEmpty();
	}

	@Test
	void canDeleteUserByUsername(){
		String username = "username";
		repositoryUnderTest.save(user);
		// When
		repositoryUnderTest.deleteUserByUsername(username);
		// Then
		boolean actual = repositoryUnderTest.findAll()
				.stream()
				.noneMatch(u -> u.getUsername().equals(username));
		assertThat(actual).isTrue();
	}

}
