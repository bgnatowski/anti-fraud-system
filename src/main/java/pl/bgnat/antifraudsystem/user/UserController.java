package pl.bgnat.antifraudsystem.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.bgnat.antifraudsystem.user.dto.*;
import pl.bgnat.antifraudsystem.user.dto.request.AddressRegisterRequest;
import pl.bgnat.antifraudsystem.user.dto.request.UserRegistrationRequest;
import pl.bgnat.antifraudsystem.user.dto.request.UserUnlockRequest;
import pl.bgnat.antifraudsystem.user.dto.request.UserUpdateRoleRequest;
import pl.bgnat.antifraudsystem.user.dto.response.UserDeleteResponse;
import pl.bgnat.antifraudsystem.user.dto.response.UserEmailConfirmedResponse;
import pl.bgnat.antifraudsystem.user.dto.response.UserUnlockResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/")
class UserController {
	private final AuthManager authManager;

	@PostMapping("/user")
	ResponseEntity<UserDTO> registerUser(@RequestBody UserRegistrationRequest user) {
		UserDTO registeredUser = authManager.registerUser(user);
		return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
	}

	@PatchMapping("/user/{username}/email/confirm/{code}")
	public ResponseEntity<UserEmailConfirmedResponse> confirmEmail(@PathVariable("username") String username,
																   @PathVariable("code") String code) {
		UserEmailConfirmedResponse confirmedResponse = authManager.confirmUserEmail(username, code);
		return new ResponseEntity<>(confirmedResponse, HttpStatus.OK);
	}

	@PatchMapping("/user/{username}/address/register")
	ResponseEntity<UserDTO> registerUserAddress(@RequestBody AddressRegisterRequest addressRegisterRequest,
										 @PathVariable("username") String username) {
		UserDTO registeredUserWithAddress = authManager.addUserAddress(username, addressRegisterRequest);
		return new ResponseEntity<>(registeredUserWithAddress, HttpStatus.CREATED);
	}

	@PatchMapping("/user/{username}/creditcard/create")
	ResponseEntity<UserDTO> createCreditCard(@PathVariable("username") String username) {
		UserDTO userWithCard = authManager.addCreditCardToUser(username);
		return new ResponseEntity<>(userWithCard, HttpStatus.CREATED);
	}

	@PatchMapping("/user/{username}/account/create")
	ResponseEntity<UserDTO> createAccount(@PathVariable("username") String username) {
		UserDTO userWithAccount = authManager.addAccountToUser(username);
		return new ResponseEntity<>(userWithAccount, HttpStatus.CREATED);
	}

	@GetMapping("/user/{username}")
	ResponseEntity<UserDTO> getUserDetails(@PathVariable("username") String username){
		UserDTO userDTO = authManager.getUserByUsername(username);
		return new ResponseEntity<>(userDTO, HttpStatus.OK);
	}

	@GetMapping("/list")
	ResponseEntity<List<UserDTO>> getAllRegisteredUsers(){
		List<UserDTO> allRegisteredUsers = authManager.getAllRegisteredUsers();
		return ResponseEntity.ok(allRegisteredUsers);
	}

	@DeleteMapping("/user/{username}")
	ResponseEntity<UserDeleteResponse> deleteUser(
			@PathVariable("username") String username){
		UserDeleteResponse userDeleteResponse = authManager.deleteUserByUsername(username);
		return ResponseEntity.ok(userDeleteResponse);
	}

	@PutMapping("/role")
	ResponseEntity<UserDTO> changeRole(@RequestBody UserUpdateRoleRequest updateRequest){
		UserDTO updatedUser = authManager.changeRole(updateRequest);
		return ResponseEntity.ok(updatedUser);
	}

	@PutMapping("/access")
	ResponseEntity<UserUnlockResponse> changeAccess(@RequestBody UserUnlockRequest updateRequest){
		UserUnlockResponse updatedUser = authManager.changeLock(updateRequest);
		return ResponseEntity.ok(updatedUser);
	}

}
