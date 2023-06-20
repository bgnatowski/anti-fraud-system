package pl.bgnat.antifraudsystem.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.bgnat.antifraudsystem.user.dto.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth/")
class UserController {
	private final UserService userService;
	private final CreditCardService creditCardService;
	private final AccountService accountService;

	UserController(UserService userService,
				   CreditCardService creditCardService,
				   AccountService accountService) {
		this.userService = userService;
		this.creditCardService = creditCardService;
		this.accountService = accountService;
	}

	@PostMapping("/user")
	ResponseEntity<UserDTO> registerUser(@RequestBody UserRegistrationRequest user) {
		UserDTO registeredUser = userService.registerUser(user);
		return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
	}

	@PatchMapping("/user/{username}/email/confirm/{code}")
	public ResponseEntity<UserDTO> confirmEmail(@PathVariable("username") String username,
												@PathVariable("code") String code) {
		UserDTO confirmedUser = userService.confirmUserEmail(username, code);
		return new ResponseEntity<>(confirmedUser, HttpStatus.OK);
	}

	@PatchMapping("/user/{username}/address/register")
	ResponseEntity<UserDTO> registerUserAddress(@RequestBody AddressRegisterRequest addressRegisterRequest,
										 @PathVariable("username") String username) {
		UserDTO registeredUserWithAddress = userService.addUserAddress(username, addressRegisterRequest);
		return new ResponseEntity<>(registeredUserWithAddress, HttpStatus.CREATED);
	}

	@PatchMapping("/user/{username}/creditcard/create")
	ResponseEntity<UserDTO> createCreditCard(@PathVariable("username") String username) {
		CreditCard newCreditCard = creditCardService.createCreditCard();
		UserDTO userWithCard = userService.addCreditCardToUser(username, newCreditCard);
		return new ResponseEntity<>(userWithCard, HttpStatus.CREATED);
	}

	@PatchMapping("/user/{username}/account/create")
	ResponseEntity<UserDTO> createAccount(@PathVariable("username") String username) {
		UserDTO existingUserWithoutAccount = userService.getUserByUsername(username);
		Account newAccount = accountService.createAccount(existingUserWithoutAccount);
		UserDTO userWithAccount = userService.addAccountToUser(username, newAccount);
		return new ResponseEntity<>(userWithAccount, HttpStatus.CREATED);
	}

	@GetMapping("/user/{username}")
	ResponseEntity<UserDTO> getUserDetails(@PathVariable("username") String username){
		UserDTO userDTO = userService.getUserByUsername(username);
		return new ResponseEntity<>(userDTO, HttpStatus.OK);
	}

	@GetMapping("/list")
	ResponseEntity<List<UserDTO>> getAllRegisteredUsers(){
		List<UserDTO> allRegisteredUsers = userService.getAllRegisteredUsers();
		return ResponseEntity.ok(allRegisteredUsers);
	}

	@DeleteMapping("/user/{username}")
	ResponseEntity<UserDeleteResponse> deleteUser(
			@PathVariable("username") String username){
		UserDeleteResponse userDeleteResponse = userService.deleteUserByUsername(username);
		return ResponseEntity.ok(userDeleteResponse);
	}

	@PutMapping("/role")
	ResponseEntity<UserDTO> changeRole(@RequestBody UserUpdateRoleRequest updateRequest){
		UserDTO updatedUser = userService.changeRole(updateRequest);
		return ResponseEntity.ok(updatedUser);
	}

	@PutMapping("/access")
	ResponseEntity<UserUnlockResponse> changeAccess(@RequestBody UserUnlockRequest updateRequest){
		UserUnlockResponse updatedUser = userService.changeLock(updateRequest);
		return ResponseEntity.ok(updatedUser);
	}

}
