package pl.bgnat.antifraudsystem.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.bgnat.antifraudsystem.dto.HttpResponse;
import pl.bgnat.antifraudsystem.user.dto.CreditCardDeleteRequest;
import pl.bgnat.antifraudsystem.user.dto.CreditCardDeleteResponse;
import pl.bgnat.antifraudsystem.user.dto.CreditCardRestrictRequest;
import pl.bgnat.antifraudsystem.user.dto.CreditCardRestrictResponse;
import pl.bgnat.antifraudsystem.user.dto.request.CreditCardActivationRequest;
import pl.bgnat.antifraudsystem.user.dto.request.CreditCardChangePinRequest;
import pl.bgnat.antifraudsystem.user.dto.response.CreditCardActivationResponse;
import pl.bgnat.antifraudsystem.user.dto.response.CreditCardChangePinResponse;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/")
class CreditCardController {
	private final CreditCardService creditCardService;
	private final Clock clock;
	@PatchMapping("/credit-card/active")
	ResponseEntity<HttpResponse> activeCreditCard(@RequestBody CreditCardActivationRequest creditCardActiveRequest) {
		CreditCardActivationResponse creditCardActivationResponse = creditCardService.activeCreditCard(creditCardActiveRequest);
		return ResponseEntity.ok().body(
				HttpResponse.builder()
						.timeStamp(LocalDateTime.now(clock).toString())
						.data(Map.of("creditCardActivationResponse", creditCardActivationResponse))
						.message(creditCardActivationResponse.message())
						.status(HttpStatus.OK)
						.statusCode(HttpStatus.OK.value())
						.build()
		);
	}

	@PatchMapping("/credit-card/changePin")
	ResponseEntity<HttpResponse> changePin(@RequestBody CreditCardChangePinRequest creditCardActiveRequest) {
		CreditCardChangePinResponse creditCardChangePinResponse = creditCardService.changePin(creditCardActiveRequest);
		return ResponseEntity.ok().body(
				HttpResponse.builder()
						.timeStamp(LocalDateTime.now(clock).toString())
						.data(Map.of("creditCardActivationResponse", creditCardChangePinResponse))
						.message(creditCardChangePinResponse.message())
						.status(HttpStatus.OK)
						.statusCode(HttpStatus.OK.value())
						.build()
		);
	}

	@PatchMapping("/credit-card/delete")
	ResponseEntity<HttpResponse> restrict(@RequestBody CreditCardRestrictRequest creditCardRestrictRequest) {
		CreditCardRestrictResponse creditCardRestrictResponse = creditCardService.restrict(creditCardRestrictRequest);
		return ResponseEntity.ok().body(
				HttpResponse.builder()
						.timeStamp(LocalDateTime.now(clock).toString())
						.data(Map.of("creditCardRestrictResponse", creditCardRestrictResponse))
						.message(creditCardRestrictResponse.message())
						.status(HttpStatus.OK)
						.statusCode(HttpStatus.OK.value())
						.build()
		);
	}

	@DeleteMapping("/credit-card/delete")
	ResponseEntity<HttpResponse> delete(@RequestBody CreditCardDeleteRequest creditCardDeleteRequest) {
		CreditCardDeleteResponse creditCardDeleteResponse =  creditCardService.delete(creditCardDeleteRequest);
		return ResponseEntity.ok().body(
				HttpResponse.builder()
						.timeStamp(LocalDateTime.now(clock).toString())
						.data(Map.of("creditCardDeleteResponse", creditCardDeleteResponse))
						.message(creditCardDeleteResponse.message())
						.status(HttpStatus.OK)
						.statusCode(HttpStatus.OK.value())
						.build()
		);
	}

}
