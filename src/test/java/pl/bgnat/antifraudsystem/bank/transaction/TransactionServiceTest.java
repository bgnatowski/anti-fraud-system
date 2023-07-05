//package pl.bgnat.antifraudsystem.transaction;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import pl.bgnat.antifraudsystem.exception.RequestValidationException;
//import pl.bgnat.antifraudsystem.transaction.dto.TransactionRequest;
//import pl.bgnat.antifraudsystem.transaction.dto.TransactionResponse;
//import pl.bgnat.antifraudsystem.transaction.dto.TransactionStatus;
//import pl.bgnat.antifraudsystem.transaction.stolenCards.exceptions.CardNumberFormatException;
//import pl.bgnat.antifraudsystem.transaction.suspiciousIP.exceptions.IpFormatException;
//import pl.bgnat.antifraudsystem.transaction.validation.TransactionValidatorFacade;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.mockito.BDDMockito.given;
//import static pl.bgnat.antifraudsystem.exception.RequestValidationException.WRONG_JSON_FORMAT;
//import static pl.bgnat.antifraudsystem.transaction.stolenCards.exceptions.CardNumberFormatException.WRONG_CARD_NUMBER_FORMAT_S;
//
//@ExtendWith(MockitoExtension.class)
//public class TransactionServiceTest {
//	@Mock
//	private TransactionValidatorFacade validator;
//	@InjectMocks
//	private TransactionService underTest;
//
//	@BeforeEach
//	void setUp() {
//		underTest = new TransactionService(
//				validator);
//	}
//	@Test
//	void shouldThrowRequestValidationExceptionWhenValidTransactionWithWrongJsonFormatRequest() {
//		// Given
//		TransactionRequest transactionRequest = new TransactionRequest(null, null, null);
//		given(validator.valid(transactionRequest)).willThrow(
//				new RequestValidationException(WRONG_JSON_FORMAT)
//		);
//		// When Then
//		assertThatThrownBy(() -> underTest.validTransaction(transactionRequest))
//				.isInstanceOf(RequestValidationException.class)
//				.hasMessageContaining(String.format(WRONG_JSON_FORMAT));
//	}
//
//	@Test
//	void shouldThrowCardNumberFormatExceptionWhenValidTransactionWithWrongCardFormatInRequest() {
//		// Given
//		TransactionRequest transactionRequest =
//				new TransactionRequest(
//						120L,
//						"192.168.1.1",
//						"4000008449433402");
//		given(validator.valid(transactionRequest)).willThrow(
//				new CardNumberFormatException("4000008449433402")
//		);
//		// When Then
//		assertThatThrownBy(() -> underTest.validTransaction(transactionRequest))
//				.isInstanceOf(CardNumberFormatException.class)
//				.hasMessageContaining(String.format(WRONG_CARD_NUMBER_FORMAT_S, "4000008449433402"));
//	}
//
//	@Test
//	void shouldThrowCardNumberFormatExceptionWhenValidTransactionWithWrongIPFormatInRequest() {
//		// Given
//		TransactionRequest transactionRequest =
//				new TransactionRequest(
//						120L,
//						"192.168.356.1",
//						"4000008449433403");
//
//		given(validator.valid(transactionRequest)).willThrow(
//				new IpFormatException("192.168.356.1")
//		);
//		// When Then
//		assertThatThrownBy(() -> underTest.validTransaction(transactionRequest))
//				.isInstanceOf(IpFormatException.class)
//				.hasMessageContaining(String.format(IpFormatException.WRONG_IP_FORMAT_S, "192.168.356.1"));
//	}
//
//	@Test
//	void shouldThrowRequestValidationExceptionWhenValidTransactionWithWrongAmount() {
//		// Given
//		TransactionRequest transactionRequest =
//				new TransactionRequest(
//						-20L,
//						"192.168.1.1",
//						"4000008449433403");
//		given(validator.valid(transactionRequest)).willThrow(
//				new RequestValidationException("Wrong request! Amount have to be positive number!")
//		);
//		// When Then
//		assertThatThrownBy(() -> underTest.validTransaction(transactionRequest))
//				.isInstanceOf(RequestValidationException.class)
//				.hasMessageContaining("Wrong request! Amount have to be positive number!");
//	}
//
//	@Test
//	void shouldReturnAllowedAndNoneWhenEverythingIsValid(){
//		// Given
//		String cardNumber = "4000008449433403";
//		String ip = "192.168.1.1";
//		TransactionRequest transactionRequest =
//				new TransactionRequest(
//						150L,
//						ip,
//						cardNumber);
//		TransactionResponse expectedResponse = new TransactionResponse(TransactionStatus.ALLOWED, "none");
//
//		given(validator.valid(transactionRequest)).willReturn("none");
//		// When Then
//		TransactionResponse actualResponse = underTest.validTransaction(transactionRequest);
//		assertThat(actualResponse).isEqualTo(expectedResponse);
//	}
//
//	@Test
//	void shouldReturnManualProcessingAndAmountWhenAmountIsToHigh(){
//		// Given
//		String cardNumber = "4000008449433403";
//		String ip = "192.168.1.1";
//		TransactionRequest transactionRequest =
//				new TransactionRequest(
//						800L,
//						ip,
//						cardNumber);
//
//		String info = "amount";
//		TransactionResponse expectedResponse =
//				new TransactionResponse(TransactionStatus.MANUAL_PROCESSING, info);
//		given(validator.valid(transactionRequest)).willReturn(info);
//		given(validator.getMaxAmountForManualProcessing()).willReturn(1500L);
//		// When Then
//		TransactionResponse actualResponse = underTest.validTransaction(transactionRequest);
//		assertThat(actualResponse).isEqualTo(expectedResponse);
//	}
//
//	@Test
//	void shouldReturnProhibitedAndAmountWhenAmountIsToHighHigh(){
//		// Given
//		String cardNumber = "4000008449433403";
//		String ip = "192.168.1.1";
//		TransactionRequest transactionRequest =
//				new TransactionRequest(
//						1800L,
//						ip,
//						cardNumber);
//
//		String info = "amount";
//		TransactionResponse expectedResponse =
//				new TransactionResponse(TransactionStatus.PROHIBITED, info);
//
//		given(validator.valid(transactionRequest)).willReturn(info);
//		// When Then
//		TransactionResponse actualResponse = underTest.validTransaction(transactionRequest);
//		assertThat(actualResponse).isEqualTo(expectedResponse);
//	}
//
//	@Test
//	void shouldReturnProhibitedAndIpWhenIpIsBlacklisted(){
//		// Given
//		String cardNumber = "4000008449433403";
//		String ip = "192.168.1.1";
//		TransactionRequest transactionRequest =
//				new TransactionRequest(
//						200L,
//						ip,
//						cardNumber);
//
//		String info = "ip";
//		TransactionResponse expectedResponse =
//				new TransactionResponse(TransactionStatus.PROHIBITED, info);
//
//		given(validator.valid(transactionRequest)).willReturn(info);
//		// When Then
//		TransactionResponse actualResponse = underTest.validTransaction(transactionRequest);
//		assertThat(actualResponse).isEqualTo(expectedResponse);
//	}
//}
