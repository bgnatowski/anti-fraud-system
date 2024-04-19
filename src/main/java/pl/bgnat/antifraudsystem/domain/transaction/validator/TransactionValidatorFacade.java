package pl.bgnat.antifraudsystem.domain.transaction.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.domain.request.TransactionRequest;
import pl.bgnat.antifraudsystem.domain.response.TransactionResponse;
import pl.bgnat.antifraudsystem.domain.transaction.TransactionStatus;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TransactionValidatorFacade {
	private final TransactionValidatorChain transactionValidatorChain;
	private final StatusValidatorChain statusValidatorChain;

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
