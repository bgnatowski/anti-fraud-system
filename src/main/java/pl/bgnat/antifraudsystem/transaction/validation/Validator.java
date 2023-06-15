package pl.bgnat.antifraudsystem.transaction.validation;

import pl.bgnat.antifraudsystem.transaction.dto.TransactionRequest;

import java.util.List;

interface Validator<T> {
	T valid(TransactionRequest status, List<String> info);
}
