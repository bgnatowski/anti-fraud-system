package pl.bgnat.antifraudsystem.bank.transaction.stolenCards;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.bgnat.antifraudsystem.bank.transaction.stolenCards.dto.StolenCardDeleteResponse;
import pl.bgnat.antifraudsystem.bank.transaction.stolenCards.dto.StolenCardRequest;

import java.util.List;

@RestController
@RequestMapping("/api/antifraud/stolencard")
class StolenCardController {
	private final StolenCardService stolenCardService;

	 StolenCardController(StolenCardService stolenCardService) {
		this.stolenCardService = stolenCardService;
	}

	@PostMapping
	ResponseEntity<StolenCard> addSuspiciousIp(@RequestBody StolenCardRequest stolenCardRequest){
		StolenCard addedStolenCard = stolenCardService.addStolenCard(stolenCardRequest);
		return ResponseEntity.ok(addedStolenCard);
	}

	@DeleteMapping("/{number}")
	ResponseEntity<StolenCardDeleteResponse> deleteSuspiciousIpByIpAddress(@PathVariable("number") String number){
		return ResponseEntity.ok(stolenCardService.deleteStolenCardByNumber(number));
	}

	@GetMapping
	ResponseEntity<List<StolenCard>> getAllSuspiciousIPs(){
		return ResponseEntity.ok(stolenCardService.getAllStolenCards());
	}
}
