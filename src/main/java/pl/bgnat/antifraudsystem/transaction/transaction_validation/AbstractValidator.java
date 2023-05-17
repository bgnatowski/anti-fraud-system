package pl.bgnat.antifraudsystem.transaction.transaction_validation;

import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.transaction.TransactionRequest;

import java.util.List;
@Component
abstract class AbstractValidator implements TransactionValidator{
	private AbstractValidator nextValidator;
	public static AbstractValidator link(AbstractValidator first, AbstractValidator... chain) {
		AbstractValidator head = first;
		for (AbstractValidator nextInChain: chain) {
			head.nextValidator = nextInChain;
			head = nextInChain;
		}
		return first;
	}
	protected List<String> nextValidation(TransactionRequest request, List<String> info) {
		if (nextValidator != null) {
			return nextValidator.isValid(request, info);
		} else {
			return info;
		}
	}
}

