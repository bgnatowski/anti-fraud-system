package pl.bgnat.antifraudsystem.domain.phone;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.bgnat.antifraudsystem.utils.Mapper;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
class PhoneNumberDTOMapper implements Mapper<PhoneNumber, PhoneNumberDTO> {
    private static Mapper<PhoneNumber, PhoneNumberDTO> instance;

    static Mapper<PhoneNumber, PhoneNumberDTO> getMapper() {
        if (instance == null) {
            instance = new PhoneNumberDTOMapper();
        }
        return instance;
    }

    @Override
    public PhoneNumberDTO apply(PhoneNumber number) {
        if (number == null) return PhoneNumberDTO.emptyDto();
        PhoneNumberDTO.PhoneNumberDTOBuilder builder = PhoneNumberDTO.builder()
                .number(number.getNumber());

        if (number.getUser() != null)
            builder.ownerId(number.getUser().getId());

        return builder.build();
    }


    @Override
    public PhoneNumberDTO map(PhoneNumber model) {
        return apply(model);
    }
}
