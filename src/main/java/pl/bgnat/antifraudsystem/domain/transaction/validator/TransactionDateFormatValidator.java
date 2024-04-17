package pl.bgnat.antifraudsystem.domain.transaction.validator;

import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.domain.exceptions.DateFormatException;
import pl.bgnat.antifraudsystem.domain.transaction.TransactionFacade;
import pl.bgnat.antifraudsystem.dto.request.TransactionRequest;
import pl.bgnat.antifraudsystem.utils.date.DateTimeParser;

import java.time.format.DateTimeParseException;
import java.util.List;

@Component
class TransactionDateFormatValidator extends AbstractTransactionValidator{

	TransactionDateFormatValidator(TransactionFacade transactionFacade) {
		super(transactionFacade);
	}

	@Override
	public List<String> valid(TransactionRequest request, List<String> info) {
		String date = request.date();
		if(!isDateFormatValid(date))
			throw new DateFormatException(request.date());

		return nextValidation(request, info);
	}

	private boolean isDateFormatValid(String jsonDate) {
		try {
			DateTimeParser.parseDate(jsonDate);
			return true;
		} catch (DateTimeParseException e) {
			return false;
		}
	}
}
