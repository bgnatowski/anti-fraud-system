package pl.bgnat.antifraudsystem.domain.phone;

import pl.bgnat.antifraudsystem.domain.user.User;

public class PhoneNumberCreator {
    public static PhoneNumber createPhoneNumber(User owner, String number) {
        return PhoneNumber.builder()
                .number(number)
                .user(owner)
                .build();
    }
}
