package pl.bgnat.antifraudsystem.bank.user.domain;

import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.bank.user.dto.PhoneNumberDTO;

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
