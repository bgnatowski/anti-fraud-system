package pl.bgnat.antifraudsystem.transaction;

import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.exception.RequestValidationException;

import java.util.List;

@Component
class TransactionAmountValidator extends Validator{
	private static final String WRONG_REQUEST_AMOUNT_HAVE_TO_BE_POSITIVE_NUMBER =
			"Wrong request! Amount have to be positive number!";
	public static final int MAXIMUM_AMOUNT_WITHOUT_ANY_RESTRICTION = 200;

	@Override
	public List<String> isValid(TransactionRequest request, List<String> info) {
		if(request.amount() <= 0)
			throw new RequestValidationException(WRONG_REQUEST_AMOUNT_HAVE_TO_BE_POSITIVE_NUMBER);
		if (request.amount() > MAXIMUM_AMOUNT_WITHOUT_ANY_RESTRICTION)
			info.add("amount");
		return nextValidation(request, info);
	}
}
