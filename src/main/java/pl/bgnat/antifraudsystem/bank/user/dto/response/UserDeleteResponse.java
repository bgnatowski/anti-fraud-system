package pl.bgnat.antifraudsystem.bank.user.dto.response;

import lombok.Builder;

@Builder
public record UserDeleteResponse(String username, String status) {
	public static final String DELETED_SUCCESSFULLY_RESPONSE = "Deleted successfully!";
}
