package pl.bgnat.antifraudsystem.domain.address;

import lombok.Builder;
import pl.bgnat.antifraudsystem.utils.Mapper;

@Builder
public record AddressDTO(
        Long ownerId,
        String addressLine1,
        String addressLine2,
        String postalCode,
        String city,
        String state,
        String country
) {
    public static AddressDTO emptyDto() {
        return new AddressDTO(0L, "", "", "", "", "", "");
    }

    public static Mapper<Address, AddressDTO> MAPPER = AddressDTOMapper.getMapper();
}
