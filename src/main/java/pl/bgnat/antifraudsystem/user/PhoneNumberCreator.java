package pl.bgnat.antifraudsystem.user;

import org.springframework.stereotype.Component;

@Component
class PhoneNumberCreator {
	PhoneNumber createPhoneNumber(User owner, String[] number){
		return PhoneNumber.builder()
				.areaCode(number[0])
				.number(number[1])
				.user(owner)
				.build();
	}
}
