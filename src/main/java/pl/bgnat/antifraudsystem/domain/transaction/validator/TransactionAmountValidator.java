package pl.bgnat.antifraudsystem.domain.transaction.validator;

import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.domain.exceptions.IllegalAmountException;
import pl.bgnat.antifraudsystem.domain.transaction.TransactionFacade;
import pl.bgnat.antifraudsystem.domain.request.TransactionRequest;

import java.util.List;

@Component
class TransactionAmountValidator extends AbstractTransactionValidator {
    private static final int MAXIMUM_AMOUNT_WITHOUT_ANY_RESTRICTION = 200;
    static final int MAX_AMOUNT_FOR_MANUAL_PROCESSING = 1500;

    TransactionAmountValidator(TransactionFacade transactionFacade) {
        super(transactionFacade);
    }

    @Override
    public List<String> valid(TransactionRequest request, List<String> info) {
        if (request.amount() <= 0)
            throw new IllegalAmountException(request.amount());

        boolean containsIp = info.contains("ip");
        boolean containsCardNumber = info.contains("card-number");

        switch (getValidationType(containsIp, containsCardNumber)) {
            case BOTH_CONTAINED -> {
                if (request.amount() > MAX_AMOUNT_FOR_MANUAL_PROCESSING)
                    info.add("amount");
                return nextValidation(request, info);
            }
            case EITHER_CONTAINED -> {
                return info;
            }
            default -> {
                if (request.amount() <= MAXIMUM_AMOUNT_WITHOUT_ANY_RESTRICTION)
                    info.add("none");
                else
                    info.add("amount");

                return nextValidation(request, info);
            }
        }
    }

    private ValidationType getValidationType(boolean containsIp, boolean containsCardNumber) {
        if (containsIp && containsCardNumber) {
            return ValidationType.BOTH_CONTAINED;
        } else if (containsIp || containsCardNumber) {
            return ValidationType.EITHER_CONTAINED;
        } else {
            return ValidationType.NONE_CONTAINED;
        }
    }

    private enum ValidationType {
        BOTH_CONTAINED,
        EITHER_CONTAINED,
        NONE_CONTAINED
    }
}
