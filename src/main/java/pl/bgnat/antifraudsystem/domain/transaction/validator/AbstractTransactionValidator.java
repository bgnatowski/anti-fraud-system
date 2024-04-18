package pl.bgnat.antifraudsystem.domain.transaction.validator;

import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.domain.transaction.TransactionFacade;
import pl.bgnat.antifraudsystem.domain.request.TransactionRequest;

import java.util.List;

@Component
abstract class AbstractTransactionValidator implements TransactionValidator{
	private AbstractTransactionValidator nextValidator;
	protected final TransactionFacade transactionFacade;

	AbstractTransactionValidator(TransactionFacade transactionFacade) {
		this.transactionFacade = transactionFacade;
	}

	public static AbstractTransactionValidator link(AbstractTransactionValidator first, AbstractTransactionValidator... chain) {
		AbstractTransactionValidator head = first;
		for (AbstractTransactionValidator nextInChain: chain) {
			head.nextValidator = nextInChain;
			head = nextInChain;
		}
		return first;
	}

	List<String> nextValidation(TransactionRequest request, List<String> info) {
		if (nextValidator != null) {
			return nextValidator.valid(request, info);
		} else {
			return info;
		}
	}
}

