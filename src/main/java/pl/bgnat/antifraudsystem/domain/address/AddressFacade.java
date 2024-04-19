package pl.bgnat.antifraudsystem.domain.address;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.bgnat.antifraudsystem.domain.user.User;
import pl.bgnat.antifraudsystem.domain.request.AddressRegisterRequest;

@Component
@RequiredArgsConstructor
public class AddressFacade {

    public AddressDTO mapToDto(Address address) {
        return AddressDTO.MAPPER.map(address);
    }

    public Address assignAddress(User user, AddressRegisterRequest addressRegisterRequest) {
        return AddressCreator.createAddress(user, addressRegisterRequest);
    }
}
