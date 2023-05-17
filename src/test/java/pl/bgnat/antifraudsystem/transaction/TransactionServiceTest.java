package pl.bgnat.antifraudsystem.transaction;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.bgnat.antifraudsystem.exception.RequestValidationException;
import pl.bgnat.antifraudsystem.exception.stolenCard.CardNumberFormatException;
import pl.bgnat.antifraudsystem.exception.suspiciousIP.IpFormatException;
import pl.bgnat.antifraudsystem.transaction.transaction_validation.TransactionValidatorFacade;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static pl.bgnat.antifraudsystem.exception.stolenCard.CardNumberFormatException.WRONG_CARD_NUMBER_FORMAT_S;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {
	@Mock
	private TransactionValidatorFacade validator;
	@InjectMocks
	private TransactionService underTest;

	@BeforeEach
	void setUp() {
		underTest = new TransactionService(
				validator);
	}
	@Test
	void shouldThrowRequestValidationExceptionWhenValidTransactionWithWrongJsonFormatRequest() {
		// Given
		TransactionRequest transactionRequest = new TransactionRequest(120l, null, null);
		// When Then
		underTest.validTransaction(transactionRequest);
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
		assertThatThrownBy(() -> underTest.validTransaction(transactionRequest))
				.isInstanceOf(RequestValidationException.class)
				.hasMessageContaining("Wrong request! Amount have to be positive number!");
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
		TransactionResponse actualResponse = underTest.validTransaction(transactionRequest);
		assertThat(actualResponse).isEqualTo(expectedResponse);
	}

}
