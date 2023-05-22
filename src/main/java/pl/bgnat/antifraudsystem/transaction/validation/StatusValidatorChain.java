package pl.bgnat.antifraudsystem.transaction.validation;

import org.springframework.stereotype.Component;

@Component
class StatusValidatorChain {
	private final PreStatusValidator preStatusValidator;
	private final StatusRegionValidator regionValidator;
	private final StatusUniqueIpValidator uniqueIpValidator;
	private final StatusAmountValidator amountStatusValidator;

	StatusValidatorChain(PreStatusValidator preStatusValidator,
						 StatusRegionValidator regionValidator,
						 StatusUniqueIpValidator uniqueIpValidator,
						 StatusAmountValidator amountValidator) {
		this.preStatusValidator = preStatusValidator;
		this.regionValidator = regionValidator;
		this.uniqueIpValidator = uniqueIpValidator;
		this.amountStatusValidator = amountValidator;
	}

	StatusValidator getStatusValidatorChain() {
		AbstractStatusValidator statusValidator = AbstractStatusValidator.link(
				preStatusValidator,
				regionValidator,
				uniqueIpValidator,
				amountStatusValidator);
		statusValidator.init();
		return statusValidator;
	}
}
