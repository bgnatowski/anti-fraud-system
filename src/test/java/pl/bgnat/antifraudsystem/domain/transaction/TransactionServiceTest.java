package pl.bgnat.antifraudsystem.domain.transaction;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.bgnat.antifraudsystem.domain.exceptions.CardNumberFormatException;
import pl.bgnat.antifraudsystem.domain.exceptions.IpFormatException;
import pl.bgnat.antifraudsystem.domain.request.TransactionRequest;
import pl.bgnat.antifraudsystem.domain.response.TransactionResponse;
import pl.bgnat.antifraudsystem.domain.transaction.validator.TransactionValidatorFacade;
import pl.bgnat.antifraudsystem.exception.RequestValidationException;
import pl.bgnat.antifraudsystem.utils.date.DateTimeUtils;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static pl.bgnat.antifraudsystem.exception.RequestValidationException.WRONG_JSON_FORMAT;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {
    @Mock
    private TransactionValidatorFacade validator;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService underTest;

    @BeforeEach
    void setUp() {
        underTest = new TransactionService(validator, transactionRepository);
    }

    @Test
    void shouldThrowRequestValidationExceptionWhenValidTransactionWithWrongJsonFormatRequest() {
        // Given
        TransactionRequest transactionRequest = TransactionRequest.builder().amount(null).ip(null).number(null).amount(null).ip(null).build();
        given(validator.valid(transactionRequest)).willThrow(new RequestValidationException(WRONG_JSON_FORMAT));
        // When Then
        assertThatThrownBy(() -> underTest.transferTransaction(transactionRequest)).isInstanceOf(RequestValidationException.class).hasMessageContaining(String.format(WRONG_JSON_FORMAT));
    }

    @Test
    void shouldThrowCardNumberFormatExceptionWhenValidTransactionWithWrongCardFormatInRequest() {
        // Given
        TransactionRequest transactionRequest = TransactionRequest.builder()
                .amount(120L)
                .ip("192.168.1.1")
                .number("4000008449433402")
                .region("PL")
                .date(DateTimeUtils.currentLocalDateTime())
                .build();
        given(validator.valid(transactionRequest))
                .willThrow(new CardNumberFormatException("4000008449433402"));
        // When Then
        assertThatThrownBy(() -> underTest.transferTransaction(transactionRequest))
                .isInstanceOf(CardNumberFormatException.class)
                .hasMessageContaining(String.format(
                        CardNumberFormatException.ERROR_MESSAGE_WRONG_CARD_NUMBER_FORMAT_S, "number", "4000008449433402"));
    }

    @Test
    void shouldThrowCardNumberFormatExceptionWhenValidTransactionWithWrongIPFormatInRequest() {
        // Given
        TransactionRequest transactionRequest = TransactionRequest.builder()
                .amount(120L)
                .ip("192.168.356.1")
                .number("4000008449433403")
                .region("PL")
                .date(DateTimeUtils.currentLocalDateTime())
                .build();

        given(validator.valid(transactionRequest)).willThrow(new IpFormatException("192.168.356.1"));
        // When Then
        assertThatThrownBy(() -> underTest.transferTransaction(transactionRequest))
                .isInstanceOf(IpFormatException.class)
                .hasMessageContaining(String.format(IpFormatException.WRONG_IP_FORMAT_S, "192.168.356.1"));
    }

    @Test
    void shouldThrowRequestValidationExceptionWhenValidTransactionWithWrongAmount() {
        // Given
        TransactionRequest transactionRequest = TransactionRequest.builder()
                .amount(-20L)
                .ip("192.168.1.1")
                .number("4000008449433403")
                .region("PL")
                .date(DateTimeUtils.currentLocalDateTime())
                .build();
        given(validator.valid(transactionRequest)).willThrow(new RequestValidationException("Wrong request! Amount have to be positive number!"));
        // When Then
        assertThatThrownBy(() -> underTest.transferTransaction(transactionRequest))
                .isInstanceOf(RequestValidationException.class)
                .hasMessageContaining("Wrong request! Amount have to be positive number!");
    }

    @Test
    void shouldReturnAllowedAndNoneWhenEverythingIsValid() {
        // Given
        String cardNumber = "4000008449433403";
        String ip = "192.168.1.1";
        TransactionRequest transactionRequest = TransactionRequest.builder()
                .amount(150L)
                .ip(ip)
                .number(cardNumber)
                .region("PL")
                .date(DateTimeUtils.currentLocalDateTime())
                .build();
        TransactionResponse expectedResponse = new TransactionResponse(TransactionStatus.ALLOWED, "none");

        given(validator.valid(transactionRequest))
                .willReturn(expectedResponse);

        // When Then
        TransactionResponse actualResponse = underTest.transferTransaction(transactionRequest);
        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    void shouldReturnManualProcessingAndAmountWhenAmountIsToHigh() {
        // Given
        String cardNumber = "4000008449433403";
        String ip = "192.168.1.1";
        TransactionRequest transactionRequest = TransactionRequest.builder()
                .amount(800L)
                .ip(ip)
                .number(cardNumber)
                .region("PL")
                .date(DateTimeUtils.currentLocalDateTime())
                .build();

        String info = "amount";
        TransactionResponse expectedResponse = new TransactionResponse(TransactionStatus.MANUAL_PROCESSING, info);

        given(validator.valid(transactionRequest))
                .willReturn(expectedResponse);
        // When Then
        TransactionResponse actualResponse = underTest.transferTransaction(transactionRequest);
        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    void shouldReturnProhibitedAndAmountWhenAmountIsToHighHigh() {
        // Given
        String cardNumber = "4000008449433403";
        String ip = "192.168.1.1";
        TransactionRequest transactionRequest = TransactionRequest.builder()
                .amount(1800L)
                .ip(ip)
                .number(cardNumber)
                .region("PL")
                .date(DateTimeUtils.currentLocalDateTime())
                .build();

        String info = "amount";
        TransactionResponse expectedResponse = new TransactionResponse(TransactionStatus.PROHIBITED, info);

        given(validator.valid(transactionRequest))
                .willReturn(expectedResponse);
        // When Then
        TransactionResponse actualResponse = underTest.transferTransaction(transactionRequest);
        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    void shouldReturnProhibitedAndIpWhenIpIsBlacklisted() {
        // Given
        String cardNumber = "4000008449433403";
        String ip = "192.168.1.1";
        TransactionRequest transactionRequest = TransactionRequest.builder()
                .amount(200L)
                .ip(ip)
                .number(cardNumber)
                .region("PL")
                .date(DateTimeUtils.currentLocalDateTime())
                .build();

        String info = "ip";
        TransactionResponse expectedResponse = new TransactionResponse(TransactionStatus.PROHIBITED, info);

        given(validator.valid(transactionRequest)).willReturn(expectedResponse);
        // When Then
        TransactionResponse actualResponse = underTest.transferTransaction(transactionRequest);
        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }
}
