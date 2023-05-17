package pl.bgnat.antifraudsystem.transaction.transaction_validation;

import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.transaction.TransactionRequest;

import java.util.ArrayList;
import java.util.List;

@Component
public class TransactionValidatorFacade {
	private final TransactionValidatorChain transactionValidatorChain;
	public TransactionValidatorFacade(TransactionValidatorChain transactionValidatorChain) {
		this.transactionValidatorChain = transactionValidatorChain;
	}

	public String valid(TransactionRequest transactionRequest) {
		TransactionValidator transactionValidator = transactionValidatorChain.getTransactionValidationFilterChain();
		List<String> info = transactionValidator.isValid(transactionRequest, new ArrayList<>());
		return info.isEmpty() ? "none" : getResultedInfoAsString(info);
	}

	private static String getResultedInfoAsString(List<String> info) {
		return String.join(", ", info.stream().sorted().toList());
	}
}