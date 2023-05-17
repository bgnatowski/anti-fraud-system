package pl.bgnat.antifraudsystem.transaction.transaction_validation;

import org.springframework.stereotype.Component;

@Component
class TransactionValidatorChain {
	private final TransactionJsonFormatValidator jsonFormatValidator;
	private final TransactionAmountValidator amountValidator;
	private final TransactionIpValidator ipValidator;
	private final TransactionCardNumberValidator cardNumberValidator;

	TransactionValidatorChain(TransactionJsonFormatValidator jsonFormatValidator,
									 TransactionAmountValidator amountValidator,
									 TransactionIpValidator ipValidator,
									 TransactionCardNumberValidator cardNumberValidator) {
		this.jsonFormatValidator = jsonFormatValidator;
		this.amountValidator = amountValidator;
		this.ipValidator = ipValidator;
		this.cardNumberValidator = cardNumberValidator;
	}

	TransactionValidator getTransactionValidationFilterChain() {
		return AbstractValidator.link(
				jsonFormatValidator,
				amountValidator,
				ipValidator,
				cardNumberValidator);
	}
}
