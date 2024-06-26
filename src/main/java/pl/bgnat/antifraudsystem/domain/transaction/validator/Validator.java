package pl.bgnat.antifraudsystem.domain.transaction.validator;

import pl.bgnat.antifraudsystem.domain.request.TransactionRequest;

import java.util.List;

interface Validator<T> {
	T valid(TransactionRequest status, List<String> info);
}
