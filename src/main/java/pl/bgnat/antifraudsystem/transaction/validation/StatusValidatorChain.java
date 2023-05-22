package pl.bgnat.antifraudsystem.transaction.validation;

import org.springframework.stereotype.Component;

@Component
class StatusValidatorChain {
	private final PreStatusValidator preStatusValidator;
	private final StatusAmountValidator amountValidator;
	private final StatusRegionValidator regionValidator;
	private final StatusUniqueIpValidator uniqueIpValidator;

	StatusValidatorChain(PreStatusValidator preStatusValidator, StatusAmountValidator amountValidator,
						 StatusRegionValidator regionValidator,
						 StatusUniqueIpValidator uniqueIpValidator) {
		this.preStatusValidator = preStatusValidator;
		this.amountValidator = amountValidator;
		this.regionValidator = regionValidator;
		this.uniqueIpValidator = uniqueIpValidator;
	}

	StatusValidator getStatusValidatorChain() {
		return AbstractStatusValidator.link(
				preStatusValidator,
				regionValidator,
				uniqueIpValidator,
				amountValidator);
	}
}
