package pl.bgnat.antifraudsystem.transaction;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.bgnat.antifraudsystem.transaction.request.TransactionRequest;
import pl.bgnat.antifraudsystem.transaction.response.TransactionResponse;

@RestController
@RequestMapping
public class TransactionController {
	private final TransactionService transactionService;

	public TransactionController(TransactionService transactionService) {
		this.transactionService = transactionService;
	}

	@PostMapping(value = "/api/antifraud/transaction", produces = "application/json")
	public ResponseEntity<TransactionResponse> validTransaction(@RequestBody TransactionRequest transactionRequest) {
		TransactionResponse response = transactionService.validTransaction(transactionRequest);
		return ResponseEntity.ok(response);

	}


}
