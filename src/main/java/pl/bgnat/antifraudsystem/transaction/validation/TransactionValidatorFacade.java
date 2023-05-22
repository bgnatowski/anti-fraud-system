package pl.bgnat.antifraudsystem.transaction.validation;

import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.transaction.dto.TransactionRequest;
import pl.bgnat.antifraudsystem.transaction.dto.TransactionResponse;
import pl.bgnat.antifraudsystem.transaction.dto.TransactionStatus;

import java.util.ArrayList;
import java.util.List;

@Component
public class TransactionValidatorFacade {
	private final TransactionValidatorChain transactionValidatorChain;
	private final StatusValidatorChain statusValidatorChain;

	public TransactionValidatorFacade(TransactionValidatorChain transactionValidatorChain, StatusValidatorChain statusValidatorChain) {
		this.transactionValidatorChain = transactionValidatorChain;
		this.statusValidatorChain = statusValidatorChain;
	}

	public TransactionResponse valid(TransactionRequest transactionRequest) {
		TransactionValidator transactionValidator = transactionValidatorChain.getTransactionValidationFilterChain();
		List<String> info = transactionValidator.valid(transactionRequest, new ArrayList<>());

		StatusValidator statusValidator= statusValidatorChain.getStatusValidatorChain();
		TransactionStatus status = statusValidator.valid(transactionRequest, info);

		return new TransactionResponse(status, getResultedInfoAsString(info));
	}

	private static String getResultedInfoAsString(List<String> info) {
		return String.join(", ", info.stream().sorted().toList());
	}
}
