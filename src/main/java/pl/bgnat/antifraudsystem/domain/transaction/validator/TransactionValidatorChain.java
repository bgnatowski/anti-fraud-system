package pl.bgnat.antifraudsystem.domain.transaction.validator;

import org.springframework.stereotype.Component;

@Component
class TransactionValidatorChain {
    private final TransactionAmountValidator amountValidator;
    private final TransactionIpValidator ipValidator;
    private final TransactionCardNumberValidator cardNumberValidator;
    private final TransactionRegionFormatValidator regionFormatValidator;

    TransactionValidatorChain(
            TransactionAmountValidator amountValidator,
            TransactionIpValidator ipValidator,
            TransactionCardNumberValidator cardNumberValidator,
            TransactionRegionFormatValidator regionFormatValidator) {
        this.amountValidator = amountValidator;
        this.ipValidator = ipValidator;
        this.cardNumberValidator = cardNumberValidator;
        this.regionFormatValidator = regionFormatValidator;
    }

    TransactionValidator getTransactionValidationFilterChain() {
        return AbstractTransactionValidator.link(
                regionFormatValidator,
                cardNumberValidator,
                ipValidator,
                amountValidator);
    }
}
