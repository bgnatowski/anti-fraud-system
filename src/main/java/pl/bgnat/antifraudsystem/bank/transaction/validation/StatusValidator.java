package pl.bgnat.antifraudsystem.bank.transaction.validation;

import pl.bgnat.antifraudsystem.bank.transaction.dto.TransactionRequest;
import pl.bgnat.antifraudsystem.bank.transaction.dto.TransactionStatus;

import java.util.List;

interface StatusValidator extends Validator<TransactionStatus> {
	TransactionStatus valid(TransactionRequest status, List<String> info);

	void init();
}
