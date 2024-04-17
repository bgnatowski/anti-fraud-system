package pl.bgnat.antifraudsystem.domain.transaction.validator;

import pl.bgnat.antifraudsystem.dto.request.TransactionRequest;
import pl.bgnat.antifraudsystem.dto.TransactionStatus;

import java.util.List;

interface StatusValidator extends Validator<TransactionStatus> {
	TransactionStatus valid(TransactionRequest status, List<String> info);

	void init();
}
