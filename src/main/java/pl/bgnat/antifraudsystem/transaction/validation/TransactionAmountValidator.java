package pl.bgnat.antifraudsystem.transaction.validation;

import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.exception.RequestValidationException;
import pl.bgnat.antifraudsystem.transaction.dto.TransactionRequest;

import java.util.List;

@Component
class TransactionAmountValidator extends AbstractValidator {
	private static final String WRONG_REQUEST_AMOUNT_HAVE_TO_BE_POSITIVE_NUMBER =
			"Wrong request! Amount have to be positive number!";
	private static final int MAXIMUM_AMOUNT_WITHOUT_ANY_RESTRICTION = 200;
	static final int MAX_AMOUNT_FOR_MANUAL_PROCESSING = 1500;
	@Override
	public List<String> isValid(TransactionRequest request, List<String> info) {
		if(request.amount() <= 0)
			throw new RequestValidationException(WRONG_REQUEST_AMOUNT_HAVE_TO_BE_POSITIVE_NUMBER);

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
		if (containsIp && containsCardNumber)
			return ValidationType.BOTH_CONTAINED;
		else if (containsIp || containsCardNumber)
			return ValidationType.EITHER_CONTAINED;
		else
			return ValidationType.NONE_CONTAINED;
	}

	private enum ValidationType {
		BOTH_CONTAINED,
		EITHER_CONTAINED,
		NONE_CONTAINED
	}
}
