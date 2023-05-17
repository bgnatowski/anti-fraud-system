package pl.bgnat.antifraudsystem.transaction;

import org.springframework.stereotype.Component;

import java.util.List;
@Component
public abstract class Validator implements TransactionValidator{
	private Validator nextValidator;
	public static Validator link(Validator first, Validator... chain) {
		Validator head = first;
		for (Validator nextInChain: chain) {
			head.nextValidator = nextInChain;
			head = nextInChain;
		}
		return first;
	}
	public void setNextValidator(Validator nextValidator) {
		this.nextValidator = nextValidator;
	}
	protected List<String> nextValidation(TransactionRequest request, List<String> info) {
		if (nextValidator != null) {
			return nextValidator.isValid(request, info);
		} else {
			return info;
		}
	}
}

