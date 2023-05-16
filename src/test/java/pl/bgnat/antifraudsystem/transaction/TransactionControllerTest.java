package pl.bgnat.antifraudsystem.transaction;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import pl.bgnat.antifraudsystem.transaction.request.TransactionRequest;
import pl.bgnat.antifraudsystem.transaction.response.TransactionResponse;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.bgnat.antifraudsystem.transaction.TransactionStatus.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {
	private static final String transactionApi = "/api/antifraud/transaction";
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private TransactionService transactionService;
	@SneakyThrows
	@Test
	@WithUserDetails
	@WithMockUser(authorities = "MERCHANT")
	public void testValidTransaction() {
		// Given
		TransactionRequest transactionRequest = new TransactionRequest(
				150L,
				"192.168.1.1",
				"4000008449433403"
		);

		String json = new ObjectMapper().writeValueAsString(transactionRequest);

		TransactionResponse expectedResponse = new TransactionResponse(ALLOWED, "none");
		given(transactionService.validTransaction(transactionRequest)).willReturn(expectedResponse);

		// When Then
		mockMvc.perform(post(transactionApi)
						.contentType(MediaType.APPLICATION_JSON)
						.content(json)
						.with(csrf()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.result", is(ALLOWED.name())))
				.andExpect(jsonPath("$.info", is("none")));
	}

	@SneakyThrows
	@Test
	@WithUserDetails
	@WithMockUser(authorities = "MERCHANT")
	public void testValidTransactionWithManualProcessingAmount() {
		// Given
		TransactionRequest transactionRequest = new TransactionRequest(
				800L,
				"192.168.1.1",
				"4000008449433403"
		);

		String json = new ObjectMapper().writeValueAsString(transactionRequest);

		TransactionResponse expectedResponse = new TransactionResponse(MANUAL_PROCESSING, "amount");
		given(transactionService.validTransaction(transactionRequest)).willReturn(expectedResponse);

		// When Then
		mockMvc.perform(post(transactionApi)
						.contentType(MediaType.APPLICATION_JSON)
						.content(json)
						.with(csrf()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.result", is(MANUAL_PROCESSING.name())))
				.andExpect(jsonPath("$.info", is("amount")));
	}

	@SneakyThrows
	@Test
	@WithUserDetails
	@WithMockUser(authorities = "MERCHANT")
	public void testValidTransactionWithProhibitedAmountAndIP() {
		// Given
		TransactionRequest transactionRequest = new TransactionRequest(
				1800L,
				"192.168.1.3",
				"4000008449433403"
		);

		String json = new ObjectMapper().writeValueAsString(transactionRequest);

		TransactionResponse expectedResponse = new TransactionResponse(PROHIBITED, "amount, ip");
		given(transactionService.validTransaction(transactionRequest)).willReturn(expectedResponse);

		// When Then
		mockMvc.perform(post(transactionApi)
						.contentType(MediaType.APPLICATION_JSON)
						.content(json)
						.with(csrf()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.result", is(PROHIBITED.name())))
				.andExpect(jsonPath("$.info", is("amount, ip")));
	}

	@SneakyThrows
	@Test
	@WithMockUser(authorities = {"ADMINISTRATOR", "SUPPORT"})
	@WithUserDetails
	public void testValidTransactionWithWrongAuthority() {
		// Given
		TransactionRequest transactionRequest = new TransactionRequest(
				150L,
				"192.168.1.1",
				"4000008449433403"
		);

		String json = new ObjectMapper().writeValueAsString(transactionRequest);

		TransactionResponse expectedResponse = new TransactionResponse(ALLOWED, "none");
		given(transactionService.validTransaction(transactionRequest)).willReturn(expectedResponse);

		// When Then
		mockMvc.perform(post(transactionApi)
						.contentType(MediaType.APPLICATION_JSON)
						.content(json))
				.andExpect(status().isForbidden());
	}


}
