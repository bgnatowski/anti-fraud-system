package pl.bgnat.antifraudsystem.domain.phone;

import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.dto.PhoneNumberDTO;

import java.util.function.Function;

@Component
class PhoneNumberDTOMapper implements Function<PhoneNumber, PhoneNumberDTO> {
	@Override
	public PhoneNumberDTO apply(PhoneNumber number) {
		if(number==null) return PhoneNumberDTO.emptyDto();
		return PhoneNumberDTO.builder()
				.number(number.getNumber())
				.build();
	}
}
