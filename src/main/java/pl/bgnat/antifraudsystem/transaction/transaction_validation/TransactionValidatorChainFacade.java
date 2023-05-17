package pl.bgnat.antifraudsystem.transaction.transaction_validation;

import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.transaction.TransactionRequest;

import java.util.ArrayList;
import java.util.List;

@Component
public class TransactionValidatorChainFacade {
	private final TransactionJsonFormatValidator jsonFormatValidator;
	private final TransactionAmountValidator amountValidator;
	private final TransactionIpValidator ipValidator;
	private final TransactionCardNumberValidator cardNumberValidator;

	public TransactionValidatorChainFacade(TransactionJsonFormatValidator jsonFormatValidator,
										   TransactionAmountValidator amountValidator,
										   TransactionIpValidator ipValidator,
										   TransactionCardNumberValidator cardNumberValidator) {
		this.jsonFormatValidator = jsonFormatValidator;
		this.amountValidator = amountValidator;
		this.ipValidator = ipValidator;
		this.cardNumberValidator = cardNumberValidator;
	}

	public String valid(TransactionRequest transactionRequest) {
		List<String> info = getTransactionValidationFilterChain().isValid(transactionRequest, new ArrayList<>());
		return info.isEmpty() ? "none" : getResultedInfoAsString(info);
	}
	private TransactionValidator getTransactionValidationFilterChain() {
		return AbstractValidator.link(
				jsonFormatValidator,
				amountValidator,
				ipValidator,
				cardNumberValidator);
	}
	private static String getResultedInfoAsString(List<String> info) {
		return String.join(", ", info.stream().sorted().toList());
	}
}
