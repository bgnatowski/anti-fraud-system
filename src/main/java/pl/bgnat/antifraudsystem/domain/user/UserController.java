package pl.bgnat.antifraudsystem.domain.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.bgnat.antifraudsystem.domain.request.*;
import pl.bgnat.antifraudsystem.domain.response.*;
import pl.bgnat.antifraudsystem.utils.date.DateTimeUtils;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/user")
class UserController {
    private final UserManager userManager;

    @GetMapping("/list")
        // admin/support - kiedy chce sprawdzić czy np jest takie konto -> potem sobie filtruje
    ResponseEntity<HttpResponse> getAllRegisteredUsers() {
        List<UserDTO> allRegisteredUsers = userManager.getAllRegisteredUsers();

        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(DateTimeUtils.currentLocalDateTime().toString())
                        .data(Map.of("allRegisteredUsers", allRegisteredUsers))
                        .message("Registered Users Retrieved")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }

    @PostMapping
        // rejestracja każdego
    ResponseEntity<HttpResponse> registerUser(@RequestBody @Valid UserRegistrationRequest user) {
        UserDTO registeredUser = userManager.registerUser(user);

        return new ResponseEntity<>(
                HttpResponse.builder()
                        .timeStamp(DateTimeUtils.currentLocalDateTime().toString())
                        .data(Map.of("registeredUser", registeredUser))
                        .message("User Registered")
                        .status(HttpStatus.CREATED)
                        .statusCode(HttpStatus.CREATED.value())
                        .build(),
                HttpStatus.CREATED);
    }

    @PatchMapping("/{username}/address/create")
        // merchant -  admin/support nie musi miec przypisanego
    ResponseEntity<HttpResponse> registerUserAddress(@Valid @RequestBody AddressRegisterRequest addressRegisterRequest,
                                                     @PathVariable("username") String username) {
        UserDTO registeredUserWithAddress = userManager.addAddress(username, addressRegisterRequest);
        return new ResponseEntity<>(
                HttpResponse.builder()
                        .timeStamp(DateTimeUtils.currentLocalDateTime().toString())
                        .data(Map.of("registeredUserWithAddress", registeredUserWithAddress))
                        .message("User address added")
                        .status(HttpStatus.CREATED)
                        .statusCode(HttpStatus.CREATED.value())
                        .build(),
                HttpStatus.CREATED);
    }

    @PatchMapping("/{username}/credit-card/create")
        //todo to account controller to moze przeniesc do konta
    ResponseEntity<HttpResponse> createCreditCard(@PathVariable("username") String username) {
        UserCreditCardCreatedResponse userWithCard = userManager.createCreditCardForUserWithUsername(username);
        return new ResponseEntity<>(
                HttpResponse.builder()
                        .timeStamp(DateTimeUtils.currentLocalDateTime().toString())
                        .data(Map.of("userWithCard", userWithCard))
                        .message("Created credit card for user")
                        .status(HttpStatus.CREATED)
                        .statusCode(HttpStatus.CREATED.value())
                        .build(),
                HttpStatus.CREATED);
    }

    @PatchMapping("/{username}/account/create")
    ResponseEntity<HttpResponse> createAccount(@PathVariable("username") String username) {
        UserAccountCreatedResponse userWithAccount = userManager.createAccountForUserWithUsername(username);
        return new ResponseEntity<>(
                HttpResponse.builder()
                        .timeStamp(DateTimeUtils.currentLocalDateTime().toString())
                        .data(Map.of("userWithAccount", userWithAccount))
                        .message("Created account for user")
                        .status(HttpStatus.CREATED)
                        .statusCode(HttpStatus.CREATED.value())
                        .build(),
                HttpStatus.CREATED);
    }

    @GetMapping("/{username}/details")
    ResponseEntity<HttpResponse> getUserDetails(@PathVariable("username") String username) {
        UserDTO userDTO = userManager.getUserByUsername(username);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(DateTimeUtils.currentLocalDateTime().toString())
                        .data(Map.of("userDTO", userDTO))
                        .message("User details retrieved")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }

    @DeleteMapping("/{username}")
    ResponseEntity<HttpResponse> deleteUser(@PathVariable("username") String username) {
        UserDeleteResponse userDeleteResponse = userManager.deleteUserByUsername(username);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(DateTimeUtils.currentLocalDateTime().toString())
                        .data(Map.of("userDeleteResponse", userDeleteResponse))
                        .message("User deleted")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }


    @PutMapping("/role")
        // manualna zmiana roli przez admina
    ResponseEntity<HttpResponse> changeRole(@RequestBody @Valid UserUpdateRoleRequest updateRequest) {
        UserDTO updatedUser = userManager.changeRole(updateRequest);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(DateTimeUtils.currentLocalDateTime().toString())
                        .data(Map.of("updatedUser", updatedUser))
                        .message("User role updated")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }

    @PutMapping("/access")
        //manualne zablokowanie/odblokowanie konta przez admina/supporta
    ResponseEntity<HttpResponse> changeAccess(@RequestBody @Valid UserUnlockRequest updateRequest) {
        UserUnlockResponse updatedUser = userManager.changeLock(updateRequest);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(DateTimeUtils.currentLocalDateTime().toString())
                        .data(Map.of("updatedUser", updatedUser))
                        .message("User access updated")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }

    @PatchMapping("/email/confirm") // all - kazdy moze potwierdzać email
    public ResponseEntity<HttpResponse> confirmEmail(@Valid @RequestBody ConfirmEmailRequest confirmEmailRequest) {
        UserEmailConfirmedResponse confirmedResponse = userManager.confirmUserEmail(confirmEmailRequest);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(DateTimeUtils.currentLocalDateTime().toString())
                        .data(Map.of("confirmedResponse", confirmedResponse))
                        .message(confirmedResponse.message())
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }

    @PatchMapping("/{username}/regenerate-activation-code")
    public ResponseEntity<HttpResponse> regenerateCode(@PathVariable String username) {
        String message = userManager.regenerateCodeForUser(username);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(DateTimeUtils.currentLocalDateTime().toString())
                        .data(Map.of("regeneratedResponse", message))
                        .message(message)
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }

}
