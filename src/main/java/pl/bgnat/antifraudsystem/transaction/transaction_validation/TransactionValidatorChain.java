package pl.bgnat.antifraudsystem.transaction.transaction_validation;

import org.springframework.stereotype.Component;

@Component
class TransactionValidatorChain {
	private final TransactionJsonFormatValidator jsonFormatValidator;
	private final TransactionCardNumberValidator cardNumberValidator;
	private final TransactionIpValidator ipValidator;
	private final TransactionAmountValidator amountValidator;

	TransactionValidatorChain(TransactionJsonFormatValidator jsonFormatValidator,
							  TransactionCardNumberValidator cardNumberValidator,
							  TransactionIpValidator ipValidator,
							  TransactionAmountValidator amountValidator) {
		this.jsonFormatValidator = jsonFormatValidator;
		this.cardNumberValidator = cardNumberValidator;
		this.ipValidator = ipValidator;
		this.amountValidator = amountValidator;
	}

	TransactionValidator getTransactionValidationFilterChain() {
		return AbstractValidator.link(
				jsonFormatValidator,
				cardNumberValidator,
				ipValidator,
				amountValidator
				);
	}
}
