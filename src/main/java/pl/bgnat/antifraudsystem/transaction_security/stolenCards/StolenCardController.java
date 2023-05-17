package pl.bgnat.antifraudsystem.transaction_security.stolenCards;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
class StolenCardController {
	private final StolenCardService stolenCardService;

	 StolenCardController(StolenCardService stolenCardService) {
		this.stolenCardService = stolenCardService;
	}

	@PostMapping( "/api/antifraud/stolencard")
	ResponseEntity<StolenCard> addSuspiciousIp(@RequestBody StolenCardRequest stolenCardRequest){
		StolenCard addedStolenCard = stolenCardService.addStolenCard(stolenCardRequest);
		return new ResponseEntity<>(addedStolenCard, HttpStatus.CREATED);
	}

	@DeleteMapping("/api/antifraud/stolencard/{number}")
	ResponseEntity<StolenCardDeleteResponse> deleteSuspiciousIpByIpAddress(@PathVariable("number") String number){
		return ResponseEntity.ok(stolenCardService.deleteStolenCardByNumber(number));
	}

	@GetMapping("/api/antifraud/stolencard")
	ResponseEntity<List<StolenCard>> getAllSuspiciousIPs(){
		return ResponseEntity.ok(stolenCardService.getAllStolenCards());
	}
}
