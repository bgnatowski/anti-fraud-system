package pl.bgnat.antifraudsystem.domain.address;

import pl.bgnat.antifraudsystem.domain.user.User;
import pl.bgnat.antifraudsystem.domain.request.AddressRegisterRequest;
import pl.bgnat.antifraudsystem.domain.enums.Country;
import pl.bgnat.antifraudsystem.domain.exceptions.InvalidAddressFormatException;

class AddressCreator {
    static Address createAddress(User owner, AddressRegisterRequest addressRequest) {
        try {
            return Address.builder()
                    .addressLine1(addressRequest.addressLine1())
                    .addressLine2(addressRequest.addressLine2())
                    .city(addressRequest.city())
                    .state(addressRequest.state())
                    .country(Country.valueOf(addressRequest.country().toUpperCase()))
                    .postalCode(addressRequest.postalCode())
                    .user(owner)
                    .build();
        } catch (IllegalArgumentException e) {
            throw new InvalidAddressFormatException(addressRequest.country() + " not supported");
        }
    }
}
