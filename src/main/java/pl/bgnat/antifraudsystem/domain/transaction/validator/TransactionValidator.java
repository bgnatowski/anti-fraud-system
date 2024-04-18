package pl.bgnat.antifraudsystem.domain.transaction.validator;

import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.domain.request.TransactionRequest;

import java.util.List;

@Component
interface TransactionValidator extends Validator<List<String>> {
	List<String> valid(TransactionRequest request, List<String> info);
}
