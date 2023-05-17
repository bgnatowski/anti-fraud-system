package pl.bgnat.antifraudsystem.transaction;

import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.exception.RequestValidationException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static pl.bgnat.antifraudsystem.exception.RequestValidationException.WRONG_JSON_FORMAT;
@Component
public class TransactionJsonFormatValidator extends Validator{
	@Override
	public List<String> isValid(TransactionRequest request, List<String> info) {
		if (!isValidRequestJsonFormat(request))
			throw new RequestValidationException(WRONG_JSON_FORMAT);
		return nextValidation(request, info);
	}

	private boolean isValidRequestJsonFormat(TransactionRequest request) {
		return Stream.of(request.number(), request.amount(), request.ip()).noneMatch(Objects::isNull);
	}
}
