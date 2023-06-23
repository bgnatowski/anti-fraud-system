package pl.bgnat.antifraudsystem.user;

import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.user.dto.PhoneNumberDTO;

import java.util.function.Function;

@Component
class PhoneNumberDTOMapper implements Function<PhoneNumber, PhoneNumberDTO> {
	@Override
	public PhoneNumberDTO apply(PhoneNumber number) {
		if(number==null) return PhoneNumberDTO.emptyDto();
		return PhoneNumberDTO.builder()
				.areaCode(number.getAreaCode())
				.number(number.getNumber())
				.build();
	}
}
