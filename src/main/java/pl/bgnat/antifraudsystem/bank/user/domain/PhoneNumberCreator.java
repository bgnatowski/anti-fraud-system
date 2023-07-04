package pl.bgnat.antifraudsystem.bank.user.domain;

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
