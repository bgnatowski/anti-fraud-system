package pl.bgnat.antifraudsystem.user;

import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.user.dto.PhoneNumberDTO;
import pl.bgnat.antifraudsystem.user.dto.UserDTO;

import java.util.function.Function;

@Component
class PhoneNumberDTOMapper implements Function<PhoneNumber, PhoneNumberDTO> {
	@Override
	public PhoneNumberDTO apply(PhoneNumber number) {
		if(number==null) return PhoneNumberDTO.emptyPhone();
		return new PhoneNumberDTO(number.getFullNumber());
	}
}
