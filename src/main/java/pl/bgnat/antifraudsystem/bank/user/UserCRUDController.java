package pl.bgnat.antifraudsystem.bank.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.bgnat.antifraudsystem.bank.HttpResponse;
import pl.bgnat.antifraudsystem.bank.user.domain.UserFacade;
import pl.bgnat.antifraudsystem.bank.user.dto.UserDTO;
import pl.bgnat.antifraudsystem.bank.user.dto.request.*;
import pl.bgnat.antifraudsystem.bank.user.dto.response.UserDeleteResponse;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/")
class UserCRUDController {
	private final UserFacade userManager;
	private final Clock clock;
	@GetMapping("/users/list") // admin/support - kiedy chce sprawdzić czy np jest takie konto -> potem sobie filtruje
	ResponseEntity<HttpResponse> getAllRegisteredUsers() {
		List<UserDTO> allRegisteredUsers = userManager.getAllRegisteredUsers();

		return ResponseEntity.ok().body(
				HttpResponse.builder()
						.timeStamp(LocalDateTime.now(clock).toString())
						.data(Map.of("allRegisteredUsers", allRegisteredUsers))
						.message("Registered Users Retrieved")
						.status(HttpStatus.OK)
						.statusCode(HttpStatus.OK.value())
						.build()
		);
	}

	@PostMapping("/user") // all - rejestracja każdego
	ResponseEntity<HttpResponse> registerUser(@RequestBody UserRegistrationRequest user) {
		UserDTO registeredUser = userManager.registerUser(user);

		return new ResponseEntity<>(
				HttpResponse.builder()
						.timeStamp(LocalDateTime.now(clock).toString())
						.data(Map.of("registeredUser", registeredUser))
						.message("User Registered")
						.status(HttpStatus.CREATED)
						.statusCode(HttpStatus.CREATED.value())
						.build(),
				HttpStatus.CREATED);
	}

	@PatchMapping("/user/{username}/address/register") // merchant -  admin/support nie musi miec przypisanego
	ResponseEntity<HttpResponse> registerUserAddress(@RequestBody AddressRegisterRequest addressRegisterRequest,
													 @PathVariable("username") String username) {
		UserDTO registeredUserWithAddress = userManager.addUserAddress(username, addressRegisterRequest);
		return new ResponseEntity<>(
				HttpResponse.builder()
						.timeStamp(LocalDateTime.now(clock).toString())
						.data(Map.of("registeredUserWithAddress", registeredUserWithAddress))
						.message("User address added")
						.status(HttpStatus.CREATED)
						.statusCode(HttpStatus.CREATED.value())
						.build(),
				HttpStatus.CREATED);
	}

	@PatchMapping("/user/{username}/credit-card/create") //todo to account controller to moze przeniesc do konta
	ResponseEntity<HttpResponse> createCreditCard(@PathVariable("username") String username) {
		UserDTO userWithCard = userManager.createCreditCardForUserWithUsername(username);
		return new ResponseEntity<>(
				HttpResponse.builder()
						.timeStamp(LocalDateTime.now(clock).toString())
						.data(Map.of("userWithCard", userWithCard))
						.message("Created credit card for user")
						.status(HttpStatus.CREATED)
						.statusCode(HttpStatus.CREATED.value())
						.build(),
				HttpStatus.CREATED);
	}

	@PatchMapping("/user/{username}/account/create")
	ResponseEntity<HttpResponse> createAccount(@PathVariable("username") String username) {
		UserDTO userWithAccount = userManager.createAccountForUserWithUsername(username);
		return new ResponseEntity<>(
				HttpResponse.builder()
						.timeStamp(LocalDateTime.now(clock).toString())
						.data(Map.of("userWithAccount", userWithAccount))
						.message("Created account for user")
						.status(HttpStatus.CREATED)
						.statusCode(HttpStatus.CREATED.value())
						.build(),
				HttpStatus.CREATED);
	}

	@GetMapping("/user/{username}/details")
	ResponseEntity<HttpResponse> getUserDetails(@PathVariable("username") String username) {
		UserDTO userDTO = userManager.getUserByUsername(username);
		return ResponseEntity.ok().body(
				HttpResponse.builder()
						.timeStamp(LocalDateTime.now(clock).toString())
						.data(Map.of("userDTO", userDTO))
						.message("User details retrieved")
						.status(HttpStatus.OK)
						.statusCode(HttpStatus.OK.value())
						.build()
		);
	}

	@DeleteMapping("/user/{username}/delete")
	ResponseEntity<HttpResponse> deleteUser(@PathVariable("username") String username) {
		UserDeleteResponse userDeleteResponse = userManager.deleteUserByUsername(username);
		return ResponseEntity.ok().body(
				HttpResponse.builder()
						.timeStamp(LocalDateTime.now(clock).toString())
						.data(Map.of("userDeleteResponse", userDeleteResponse))
						.message("User deleted")
						.status(HttpStatus.OK)
						.statusCode(HttpStatus.OK.value())
						.build()
		);
	}



}
