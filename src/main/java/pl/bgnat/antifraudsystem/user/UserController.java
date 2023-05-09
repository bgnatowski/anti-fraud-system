package pl.bgnat.antifraudsystem.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.bgnat.antifraudsystem.user.request.UserRegistrationRequest;
import pl.bgnat.antifraudsystem.user.request.UserRoleUpdateRequest;
import pl.bgnat.antifraudsystem.user.request.UserUnlockRequest;
import pl.bgnat.antifraudsystem.user.response.UserDeleteResponse;
import pl.bgnat.antifraudsystem.user.response.UserUnlockResponse;

import java.util.List;

@RestController
@RequestMapping("/api/auth/")
public class UserController {
	private final MyUserDetailService userDetailService;

	public UserController(MyUserDetailService userDetailService) {
		this.userDetailService = userDetailService;
	}

	@PostMapping("/user")
	public ResponseEntity<UserDTO> registerUser(@RequestBody UserRegistrationRequest user) {
		UserDTO registeredUser = userDetailService.registerUser(user);
		return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
	}

	@GetMapping("/list")
	public ResponseEntity<List<UserDTO>> getAllRegisteredUsers(){
		List<UserDTO> allRegisteredUsers = userDetailService.getAllRegisteredUsers();
		return ResponseEntity.ok(allRegisteredUsers);
	}

	@DeleteMapping("/user/{username}")
	public ResponseEntity<UserDeleteResponse> deleteUser(
			@PathVariable("username") String username){
		UserDeleteResponse userDeleteResponse = userDetailService.deleteUserByUsername(username);
		return ResponseEntity.ok(userDeleteResponse);
	}

	@PutMapping("/role")
	public ResponseEntity<UserDTO> changeRole(@RequestBody UserRoleUpdateRequest updateRequest){
		UserDTO updatedUser = userDetailService.changeRole(updateRequest);
		return ResponseEntity.ok(updatedUser);
	}

	@PutMapping("/access")
	public ResponseEntity<UserUnlockResponse> changeRole(@RequestBody UserUnlockRequest updateRequest){
		UserUnlockResponse updatedUser = userDetailService.changeLock(updateRequest);
		return ResponseEntity.ok(updatedUser);
	}

}
