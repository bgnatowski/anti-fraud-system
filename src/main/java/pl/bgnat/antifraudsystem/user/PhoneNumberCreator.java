package pl.bgnat.antifraudsystem.user;

import org.springframework.stereotype.Component;

@Component
class PhoneNumberCreator {
	PhoneNumber createPhoneNumber(User owner, String number){
		return PhoneNumber.builder()
				.number(number)
				.user(owner)
				.build();
	}
}
