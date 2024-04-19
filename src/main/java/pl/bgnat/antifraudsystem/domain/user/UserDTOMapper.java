package pl.bgnat.antifraudsystem.domain.user;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.bgnat.antifraudsystem.domain.address.AddressDTO;
import pl.bgnat.antifraudsystem.domain.phone.PhoneNumberDTO;
import pl.bgnat.antifraudsystem.utils.Mapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class UserDTOMapper implements Mapper<User, UserDTO> {
    private static Mapper<User, UserDTO> instance;

    static Mapper<User, UserDTO> getMapper() {
        if (instance == null) {
            instance = new UserDTOMapper();
        }
        return instance;
    }

    @Override
    public UserDTO apply(User user) {
        UserDTO.UserDTOBuilder userDTOBuilder = UserDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .email(user.getEmail())
                .dateOfBirth(user.getDateOfBirth())
                .role(user.getRole())
                .isActive(user.isAccountNonLocked())
                .hasAccount(user.isHasAccount())
                .hasAnyCreditCard(user.isHasAnyCreditCard())
                .numberOfCreditCards(user.getNumberOfCreditCards());

        if (user.getAddress() != null)
            userDTOBuilder.addressDTO(AddressDTO.MAPPER.map(user.getAddress()));
        if (user.getPhone() != null)
            userDTOBuilder.phoneNumberDTO(PhoneNumberDTO.MAPPER.map(user.getPhone()));
        if (user.getAccount() != null)
            userDTOBuilder.accountId(user.getAccount().getId());

        return userDTOBuilder.build();
    }

    @Override
    public UserDTO map(User model) {
        return apply(model);
    }
}
