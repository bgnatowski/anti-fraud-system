package pl.bgnat.antifraudsystem.domain.phone;

import lombok.Builder;
import pl.bgnat.antifraudsystem.utils.Mapper;

@Builder
public record PhoneNumberDTO(
        Long ownerId,
        String number) {
    public static PhoneNumberDTO emptyDto() {
        return new PhoneNumberDTO(0L, "");
    }

    public static Mapper<PhoneNumber, PhoneNumberDTO> MAPPER = PhoneNumberDTOMapper.getMapper();
}
