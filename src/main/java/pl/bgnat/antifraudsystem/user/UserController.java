package pl.bgnat.antifraudsystem.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.bgnat.antifraudsystem.user.dto.*;
import pl.bgnat.antifraudsystem.user.dto.request.*;
import pl.bgnat.antifraudsystem.user.dto.response.UserDeleteResponse;
import pl.bgnat.antifraudsystem.user.dto.response.UserEmailConfirmedResponse;
import pl.bgnat.antifraudsystem.user.dto.response.UserUnlockResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/")
class UserController {
	private final UserManager userManager;

	@GetMapping("/users/list")
	ResponseEntity<List<UserDTO>> getAllRegisteredUsers(){
		List<UserDTO> allRegisteredUsers = userManager.getAllRegisteredUsers();
		return ResponseEntity.ok(allRegisteredUsers);
	}

	@PostMapping("/user")
	ResponseEntity<UserDTO> registerUser(@RequestBody UserRegistrationRequest user) {
		UserDTO registeredUser = userManager.registerUser(user);
		return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
	}

	@PatchMapping("/user/email/confirm/")
	public ResponseEntity<UserEmailConfirmedResponse> confirmEmail(@RequestBody ConfirmEmailRequest confirmEmailRequest) {
		UserEmailConfirmedResponse confirmedResponse = userManager.confirmUserEmail(confirmEmailRequest);
		return new ResponseEntity<>(confirmedResponse, HttpStatus.OK);
	}

	@PatchMapping("/user/{username}/address/register")
	ResponseEntity<UserDTO> registerUserAddress(@RequestBody AddressRegisterRequest addressRegisterRequest,
										 @PathVariable("username") String username) {
		UserDTO registeredUserWithAddress = userManager.addUserAddress(username, addressRegisterRequest);
		return new ResponseEntity<>(registeredUserWithAddress, HttpStatus.CREATED);
	}

	@PatchMapping("/user/{username}/creditcard/create")
	ResponseEntity<UserDTO> createCreditCard(@PathVariable("username") String username) {
		UserDTO userWithCard = userManager.createCreditCardForUserWithUsername(username);
		return new ResponseEntity<>(userWithCard, HttpStatus.CREATED);
	}

	@PatchMapping("/user/{username}/account/create")
	ResponseEntity<UserDTO> createAccount(@PathVariable("username") String username) {
		UserDTO userWithAccount = userManager.createAccountForUserWithUsename(username);
		return new ResponseEntity<>(userWithAccount, HttpStatus.CREATED);
	}

	@GetMapping("/user/{username}/details")
	ResponseEntity<UserDTO> getUserDetails(@PathVariable("username") String username){
		UserDTO userDTO = userManager.getUserByUsername(username);
		return new ResponseEntity<>(userDTO, HttpStatus.OK);
	}

	@DeleteMapping("/user/{username}/delete")
	ResponseEntity<UserDeleteResponse> deleteUser(
			@PathVariable("username") String username){
		UserDeleteResponse userDeleteResponse = userManager.deleteUserByUsername(username);
		return ResponseEntity.ok(userDeleteResponse);
	}

	@PutMapping("/user/role")
	ResponseEntity<UserDTO> changeRole(@RequestBody UserUpdateRoleRequest updateRequest){
		UserDTO updatedUser = userManager.changeRole(updateRequest);
		return ResponseEntity.ok(updatedUser);
	}

	@PutMapping("/user/access")
	ResponseEntity<UserUnlockResponse> changeAccess(@RequestBody UserUnlockRequest updateRequest){
		UserUnlockResponse updatedUser = userManager.changeLock(updateRequest);
		return ResponseEntity.ok(updatedUser);
	}

}
