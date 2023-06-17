package pl.bgnat.antifraudsystem.user;

import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.user.dto.AddressDTO;

import java.util.function.Function;

@Component
class AddressDTOMapper implements Function<Address, AddressDTO> {
	@Override
	public AddressDTO apply(Address address) {
		if(address == null)
			return AddressDTO.emptyAddress();

		return new AddressDTO(
				address.getAddressLine1(),
				address.getAddressLine2(),
				address.getPostalCode(),
				address.getCity(),
				address.getState(),
				address.getCountry().getCountryName());
	}
}
