package pl.bgnat.antifraudsystem.dto.response;

import lombok.Builder;

@Builder
public record CreditCardActivationResponse(String message) {
	public static final String CREDIT_CARD_ACTIVATED = "Credit card activated";

}
