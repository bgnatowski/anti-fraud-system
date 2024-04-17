package pl.bgnat.antifraudsystem.bank.transaction.validation;

import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.bank.transaction.validation.exceptions.DateFormatException;
import pl.bgnat.antifraudsystem.bank.transaction.domain.TransactionFacade;
import pl.bgnat.antifraudsystem.bank.transaction.dto.TransactionRequest;
import pl.bgnat.antifraudsystem.utils.parser.DateTimeParser;

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
