package pl.bgnat.antifraudsystem.bank.user.domain;

class PhoneNumberCreator {
    static PhoneNumber createPhoneNumber(User owner, String number) {
        return PhoneNumber.builder()
                .number(number)
                .user(owner)
                .build();
    }
}
