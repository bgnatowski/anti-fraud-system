package pl.bgnat.antifraudsystem.domain.transaction.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.bgnat.antifraudsystem.AbstractTestcontainers;
import pl.bgnat.antifraudsystem.exception.RequestValidationException;
import pl.bgnat.antifraudsystem.domain.request.TransactionRequest;
import pl.bgnat.antifraudsystem.domain.response.TransactionResponse;
import pl.bgnat.antifraudsystem.domain.transaction.TransactionStatus;
import pl.bgnat.antifraudsystem.domain.exceptions.IllegalAmountException;
import pl.bgnat.antifraudsystem.utils.date.DateTimeUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static pl.bgnat.antifraudsystem.exception.RequestValidationException.WRONG_JSON_FORMAT;

@ExtendWith(MockitoExtension.class)
public class TransactionValidatorChainFacadeTest extends AbstractTestcontainers {

	@Mock private TransactionAmountValidator amountValidator;
	@Mock private TransactionIpValidator ipValidator;
	@Mock private TransactionCardNumberValidator cardNumberValidator;
	@Mock private TransactionRegionFormatValidator regionFormatValidator;

	@Mock private PreStatusValidator preStatusValidator;
	@Mock private StatusAmountValidator amountStatusValidator;
	@Mock private StatusRegionValidator regionValidator;
	@Mock private StatusUniqueIpValidator uniqueIpValidator;

	private TransactionValidatorChain transactionValidatorChain;
	private StatusValidatorChain statusValidatorChain;

	private TransactionValidatorFacade underTest;

	@BeforeEach
	void setUp() {
		transactionValidatorChain = new TransactionValidatorChain(
				amountValidator, ipValidator, cardNumberValidator, regionFormatValidator
		);
		statusValidatorChain = new StatusValidatorChain(
				preStatusValidator, regionValidator, uniqueIpValidator, amountStatusValidator);

		underTest = new TransactionValidatorFacade(transactionValidatorChain, statusValidatorChain);
	}

	@Test
	void shouldReturnNoneWhenTransactionIsValid() {
		// given
		TransactionRequest transactionRequest = new TransactionRequest(
				150L,
				"192.168.1.1",
				"4000008449433403",
				"EAP",
				DateTimeUtils.parseLocalDateTime("2022-12-22T16:07:00")
		);
		List<String> info = List.of("none");
		// when then
		when(transactionValidatorChain.getTransactionValidationFilterChain().valid(transactionRequest, new ArrayList<>())).
				thenReturn(info);
		when(statusValidatorChain.getStatusValidatorChain().valid(transactionRequest, info))
				.thenReturn(TransactionStatus.ALLOWED);

		TransactionResponse expected = new TransactionResponse(TransactionStatus.ALLOWED, "none");
		TransactionResponse actual = underTest.valid(transactionRequest);


		assertThat(actual).isEqualTo(expected);
	}

	@Test
	void shouldThrowRequestValidationExceptionWhenInvalidRequestWithNulls() {
		// given
		TransactionRequest transactionRequest = new TransactionRequest(
				null, null, null, null, null);
		// when then
		when(transactionValidatorChain.getTransactionValidationFilterChain().valid(eq(transactionRequest), anyList()))
				.thenThrow(new RequestValidationException(WRONG_JSON_FORMAT)
		);
		assertThatThrownBy(() -> underTest.valid(transactionRequest))
				.isInstanceOf(RequestValidationException.class)
				.hasMessageContaining(WRONG_JSON_FORMAT);
	}

	@Test
	void shouldThrowRequestValidationExceptionWhenInvalidRequestAmount() {
		// given
		TransactionRequest transactionRequest = new TransactionRequest(
				-20L,
				"192.168.1.1",
				"4000008449433403",
				"EAP",
				DateTimeUtils.parseLocalDateTime("2022-12-22T16:07:00"));
		// when then
		when(transactionValidatorChain.getTransactionValidationFilterChain().valid(eq(transactionRequest), anyList()))
				.thenThrow(new IllegalAmountException(-20L));

		assertThatThrownBy(() -> underTest.valid(transactionRequest))
				.isInstanceOf(IllegalAmountException.class)
				.hasMessageContaining(String.format(IllegalAmountException.WRONG_REQUEST_AMOUNT_HAVE_TO_BE_POSITIVE_NUMBER_PASSED_D, -20L));
	}

	@Test
	void shouldReturnAmount() {
		// given
		TransactionRequest transactionRequest = new TransactionRequest(
				800L,
				"192.168.1.1",
				"4000008449433403",
				"EAP",
				DateTimeUtils.parseLocalDateTime("2022-12-22T16:07:00"));
		List<String> info = List.of("amount");
		// When
		when(transactionValidatorChain.getTransactionValidationFilterChain().valid(transactionRequest, new ArrayList<>())).
				thenReturn(info);
		when(statusValidatorChain.getStatusValidatorChain().valid(transactionRequest, info))
				.thenReturn(TransactionStatus.ALLOWED);

		TransactionResponse expected = new TransactionResponse(TransactionStatus.ALLOWED, "amount");
		// Then
		TransactionResponse actual = underTest.valid(transactionRequest);
		assertThat(actual).isEqualTo(expected);
	}
}
