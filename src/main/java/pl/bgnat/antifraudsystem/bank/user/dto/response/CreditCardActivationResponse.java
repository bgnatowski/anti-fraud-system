package pl.bgnat.antifraudsystem.bank.user.dto.response;

import lombok.Builder;

@Builder
public record CreditCardActivationResponse(String message) {
	public static final String CREDIT_CARD_ACTIVATED = "Credit card activated";

}
