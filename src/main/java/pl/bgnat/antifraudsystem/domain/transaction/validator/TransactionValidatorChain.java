package pl.bgnat.antifraudsystem.domain.transaction.validator;

import org.springframework.stereotype.Component;

@Component
class TransactionValidatorChain {
	private final TransactionJsonFormatValidator jsonFormatValidator;
	private final TransactionAmountValidator amountValidator;
	private final TransactionIpValidator ipValidator;
	private final TransactionCardNumberValidator cardNumberValidator;
	private final TransactionDateFormatValidator dateFormatValidator;
	private final TransactionRegionFormatValidator regionFormatValidator;
	TransactionValidatorChain(TransactionJsonFormatValidator jsonFormatValidator,
							  TransactionAmountValidator amountValidator,
							  TransactionIpValidator ipValidator,
							  TransactionCardNumberValidator cardNumberValidator, TransactionDateFormatValidator dateFormatValidator, TransactionRegionFormatValidator regionFormatValidator) {
		this.jsonFormatValidator = jsonFormatValidator;
		this.amountValidator = amountValidator;
		this.ipValidator = ipValidator;
		this.cardNumberValidator = cardNumberValidator;
		this.dateFormatValidator = dateFormatValidator;
		this.regionFormatValidator = regionFormatValidator;
	}

	TransactionValidator getTransactionValidationFilterChain() {
		return AbstractTransactionValidator.link(
				jsonFormatValidator,
				regionFormatValidator,
				dateFormatValidator,
				cardNumberValidator,
				ipValidator,
				amountValidator);
	}
}
