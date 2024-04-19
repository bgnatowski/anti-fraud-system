package pl.bgnat.antifraudsystem.domain.address;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.bgnat.antifraudsystem.utils.Mapper;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
class AddressDTOMapper implements Mapper<Address, AddressDTO> {
    private static Mapper<Address, AddressDTO> instance;

    static Mapper<Address, AddressDTO> getMapper() {
        if (instance == null) {
            instance = new AddressDTOMapper();
        }
        return instance;
    }

    @Override
    public AddressDTO apply(Address address) {
        if (address == null)
            return AddressDTO.emptyDto();

        AddressDTO.AddressDTOBuilder builder = AddressDTO.builder()
                .addressLine1(address.getAddressLine1())
                .addressLine2(address.getAddressLine2())
                .postalCode(address.getPostalCode())
                .city(address.getCity())
                .state(address.getState())
                .country(address.getCountry().getCountryName());

        if (address.getUser() != null)
            builder.ownerId(address.getUser().getId());

        return builder.build();
    }

    @Override
    public AddressDTO map(Address model) {
        return apply(model);
    }
}
