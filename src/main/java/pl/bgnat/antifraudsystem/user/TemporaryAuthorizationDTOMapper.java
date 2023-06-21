package pl.bgnat.antifraudsystem.user;

import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.user.dto.PhoneNumberDTO;
import pl.bgnat.antifraudsystem.user.dto.TemporaryAuthorizationDTO;

import java.util.function.Function;

@Component
class TemporaryAuthorizationDTOMapper implements Function<TemporaryAuthorization, TemporaryAuthorizationDTO> {
	@Override
	public TemporaryAuthorizationDTO apply(TemporaryAuthorization temporaryAuthorization) {
		return TemporaryAuthorizationDTO.builder()
				.id(temporaryAuthorization.getId())
				.code(temporaryAuthorization.getCode())
				.expirationDate(temporaryAuthorization.getExpirationDate())
				.build();
	}
}
