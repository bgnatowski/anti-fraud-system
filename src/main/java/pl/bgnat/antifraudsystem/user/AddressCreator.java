package pl.bgnat.antifraudsystem.user;

import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.user.dto.request.AddressRegisterRequest;
import pl.bgnat.antifraudsystem.user.enums.Country;
import pl.bgnat.antifraudsystem.user.exceptions.InvalidAddressFormatException;

@Component
class AddressCreator {
	Address createAddress(User owner, AddressRegisterRequest addressRequest){
		try{
		return Address.builder()
				.addressLine1(addressRequest.addressLine1())
				.addressLine2(addressRequest.addressLine2())
				.city(addressRequest.city())
				.state(addressRequest.state())
				.country(Country.valueOf(addressRequest.country().toUpperCase()))
				.postalCode(addressRequest.postalCode())
				.user(owner)
				.build();
		} catch (IllegalArgumentException e){
			throw new InvalidAddressFormatException(addressRequest.country()+" not supported");
		}
	}
}
