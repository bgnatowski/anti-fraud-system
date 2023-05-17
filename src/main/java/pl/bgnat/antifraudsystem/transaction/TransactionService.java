package pl.bgnat.antifraudsystem.transaction;

import org.springframework.stereotype.Service;
import pl.bgnat.antifraudsystem.transaction.transaction_validation.TransactionValidatorChainFacade;

@Service
class TransactionService {
	private final TransactionValidatorChainFacade validatorChain;
	TransactionService(TransactionValidatorChainFacade validatorChain) {
		this.validatorChain = validatorChain;
	}
	TransactionResponse validTransaction(TransactionRequest transactionRequest){
		String info = validatorChain.valid(transactionRequest);
		Long amount = transactionRequest.amount();

		if (amount <= 200)
			return new TransactionResponse(TransactionStatus.ALLOWED, info);
		else if (amount <= 1500)
			return new TransactionResponse(TransactionStatus.MANUAL_PROCESSING, info);
		else
			return new TransactionResponse(TransactionStatus.PROHIBITED, info);
	}

}
