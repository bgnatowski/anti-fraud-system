package pl.bgnat.antifraudsystem.bank.user;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.bgnat.antifraudsystem.bank.HttpResponse;
import pl.bgnat.antifraudsystem.bank.user.domain.UserFacade;
import pl.bgnat.antifraudsystem.bank.user.dto.UserDTO;
import pl.bgnat.antifraudsystem.bank.user.dto.request.UserUnlockRequest;
import pl.bgnat.antifraudsystem.bank.user.dto.request.UserUpdateRoleRequest;
import pl.bgnat.antifraudsystem.bank.user.dto.response.UserUnlockResponse;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/")
class UserActionController {
	private final UserFacade userManager;
	private final Clock clock;
	@PutMapping("/user/role") // manualna zmiana roli przez admina
	ResponseEntity<HttpResponse> changeRole(@RequestBody UserUpdateRoleRequest updateRequest) {
		UserDTO updatedUser = userManager.changeRole(updateRequest);
		return ResponseEntity.ok().body(
				HttpResponse.builder()
						.timeStamp(LocalDateTime.now(clock).toString())
						.data(Map.of("updatedUser", updatedUser))
						.message("User role updated")
						.status(HttpStatus.OK)
						.statusCode(HttpStatus.OK.value())
						.build()
		);
	}

	@PutMapping("/user/access") //manualne zablokowanie/odblokowanie konta przez admina/supporta
	ResponseEntity<HttpResponse> changeAccess(@RequestBody UserUnlockRequest updateRequest) {
		UserUnlockResponse updatedUser = userManager.changeLock(updateRequest);
		return ResponseEntity.ok().body(
				HttpResponse.builder()
						.timeStamp(LocalDateTime.now(clock).toString())
						.data(Map.of("updatedUser", updatedUser))
						.message("User access updated")
						.status(HttpStatus.OK)
						.statusCode(HttpStatus.OK.value())
						.build()
		);
	}
}
