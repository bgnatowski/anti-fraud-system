package pl.bgnat.antifraudsystem.bank.user.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.bgnat.antifraudsystem.bank.user.dto.AddressDTO;

@Service
@RequiredArgsConstructor
class AddressService {
    private final AddressDTOMapper addressDTOMapper;
    AddressDTO mapToDto(Address address){
        return addressDTOMapper.apply(address);
    }
}
