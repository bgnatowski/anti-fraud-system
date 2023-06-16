package pl.bgnat.antifraudsystem.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.bgnat.antifraudsystem.user.dto.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth/")
class UserController {
	private final UserService userService;
	private final CreditCardService creditCardService;
	private final AccountService accountService;

	UserController(UserService userService, CreditCardService creditCardService, AccountService accountService) {
		this.userService = userService;
		this.creditCardService = creditCardService;
		this.accountService = accountService;
	}

	@PostMapping("/user")
	ResponseEntity<UserDTO> registerUser(@RequestBody UserRegistrationRequest user,
										 @RequestBody PhoneNumberRegisterRequest phone,
										 @RequestBody AddressRegisterRequest address) {
		UserDTO registeredUser = userService.registerUser(user, phone, address);
		return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
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
