package pl.bgnat.antifraudsystem.bank.transaction.validation;

import pl.bgnat.antifraudsystem.bank.transaction.dto.TransactionRequest;

import java.util.List;

interface Validator<T> {
	T valid(TransactionRequest status, List<String> info);
}
