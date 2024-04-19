package pl.bgnat.antifraudsystem.domain.transaction;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.bgnat.antifraudsystem.domain.request.TransactionRequest;
import pl.bgnat.antifraudsystem.domain.response.TransactionResponse;

@RestController
@RequestMapping("/api/antifraud/transaction")
@RequiredArgsConstructor
class TransactionController {
	private final TransactionService transactionService;
	@PostMapping(produces = "application/json")
	ResponseEntity<TransactionResponse> validTransaction(@Valid @RequestBody TransactionRequest transactionRequest) {
		TransactionResponse response = transactionService.transferTransaction(transactionRequest);
		return ResponseEntity.ok(response);

	}
}
