package pl.bgnat.antifraudsystem.bank.transaction.domain;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.bgnat.antifraudsystem.bank.transaction.dto.TransactionRequest;
import pl.bgnat.antifraudsystem.bank.transaction.dto.TransactionResponse;

@RestController
@RequestMapping
class TransactionController {
	private final TransactionService transactionService;
	TransactionController(TransactionService transactionService) {
		this.transactionService = transactionService;
	}

	@PostMapping(value = "/api/antifraud/transaction", produces = "application/json")
	ResponseEntity<TransactionResponse> validTransaction(@RequestBody TransactionRequest transactionRequest) {
		TransactionResponse response = transactionService.transferTransaction(transactionRequest);
		return ResponseEntity.ok(response);

	}


}
