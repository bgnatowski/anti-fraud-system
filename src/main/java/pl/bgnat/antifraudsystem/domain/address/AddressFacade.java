package pl.bgnat.antifraudsystem.domain.address;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.domain.user.User;
import pl.bgnat.antifraudsystem.dto.AddressDTO;
import pl.bgnat.antifraudsystem.dto.request.AddressRegisterRequest;

@Component
@RequiredArgsConstructor
public class AddressFacade {
    private final AddressService addressService;

    public AddressDTO mapToDto(Address address) {
        return addressService.mapToDto(address);
    }

    public Address assignAddress(User user, AddressRegisterRequest addressRegisterRequest) {
        return AddressCreator.createAddress(user, addressRegisterRequest);
    }
}
