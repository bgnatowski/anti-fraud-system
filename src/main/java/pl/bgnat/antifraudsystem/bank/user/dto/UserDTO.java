package pl.bgnat.antifraudsystem.bank.user.dto;

import lombok.Builder;
import pl.bgnat.antifraudsystem.bank.user.enums.Role;

import java.time.LocalDate;

@Builder
public record UserDTO(Long id,
					  String firstName,
					  String lastName,
					  String username,
					  String email,
					  LocalDate dateOfBirth,
					  Role role,
					  boolean isActive,
					  boolean hasAccount,
					  boolean hasAnyCreditCard,
					  int numberOfCreditCards,
					  AddressDTO address,
					  PhoneNumberDTO phoneNumber
) {
}
