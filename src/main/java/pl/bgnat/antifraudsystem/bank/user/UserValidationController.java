package pl.bgnat.antifraudsystem.bank.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.bgnat.antifraudsystem.bank.HttpResponse;
import pl.bgnat.antifraudsystem.bank.user.domain.UserFacade;
import pl.bgnat.antifraudsystem.bank.user.dto.request.ConfirmEmailRequest;
import pl.bgnat.antifraudsystem.bank.user.dto.response.UserEmailConfirmedResponse;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/")
class UserValidationController {
	private final UserFacade userManager;
	private final Clock clock;

	@PatchMapping("/user/email/confirm") // all - kazdy moze potwierdzać email
	public ResponseEntity<HttpResponse> confirmEmail(@RequestBody ConfirmEmailRequest confirmEmailRequest) {
		UserEmailConfirmedResponse confirmedResponse = userManager.confirmUserEmail(confirmEmailRequest);
		return ResponseEntity.ok().body(
				HttpResponse.builder()
						.timeStamp(LocalDateTime.now(clock).toString())
						.data(Map.of("confirmedResponse", confirmedResponse))
						.message(confirmedResponse.message())
						.status(HttpStatus.OK)
						.statusCode(HttpStatus.OK.value())
						.build()
		);
	}
}
