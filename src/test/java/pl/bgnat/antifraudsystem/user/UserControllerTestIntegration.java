package pl.bgnat.antifraudsystem.user;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.util.List;

import static io.restassured.RestAssured.given;

@Disabled
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserControllerTestIntegration {
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
	@Disabled
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

