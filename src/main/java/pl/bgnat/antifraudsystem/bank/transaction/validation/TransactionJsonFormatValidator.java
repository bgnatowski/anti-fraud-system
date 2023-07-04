package pl.bgnat.antifraudsystem.bank.transaction.validation;


import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.exception.RequestValidationException;
import pl.bgnat.antifraudsystem.bank.transaction.TransactionFacade;
import pl.bgnat.antifraudsystem.bank.transaction.dto.TransactionRequest;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static pl.bgnat.antifraudsystem.exception.RequestValidationException.WRONG_JSON_FORMAT;

@Component
class TransactionJsonFormatValidator extends AbstractTransactionValidator {

	TransactionJsonFormatValidator(TransactionFacade transactionFacade) {
		super(transactionFacade);
	}

	@Override
	public List<String> valid(TransactionRequest request, List<String> info) {
		if (!isValidRequestJsonFormat(request))
			throw new RequestValidationException(WRONG_JSON_FORMAT);
		return nextValidation(request, info);
	}

	private boolean isValidRequestJsonFormat(TransactionRequest request) {
		return Stream.of(request.number(), request.amount(), request.ip()).noneMatch(Objects::isNull);
	}

}
