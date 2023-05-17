package pl.bgnat.antifraudsystem.transaction;

import org.springframework.stereotype.Service;
import pl.bgnat.antifraudsystem.transaction.transaction_validation.TransactionValidatorFacade;

@Service
class TransactionService {
	public static final int MAX_AMOUNT_FOR_MANUAL_PROCESSING = 1500;
	private final TransactionValidatorFacade validatorChainFacade;

	TransactionService(TransactionValidatorFacade validatorChainFacade) {
		this.validatorChainFacade = validatorChainFacade;
	}

	TransactionResponse validTransaction(TransactionRequest transactionRequest) {
		String info = validatorChainFacade.valid(transactionRequest);

		boolean isManualProcessing = transactionRequest.amount() <= MAX_AMOUNT_FOR_MANUAL_PROCESSING;
		TransactionStatus status = switch (info) {
			case "none" -> TransactionStatus.ALLOWED;
			case "amount" -> isManualProcessing ? TransactionStatus.MANUAL_PROCESSING : TransactionStatus.PROHIBITED;
			default -> TransactionStatus.PROHIBITED;
		};
		return new TransactionResponse(status, info);
	}
}
