package pl.bgnat.antifraudsystem.transaction;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.bgnat.antifraudsystem.exception.RequestValidationException;

@RestController
@RequestMapping("/api/antifraud/transaction")
public class TransactionController {
	private final TransactionService transactionService;

	public TransactionController(TransactionService transactionService) {
		this.transactionService = transactionService;
	}

	@PostMapping(produces = "application/json")
	public ResponseEntity<TransactionResponse> validTransaction(@RequestBody TransactionRequest request) {
		Long amount = request.amount();
		if(amount == null || amount <= 0)
			throw new RequestValidationException("Wrong request!");
		else {
			TransactionResponse response = transactionService.validTransaction(amount);
			return ResponseEntity.ok(response);
		}
	}
}
