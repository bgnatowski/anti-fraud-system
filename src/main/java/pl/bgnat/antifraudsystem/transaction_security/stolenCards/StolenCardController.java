package pl.bgnat.antifraudsystem.transaction_security.stolenCards;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.bgnat.antifraudsystem.transaction_security.stolenCards.request.StolenCardRequest;
import pl.bgnat.antifraudsystem.transaction_security.stolenCards.response.StolenCardDeleteResponse;
import pl.bgnat.antifraudsystem.transaction_security.suspiciousIP.SuspiciousIP;
import pl.bgnat.antifraudsystem.transaction_security.suspiciousIP.response.SuspiciousIpDeleteResponse;

import java.util.List;

@RestController
public class StolenCardController {
	private final StolenCardService stolenCardService;

	public StolenCardController(StolenCardService stolenCardService) {
		this.stolenCardService = stolenCardService;
	}

	@PostMapping( "/api/antifraud/stolencard")
	public ResponseEntity<StolenCard> addSuspiciousIp(@RequestBody StolenCardRequest stolenCardRequest){
		StolenCard addedStolenCard = stolenCardService.addStolenCard(stolenCardRequest);
		return new ResponseEntity(addedStolenCard, HttpStatus.CREATED);
	}

	@DeleteMapping("/api/antifraud/stolencard/{number}")
	public ResponseEntity<StolenCardDeleteResponse> deleteSuspiciousIpByIpAddress(@PathVariable("number") String number){
		return ResponseEntity.ok(stolenCardService.deleteStolenCardByNumber(number));
	}

	@GetMapping("/api/antifraud/stolencard")
	public ResponseEntity<List<StolenCard>> getAllSuspiciousIPs(){
		return ResponseEntity.ok(stolenCardService.getAllStolenCards());
	}
}
