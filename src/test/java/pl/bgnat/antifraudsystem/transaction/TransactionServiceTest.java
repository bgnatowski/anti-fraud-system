package pl.bgnat.antifraudsystem.transaction;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.bgnat.antifraudsystem.exception.RequestValidationException;
import pl.bgnat.antifraudsystem.exception.stolenCard.CardNumberFormatException;
import pl.bgnat.antifraudsystem.exception.suspiciousIP.IpFormatException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static pl.bgnat.antifraudsystem.exception.RequestValidationException.WRONG_JSON_FORMAT;
import static pl.bgnat.antifraudsystem.exception.stolenCard.CardNumberFormatException.WRONG_CARD_NUMBER_FORMAT_S;
import static pl.bgnat.antifraudsystem.transaction.TransactionService.WRONG_REQUEST_AMOUNT_HAVE_TO_BE_POSITIVE_NUMBER;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {
	@Mock
	private TransactionValidator transactionValidator;
	@InjectMocks
	private TransactionService underTest;

	@Test
	void shouldThrowRequestValidationExceptionWhenValidTransactionWithWrongJsonFormatRequest() {
		// Given
		TransactionRequest transactionRequest = new TransactionRequest(120l, null, null);
		// When Then
		assertThatThrownBy(() -> underTest.validTransaction(transactionRequest))
				.isInstanceOf(RequestValidationException.class)
				.hasMessageContaining(WRONG_JSON_FORMAT);
		verify(transactionValidator, never()).isValidIpAddress(any());
		verify(transactionValidator, never()).isValidIpAddress(any());
	}

	@Test
	void shouldThrowCardNumberFormatExceptionWhenValidTransactionWithWrongCardFormatInRequest() {
		// Given
		TransactionRequest transactionRequest =
				new TransactionRequest(
						120l,
						"192.168.1.1",
						"4000008449433402");
		// When Then
		assertThatThrownBy(() -> underTest.validTransaction(transactionRequest))
				.isInstanceOf(CardNumberFormatException.class)
				.hasMessageContaining(String.format(WRONG_CARD_NUMBER_FORMAT_S, "4000008449433402"));
	}

	@Test
	void shouldThrowCardNumberFormatExceptionWhenValidTransactionWithWrongIPFormatInRequest() {
		// Given
		TransactionRequest transactionRequest =
				new TransactionRequest(
						120l,
						"192.168.356.1",
						"4000008449433403");
		// When Then
		when(transactionValidator.isValidCardNumber("4000008449433403")).thenReturn(true);
		assertThatThrownBy(() -> underTest.validTransaction(transactionRequest))
				.isInstanceOf(IpFormatException.class)
				.hasMessageContaining(String.format(IpFormatException.WRONG_IP_FORMAT_S, "192.168.356.1"));
	}

	@Test
	void shouldThrowRequestValidationExceptionWhenValidTransactionWithWrongAmount() {
		// Given
		TransactionRequest transactionRequest =
				new TransactionRequest(
						-20L,
						"192.168.1.1",
						"4000008449433403");
		// When Then
		when(transactionValidator.isValidCardNumber("4000008449433403")).thenReturn(true);
		when(transactionValidator.isValidIpAddress("192.168.1.1")).thenReturn(true);
		assertThatThrownBy(() -> underTest.validTransaction(transactionRequest))
				.isInstanceOf(RequestValidationException.class)
				.hasMessageContaining(WRONG_REQUEST_AMOUNT_HAVE_TO_BE_POSITIVE_NUMBER);
	}

	@Test
	void shouldReturnAllowedAndNoneWhenEverythingIsValid(){
		// Given
		String cardNumber = "4000008449433403";
		String ip = "192.168.1.1";
		TransactionRequest transactionRequest =
				new TransactionRequest(
						150L,
						ip,
						cardNumber);

		TransactionResponse expectedResponse = new TransactionResponse(TransactionStatus.ALLOWED, "none");
		// When Then
		when(transactionValidator.isValidCardNumber(cardNumber)).thenReturn(true);
		when(transactionValidator.isValidIpAddress(ip)).thenReturn(true);
		when(transactionValidator.isValidCardNumber(cardNumber)).thenReturn(true);
		when(transactionValidator.isValidIpAddress(ip)).thenReturn(true);
		TransactionResponse actualResponse = underTest.validTransaction(transactionRequest);

		assertThat(actualResponse).isEqualTo(expectedResponse);
	}

}
