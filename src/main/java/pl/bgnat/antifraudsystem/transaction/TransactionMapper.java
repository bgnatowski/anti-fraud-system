package pl.bgnat.antifraudsystem.transaction;

import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.transaction.dto.Region;
import pl.bgnat.antifraudsystem.transaction.dto.TransactionRequest;

import java.util.function.Function;

import static pl.bgnat.antifraudsystem.utils.parser.DateTimeParser.parseDate;

@Component
class TransactionMapper implements Function<TransactionRequest, Transaction> {
	@Override
	public Transaction apply(TransactionRequest request) {
		return Transaction.builder()
				.amount(request.amount())
				.ipAddress(request.ip())
				.cardNumber(request.number())
				.region(Region.valueOf(request.region()))
				.date(parseDate(request.date()))
				.build();
	}
}
