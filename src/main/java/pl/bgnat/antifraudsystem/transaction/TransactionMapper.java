package pl.bgnat.antifraudsystem.transaction;

import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.transaction.dto.Region;
import pl.bgnat.antifraudsystem.transaction.dto.TransactionRequest;

import java.util.function.Function;

import static pl.bgnat.antifraudsystem.utils.DateTimeParser.parseDate;

@Component
class TransactionMapper implements Function<TransactionRequest, Transaction> {
	@Override
	public Transaction apply(TransactionRequest request) {
		return new Transaction(
				request.amount(),
				request.ip(),
				request.number(),
				Region.valueOf(request.region()),
				parseDate(request.date())
		);
	}
}
