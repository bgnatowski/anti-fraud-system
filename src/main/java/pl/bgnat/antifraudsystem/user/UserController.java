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

	UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("/user")
	ResponseEntity<UserDTO> registerUser(@RequestBody UserRegistrationRequest user) {
		UserDTO registeredUser = userService.registerUser(user);
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
