package pl.bgnat.antifraudsystem.user;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import pl.bgnat.antifraudsystem.AbstractTestcontainers;
import pl.bgnat.antifraudsystem.TestConfig;

import java.util.List;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserControllerTest extends AbstractTestcontainers {
	private final String transactionApi = "/api/antifraud/transaction";
	private final String userApi = "/api/auth/user";
	private final String userListApi = "/api/auth/list";
	private final String lockApi = "/api/auth/access";
	private final String roleApi = "/api/auth/role";
	@LocalServerPort
	private int port;

	@Autowired
	UserRepository userRepository;

	@BeforeEach
	void setUp() {
		RestAssured.baseURI = "http://localhost:" + port;
		userRepository.deleteAll();
	}

	@Test
	public void shouldReturn401UnauthorizedWhenGetOnUserListApiWithoutAuthorization() {
		given()
				.contentType(ContentType.JSON)
				.when()
				.get(userListApi)
				.then()
				.statusCode(HttpStatus.UNAUTHORIZED.value());
	}

	@Test
	public void shouldReturnListOfAllRegisteredUsersWhenCalledByAdministrator(){
		// Given
		List<User> users = List.of(
				new User("Admin", "admin", "password", Role.ADMINISTRATOR, true),
				new User("John Doe", "johndoe", "password", Role.MERCHANT, false),
				new User("John Doe2", "johndoe2", "password", Role.MERCHANT, false),
				new User("John Doe3", "johndoe3", "password", Role.MERCHANT, false)
		);
		userRepository.saveAll(users);

		// przygotowanie nagłówka z autoryzacją


		given()
				.auth()
				.basic("admin", "password")
				.header("Accept", ContentType.JSON.getAcceptHeader())
				.contentType(ContentType.JSON)
				.when()
				.get(userListApi)
				.then()
				.statusCode(HttpStatus.OK.value());
	}

}

