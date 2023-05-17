package pl.bgnat.antifraudsystem.transaction;

import org.springframework.stereotype.Component;

@Component
 class TransactionValidatorChain {
	private final TransactionJsonFormatValidator jsonFormatValidator;
	private final TransactionAmountValidator amountValidator;
	private final TransactionIpValidator ipValidator;
	private final TransactionCardNumberValidator cardNumberValidator;

	public TransactionValidatorChain(TransactionJsonFormatValidator jsonFormatValidator,
									 TransactionAmountValidator amountValidator,
									 TransactionIpValidator ipValidator,
									 TransactionCardNumberValidator cardNumberValidator) {
		this.jsonFormatValidator = jsonFormatValidator;
		this.amountValidator = amountValidator;
		this.ipValidator = ipValidator;
		this.cardNumberValidator = cardNumberValidator;
	}

	public TransactionValidator getTransactionValidationFilterChain() {
		return Validator.link(
				jsonFormatValidator,
				amountValidator,
				ipValidator,
				cardNumberValidator);
	}
}
