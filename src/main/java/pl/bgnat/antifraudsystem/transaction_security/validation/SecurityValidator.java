package pl.bgnat.antifraudsystem.transaction_security.validation;

import org.springframework.stereotype.Component;

@Component
public interface SecurityValidator<T> {
	boolean isValid(T data);
}
