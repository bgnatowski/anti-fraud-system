package pl.bgnat.antifraudsystem.domain.user;

import lombok.Builder;
import pl.bgnat.antifraudsystem.domain.address.AddressDTO;
import pl.bgnat.antifraudsystem.domain.enums.Role;
import pl.bgnat.antifraudsystem.domain.phone.PhoneNumber;
import pl.bgnat.antifraudsystem.domain.phone.PhoneNumberDTO;
import pl.bgnat.antifraudsystem.utils.Mapper;

import java.time.LocalDate;

@Builder
public record UserDTO(Long id,
                      String firstName,
                      String lastName,
                      String username,
                      String email,
                      LocalDate dateOfBirth,
                      PhoneNumberDTO phoneNumberDTO,
                      AddressDTO addressDTO,
					  Long accountId,
                      Role role,
                      boolean isActive,
                      boolean hasAccount,
                      boolean hasAnyCreditCard,
                      int numberOfCreditCards
) {
	public static Mapper<User, UserDTO> MAPPER = UserDTOMapper.getMapper();
}
