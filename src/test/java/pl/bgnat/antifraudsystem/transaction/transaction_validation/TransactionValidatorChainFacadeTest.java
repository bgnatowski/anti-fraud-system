package pl.bgnat.antifraudsystem.transaction.transaction_validation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.bgnat.antifraudsystem.AbstractTestcontainers;
import pl.bgnat.antifraudsystem.exception.RequestValidationException;
import pl.bgnat.antifraudsystem.transaction.TransactionRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static pl.bgnat.antifraudsystem.exception.RequestValidationException.WRONG_JSON_FORMAT;

@SpringBootTest
public class TransactionValidatorChainFacadeTest extends AbstractTestcontainers {

	@Autowired
	private TransactionValidatorFacade underTest;

	@Test
	void shouldReturnNoneWhenTransactionIsValid() {
		// given
		TransactionRequest transactionRequest = new TransactionRequest(
				150L, "192.168.1.1", "4000008449433403");
		// when
		String result = underTest.valid(transactionRequest);

		// then
		assertThat(result).isEqualTo("none");

	}

	@Test
	void shouldThrowRequestValidationExceptionWhenInvalidRequestWithNulls() {
		// given
		TransactionRequest transactionRequest = new TransactionRequest(
				800L, null, null);

		// when
		assertThatThrownBy(() -> underTest.valid(transactionRequest))
				.isInstanceOf(RequestValidationException.class)
				.hasMessageContaining(WRONG_JSON_FORMAT);

	}

	@Test
	void shouldThrowRequestValidationExceptionWhenInvalidRequestAmount() {
		// given
		TransactionRequest transactionRequest = new TransactionRequest(
				-20L, "192.168.1.1", "4000008449433403");

		// when
		assertThatThrownBy(() -> underTest.valid(transactionRequest))
				.isInstanceOf(RequestValidationException.class)
				.hasMessageContaining("Wrong request! Amount have to be positive number!");
	}

	@Test
	void shouldReturnAmount() {
		// given
		TransactionRequest transactionRequest = new TransactionRequest(
				800L, "192.168.1.1", "4000008449433403");
		String expectedInfo ="amount";
		// when
		String actualInfo = underTest.valid(transactionRequest);
		assertThat(actualInfo).isEqualTo(expectedInfo);
	}

}
