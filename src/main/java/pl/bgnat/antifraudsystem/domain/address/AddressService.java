package pl.bgnat.antifraudsystem.domain.address;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.bgnat.antifraudsystem.dto.AddressDTO;

@Service
@RequiredArgsConstructor
class AddressService {
    private final AddressDTOMapper addressDTOMapper;
    AddressDTO mapToDto(Address address){
        return addressDTOMapper.apply(address);
    }
}
