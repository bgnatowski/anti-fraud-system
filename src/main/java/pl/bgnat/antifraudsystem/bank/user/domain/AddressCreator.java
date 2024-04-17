package pl.bgnat.antifraudsystem.bank.user.domain;

import pl.bgnat.antifraudsystem.bank.user.dto.request.AddressRegisterRequest;
import pl.bgnat.antifraudsystem.bank.user.enums.Country;
import pl.bgnat.antifraudsystem.bank.user.exceptions.InvalidAddressFormatException;

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
