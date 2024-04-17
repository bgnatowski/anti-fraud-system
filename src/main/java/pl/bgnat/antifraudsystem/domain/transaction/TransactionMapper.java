package pl.bgnat.antifraudsystem.domain.transaction;

import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.dto.TransactionRegion;
import pl.bgnat.antifraudsystem.dto.request.TransactionRequest;

import java.util.function.Function;

import static pl.bgnat.antifraudsystem.utils.date.DateTimeParser.parseDate;

@Component
class TransactionMapper implements Function<TransactionRequest, Transaction> {
	@Override
	public Transaction apply(TransactionRequest request) {
		return Transaction.builder()
				.amount(request.amount())
				.ipAddress(request.ip())
				.cardNumber(request.number())
				.region(TransactionRegion.valueOf(request.region()))
				.date(parseDate(request.date()))
				.build();
	}
}
